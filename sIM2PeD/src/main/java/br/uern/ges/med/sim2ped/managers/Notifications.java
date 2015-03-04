package br.uern.ges.med.sim2ped.managers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import br.uern.ges.med.sim2ped.MainActivity;
import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.utils.Constant;

public class Notifications {

	private Context context;

    public Notifications(Context context) {
		this.context = context;
	}

	/**
	 * Adds notification. 
	 * 
	 * @param IdNotification Id of notification.
	 * @param title Title of notification.
	 * @param contentText Text of notification.
	 * @param ticker Text of ticker in notification.
	 * @param onGoing If notification is permanent until canceled.
	 * 
	 * @return void
	 */
	public void addNotification(int IdNotification, String title, String contentText, String ticker, boolean onGoing) {
		String[] k = {};
    	String[] v = {};
    	this._addNotification(IdNotification, title, contentText, k, v, ticker, onGoing);
	}
	
	/**
	 * Adds notification. 
	 * 
	 * @param IdNotification Id of notification.
	 * @param title Title of notification.
	 * @param contentText Text of notification.
	 * @param keys Keys parameters bundle.
	 * @param values Values parameters bundle. The respective keys.
	 * @param ticker Text of ticker in notification.
	 * @param onGoing If notification is permanent until canceled.
	 * 
	 * @return void
	 */
	public void addNotification(int IdNotification, String title, String contentText, String[] keys, String[] values, String ticker, boolean onGoing) {
    	this._addNotification(IdNotification, title, contentText, keys, values, ticker, onGoing);
	}
	
	/**
	 * Adds notification. 
	 * 
	 * @param IdNotification Id of notification.
	 * @param title Title of notification.
	 * @param contentText Text of notification.
	 * @param keys Keys parameters bundle.
	 * @param values Values parameters bundle. The respective keys.
	 * @param onGoing If notification is permanent until canceled.
	 * 
	 * @return void
	 */
	public void addNotification(int IdNotification, String title, String contentText, String[] keys, String[] values, boolean onGoing) {
    	this._addNotification(IdNotification, title, contentText, keys, values, "", onGoing);
	}
	
	/**
	 * Adds notification. 
	 * 
	 * @param IdNotification Id of notification
	 * @param title Title of notification
	 * @param contentText Text of notification
	 * @param onGoing If notification is permanent until canceled
	 * 
	 * @return void
	 */
	public void addNotification(int IdNotification, String title, String contentText, boolean onGoing) {
		String[] k = {};
    	String[] v = {};
    	this._addNotification(IdNotification, title, contentText, k, v, "", onGoing);
	}
	
	
	private void _addNotification(int IdNotification, String title, String contentText, String[] keys, String[] values, String ticker, boolean onGoing) {

        NotificationCompat.Builder builderCompat = new NotificationCompat.Builder(context)
                // .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.ic_notification).setContentTitle(title).setContentText(contentText)
                .setOngoing(onGoing).setPriority(NotificationCompat.PRIORITY_MAX).setWhen(0)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
		
		if(onGoing)
			builderCompat.setWhen(System.currentTimeMillis());
		
		if(!ticker.equals(""))
			builderCompat.setTicker(ticker);

        Intent notificationIntent = new Intent(context, MainActivity.class);
		notificationIntent.putExtra(Constant.BUNDLE_NOTIFICATION_ID, IdNotification);

		for (int i = 0; i < keys.length; i++) {
			notificationIntent.putExtra(keys[i], values[i]);
		}

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,	PendingIntent.FLAG_UPDATE_CURRENT);
		builderCompat.setContentIntent(contentIntent);

		// Add as notification
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(IdNotification, builderCompat.build());
	}

	// Remove notification
	public void removeNotification(int IdNotification) {
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(IdNotification);
	}

}
