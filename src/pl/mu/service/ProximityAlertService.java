package pl.mu.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import pl.mu.DB.DatabaseHelper;
import pl.mu.data.ReminderObject;
import pl.mu.receiver.ProximityAlertReceiver;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

public class ProximityAlertService extends Service {
	private String TAG = getClass().getSimpleName();
	private static final String PROXIMITY_INTENT_ACTION = new String("pl.mu.action.PROXIMITY_ALERT");	
	private List<ReminderObject> datas;
	private IntentFilter intentFilter;
	private LocationManager locationManager;
	private ProximityAlertReceiver proximityAlertReceiver;
	
	@Override
	public void onCreate() {
		proximityAlertReceiver = new ProximityAlertReceiver();
		intentFilter = new IntentFilter(PROXIMITY_INTENT_ACTION);
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "on start");
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		addProximityAlert();
		registerReceiver(proximityAlertReceiver, intentFilter);
		
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(proximityAlertReceiver);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	public void addProximityAlert() {
		readData();
		clearProximityReminders();
		removeOutdated();
        registerIntents();
	}
	
	private void readData() {
    	datas = new ArrayList<ReminderObject>();
    	loadReminderObjectList();
        Log.d(TAG, "datas " + datas.size());
	}
	
	private void clearProximityReminders() {
		for(int i = 0; i < datas.size(); i++) {
			if(datas.get(i).checkIfAdded() == 1) {
				ReminderObject reminderObject = datas.get(i);
				removeProximityAlert(reminderObject);
				markReminderAdded(reminderObject,0);
			}
    	}
	}
	
	private void registerIntents() {
    	for(int i = 0; i < datas.size(); i++) {
    		ReminderObject reminderObject = datas.get(i);
    		setProximityAlert(reminderObject, -1);
    		markReminderAdded(reminderObject, 1);
    	}
    }
	
	private void setProximityAlert(ReminderObject reminderObject, int i) {
    	float radius = 1000f; // 1000 meter radius
    	long expiration = 600000; // Expiration is 10 Minutes (10mins * 60secs * 1000milliSecs)
    	expiration = timeLeftToExpiry(reminderObject.endDateTimestamp);
    	
    	PendingIntent pendingIntent = createPendingIntent(reminderObject, i); // TODO: check if it is not used as stated in documentation
    	locationManager.addProximityAlert(reminderObject.getLatDouble(), reminderObject.getLonDouble(), radius, expiration, pendingIntent);
    	
    	Log.d(TAG, "Proximity Alert added " + reminderObject.title);
    }
	
	private void removeProximityAlert(ReminderObject reminderObject) {
		PendingIntent pendingIntent = createPendingIntent(reminderObject, -1); // TODO: check if it is not used as stated in documentation
    	locationManager.removeProximityAlert(pendingIntent);
	}

	private PendingIntent createPendingIntent(ReminderObject reminderObject, int requestCode) {
		Intent intent = new Intent(PROXIMITY_INTENT_ACTION);
    	intent.putExtra(ProximityAlertReceiver.EVENT_ID_INTENT_EXTRA, reminderObject.id);
    	intent.putExtra(ProximityAlertReceiver.EVENT_NOTIFICATION_EXTRA, reminderObject.title);
    	PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		return pendingIntent;
	}
	
	private void loadReminderObjectList() {
		DatabaseHelper databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        Dao<ReminderObject, String> dao = null;
        
        try {
			dao = databaseHelper.getReminderDao();
			datas = dao.queryForAll();
        } catch (SQLException e) {
        	// TODO: throw exception
		}
	}
	
	private void markReminderAdded(ReminderObject reminderObject, int i) {
		DatabaseHelper databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        Dao<ReminderObject, String> dao = null;
        
        try {
        	dao = databaseHelper.getReminderDao();
        	reminderObject.markAdded(i);
			dao.update(reminderObject);
        } catch (SQLException e) {
        	// TODO: throw exception
		}
	}
	
	private void removeReminder(ReminderObject reminderObject) {
		DatabaseHelper databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        Dao<ReminderObject, String> dao = null;
        
        try {
        	dao = databaseHelper.getReminderDao();
			dao.delete(reminderObject);
        } catch (SQLException e) {
        	// TODO: throw exception
		}
	}
	
	private void removeOutdated() {
		for(int i = 0; i < datas.size(); i++) {
    		if(outDated(datas.get(i))) {
    			removeReminder(datas.get(i));
    			datas.clear();
    			loadReminderObjectList();
    		}
    	}
	}
	
	private boolean outDated(ReminderObject reminderObject) {
		Date now = new Date();
		Long nowTimestamp = now.getTime(); 
		
		if(nowTimestamp > Long.valueOf(reminderObject.endDateTimestamp))
			return true;
		return false;
	}
	
	private long timeLeftToExpiry(String endDateTimestamp) {
		Date now = new Date();
		Long nowTimestamp = now.getTime(); 
		
		return Long.valueOf(endDateTimestamp) - nowTimestamp;
	}
}
