/*
 * http://wptrafficanalyzer.in/blog/android-sidebar-navigation-drawer-with-icons/
 * http://stackoverflow.com/questions/17258020/switching-between-android-navigation-drawer-image-and-up-caret-when-using-fragme
 */

package br.uern.ges.med.sim2ped;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import br.uern.ges.med.sim2ped.adapter.NavigationAdapter;
import br.uern.ges.med.sim2ped.adapter.NavigationItemAdapter;
import br.uern.ges.med.sim2ped.beans.StatisticalUsage;
import br.uern.ges.med.sim2ped.beans.StatisticalViewProcedure;
import br.uern.ges.med.sim2ped.beans.User;
import br.uern.ges.med.sim2ped.fragments.AboutFragments;
import br.uern.ges.med.sim2ped.fragments.AlertCareFragments;
import br.uern.ges.med.sim2ped.fragments.DetailsTipFragment;
import br.uern.ges.med.sim2ped.fragments.ProceduresFragments;
import br.uern.ges.med.sim2ped.fragments.SendStatisticsFragment;
import br.uern.ges.med.sim2ped.fragments.SettingsFragments;
import br.uern.ges.med.sim2ped.fragments.TipsFragments;
import br.uern.ges.med.sim2ped.fragments.TipsFragments.OnTipSelectedListener;
import br.uern.ges.med.sim2ped.managers.Notifications;

import br.uern.ges.med.sim2ped.model.StatisticsModel;
import br.uern.ges.med.sim2ped.model.UserModel;
import br.uern.ges.med.sim2ped.preferences.Preferences;
import br.uern.ges.med.sim2ped.utils.Constant;
import br.uern.ges.med.sim2ped.utils.Constant.FragmentsMenu;

public class MainActivity extends ActionBarActivity implements OnTipSelectedListener {

	private String mTitle = "";
	private String[] mFragments;
    private DrawerLayout mDrawerLayout;
	private LinearLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationAdapter navigationAdapter;
	private int lastPosition = 0;
    private Bundle arguments = new Bundle();
    private long statisticId = 0;
    private Preferences preferences;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setIcon(R.drawable.ic_ab_logo);

		setContentView(R.layout.layout_main);

		// Enabling Up navigation
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		// Getting a reference to the drawer listview
        ListView mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		// Getting a reference to the sidebar drawer ( Title + ListView )
		mDrawer = (LinearLayout) findViewById(R.id.drawer);

		// Getting reference to DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Getting reference to the drawer to user details
        RelativeLayout userDrawer = (RelativeLayout) findViewById(R.id.userDrawer);

		// Getting an array of fragments names
		mFragments = getResources().getStringArray(R.array.nav_drawer_items);

		// mFlags = getResources().getStringArray(R.array.nav_drawer_icons);
        TypedArray mIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		// Just for the eclipse not claim the use of the method. I'm not use later... :)
		mIcons.recycle();

		navigationAdapter = new NavigationAdapter(this);

		for (int i = 0; i < mFragments.length; i++) {
			NavigationItemAdapter itemNavigation;

			if (mFragments[i].substring(0, 4).equals("sub_")) {
				mFragments[i] = mFragments[i].substring(4); // erasing sub_ string
				itemNavigation = new NavigationItemAdapter(mFragments[i], mIcons.getResourceId(i, 0));
				itemNavigation.setSub(true);
			} else {
				itemNavigation = new NavigationItemAdapter(mFragments[i], mIcons.getResourceId(i, 0));
			}
			navigationAdapter.addItem(itemNavigation);
		}

		// Setting the adapter to the listView
		mDrawerList.setAdapter(navigationAdapter);

