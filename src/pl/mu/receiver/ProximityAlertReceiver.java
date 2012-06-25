package pl.mu.receiver;

import pl.mu.LocationReminderActivity;
import pl.mu.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.util.Log;

public class ProximityAlertReceiver extends BroadcastReceiver {
	private String TAG = getClass().getSimpleName(); 
	private static final int NOTIFICATION_ID = 1000;
	public static final String EVENT_ID_INTENT_EXTRA = "EventIDIntentExtraKey";
	public static final String EVENT_NOTIFICATION_EXTRA = "EventNotificationExtraKey";

	@Override
	public void onReceive(Context context, Intent intent) {
		String eventTitle = intent.getStringExtra(EVENT_NOTIFICATION_EXTRA);
		String event = "";
		
		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		Boolean entering = intent.getBooleanExtra(key, false);
		
		if (entering) {
			event = "Entering";
			Log.d(TAG, "entering");
		}
		else {
			event = "Exiting";
			Log.d(TAG, "exiting");
		}
		
		NotificationManager notificationManager = 
			(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Intent notifyIntent = new Intent(context, LocationReminderActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0);		
		
		Notification notification = createNotification();
		notification.setLatestEventInfo(context, event + " Reminder Alert!!!", "Remember about: " + eventTitle, pendingIntent);
		notificationManager.notify(NOTIFICATION_ID, notification);
		
	}
	
	private Notification createNotification() {
		Notification notification = new Notification();
		
		notification.icon = R.drawable.ic_menu_notifications;
		notification.when = System.currentTimeMillis();
		
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		
		notification.ledARGB = Color.WHITE;
		notification.ledOnMS = 1500;
		notification.ledOffMS = 1500;
		
		return notification;
	}
	
}
