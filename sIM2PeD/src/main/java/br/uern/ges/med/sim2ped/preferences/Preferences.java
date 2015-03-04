package br.uern.ges.med.sim2ped.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import br.uern.ges.med.sim2ped.utils.Constant;

public class Preferences {

	private Context context;

	public Preferences(Context context){
		this.context = context;
	}
	
	public String getPrefLayoutTips(){
		SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(context);		
		return appPrefs.getString(Constant.PREF_LAYOUT_TIPS, "inline");
	}
	
	public String getPrefFontSizeTips(){
		SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(context);		
		return appPrefs.getString(Constant.PREF_FONT_SIZE_TIPS, "22");
	}

    public String getPrefEarlyMinutesForAlarm(){
        SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return appPrefs.getString(Constant.PREF_EARLY_MINUTES_FOR_ALARM, "5");
    }

    public String getPrefNumberOfTipsDisplayed(){
        SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return appPrefs.getString(Constant.PREF_NUMBER_OF_TIPS_DISPLAYED, "10");
    }

    public void setKeepLogged(boolean maintain){
        SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEd = appPrefs.edit();
        prefsEd.putBoolean(Constant.PREF_KEEP_LOGGED, maintain);
        prefsEd.commit();
    }

    public boolean getKeepLogged(){
        SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return appPrefs.getBoolean(Constant.PREF_KEEP_LOGGED, false);
    }

    public void setUserCodeLogged(long userCode){
        SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEd = appPrefs.edit();
        prefsEd.putLong(Constant.PREF_USER_CODE_LOGGED, userCode);
        prefsEd.commit();
    }

    public long getUserCodeLogged(){
        SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return appPrefs.getLong(Constant.PREF_USER_CODE_LOGGED, 0);
    }

}