		// User Details event handler for the drawer items
		userDrawer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawer);
            }
        });

		// ItemClick event handler for the drawer items
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                setLastPosition(position);
                showFragment(position, arguments);

                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawer);
            }
        });

		// Creating a ToggleButton for NavigationDrawer with drawer event listener
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer,
				R.string.app_name, R.string.app_name) {

			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu();
			}

			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu();
			}
		};

		// Setting event listener for the drawer
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState != null) {
            int lastPos = savedInstanceState.getInt(Constant.BUNDLE_LAST_POSITION);

            setLastPosition(lastPos);

			if (lastPosition < mFragments.length) {
				navigationAdapter.resetarCheck();
				navigationAdapter.setChecked(lastPosition, true);
			}

		} else {
			setLastPosition(lastPosition);
			showFragment(lastPosition, arguments);
		}

        preferences = new Preferences(this);

        UserModel userModel = new UserModel(this);
        User user = userModel.getUser(preferences.getUserCodeLogged());

        TextView textViewTitleDrawer = (TextView) findViewById(R.id.titleDrawer);
        TextView textViewSubTitleDrawer = (TextView) findViewById(R.id.subTitleDrawer);

        textViewTitleDrawer.setText(user.getFirstName() + " " + user.getLastName());
        textViewSubTitleDrawer.setText("# " + user.getId());

        StatisticsModel statisticsModel = new StatisticsModel(this);
        StatisticalUsage statisticalUsage = new StatisticalUsage();

        statisticalUsage.setEntryTime(Calendar.getInstance().getTimeInMillis());
        statisticalUsage.setUserId(preferences.getUserCodeLogged());

        statisticId = statisticsModel.insertStaticalUsage(statisticalUsage);

        Intent intent = getIntent();
        verifyNotificationItent(intent);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.i(Constant.TAG_LOG, "onNewIntent()");

		verifyNotificationItent(intent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(Constant.BUNDLE_LAST_POSITION, lastPosition);

		mTitle = mFragments[lastPosition];
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
        Log.i(Constant.TAG_LOG, "onConfigurationChanged()");

		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
        Log.i(Constant.TAG_LOG, "onPostCreate()");

		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();

		if (savedInstanceState != null) {
			mDrawerToggle.setDrawerIndicatorEnabled(true);
			mTitle = mFragments[lastPosition];
			getSupportActionBar().setTitle(mTitle);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

	public void showFragment(int position, Bundle arguments) {

		// Creating a fragment object
		Fragment fragment = null;

		FragmentsMenu[] fragmentMenu = FragmentsMenu.values();

		switch (fragmentMenu[position]) {
		case TIPS:
            if(arguments.isEmpty()){
                fragment = new TipsFragments();
            } else {
                fragment = new DetailsTipFragment();
            }
			break;

		case SETTINGS:
			fragment = new SettingsFragments();
			break;

		case PROCEDURES:
            if(arguments.isEmpty()){
                insertProcedureView();
                fragment = new ProceduresFragments();
            } else {
                fragment = new AlertCareFragments();
            }

			/*
			 * new Notifications(this). addNotification( Constant.NOTIF_ID_SYNC,
			 * this.getString(R.string.notif_need_sync_title), this.getString(R.string.notif_need_sync_content), true);
			 * int sync_pos = Integer.parseInt(getResources().getString(R.string.nav_item_sync));
			 * navigationAdapter.getItem(sync_pos).setEnable(); navigationAdapter.notifyDataSetChanged();
			 */
			break;

		case ABOUT:
			fragment = new AboutFragments();
			break;

		case SENT_HISTORY:

			int item_position = Integer.parseInt(getResources().getString(R.string.nav_item_sync));

			if (!navigationAdapter.getItem(item_position).isDisable()) {
                fragment = new SendStatisticsFragment();
			} else {
				Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.msg_no_history),
                        Toast.LENGTH_LONG).show();
			}


			break;

		default:
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.not_implementd), Toast.LENGTH_LONG).show();
			break;
		}

        if (fragment != null) {
            fragment.setArguments(arguments);
            // Open fragment instantiated
            openFragment(fragment, position);
        }

	}

    private void openFragment(Fragment fragment, int position) {

		// Currently selected country
		mTitle = mFragments[position];

		getSupportActionBar().setTitle(mTitle);

		// Getting reference to the FragmentManager
		FragmentManager fragmentManager = getSupportFragmentManager();

		// Creating a fragment transaction
		FragmentTransaction ft = fragmentManager.beginTransaction();

		// Adding a fragment to the fragment transaction
		ft.replace(R.id.frame_container, fragment);

		// Transition of fragments
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        if(!fragment.getArguments().isEmpty()){
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            ft.addToBackStack(null);
        }

		// Committing the transaction
		ft.commit();

		if (position < mFragments.length) {
			navigationAdapter.resetarCheck();
			navigationAdapter.setChecked(position, true);
        }
	}

	public void setLastPosition(int posicao) {
		this.lastPosition = posicao;
	}


	@Override
	public void onTipSelected(long ID) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.BUNDLE_TIP_ID, String.valueOf(ID));
        bundle.putString(Constant.BUNDLE_STATISTIC_ID, String.valueOf(statisticId));

        showFragment(FragmentsMenu.TIPS.value, bundle);
	}

	@Override
	public void onBackPressed() {
		// Back in home pressed fragments tutorial:
		// http://stackoverflow.com/questions/17258020/switching-between-android-navigation-drawer-image-and-up-caret-when-using-fragme

		super.onBackPressed();
		// turn on the Navigation Drawer image;
		// this is called in the LowerLevelFragments
		mDrawerToggle.setDrawerIndicatorEnabled(true);
	}

    private void verifyNotificationItent(Intent intent){
        Notifications not = new Notifications(this);

        int idNotification = intent.getIntExtra(Constant.BUNDLE_NOTIFICATION_ID, 0);

        if (idNotification > 0) {
            not.removeNotification(idNotification);

            switch (idNotification) {
                case Constant.NOTIF_ID_NEW_TIP:
                    Log.v(Constant.TAG_LOG, "Notification clicked: NEWTIP");
                    showFragment(FragmentsMenu.TIPS.value, intent.getExtras());
                    break;

                case Constant.NOTIF_ID_SYNC:
                    Log.v(Constant.TAG_LOG, "Notification clicked: NEED_SYNC");
                    break;

                case Constant.NOTIF_ID_NEW_CARE:
                    Log.v(Constant.TAG_LOG, "Notification clicked: NEWCARE");
                    showFragment(FragmentsMenu.PROCEDURES.value, intent.getExtras());
                    break;
            }
        }
    }

    private void insertProcedureView() {
        StatisticsModel statisticsModel = new StatisticsModel(this);
        StatisticalViewProcedure statisticalViewProcedure = new StatisticalViewProcedure();

        statisticalViewProcedure.setHourView(Calendar.getInstance().getTimeInMillis());
        statisticalViewProcedure.setStatisticalId(statisticId);
        statisticalViewProcedure.setUserId(preferences.getUserCodeLogged());

        statisticsModel.insertStaticalViewProcedure(statisticalViewProcedure);
    }

    public ActionBarDrawerToggle getMDrawerToggle(){
        return mDrawerToggle;
    }

    /*public NavigationAdapter getNavigationAdapter(){
        return navigationAdapter;
    }*/
}