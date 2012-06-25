package pl.mu.service;

import java.sql.SQLException;
import java.util.ArrayList;
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
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "on start");
		addProximityAlert();
		registerIntentReceiver();
//		return super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	private void registerIntentReceiver() {
		intentFilter = new IntentFilter(PROXIMITY_INTENT_ACTION);
		registerReceiver(new ProximityAlertReceiver(), intentFilter);
	}
	
	public void addProximityAlert() {
		readData();
        registerIntents();
	}
	
	private void readData() {
    	datas = new ArrayList<ReminderObject>();
    	loadReminderObjectList();
    	datas.add(new ReminderObject(9001, "1st reminder", "0", "52.236618", "21.027979", "1st description"));
    	datas.add(new ReminderObject(9002, "2nd reminder", "0", "52.263417" ,"21.035682", "2nd description"));
        Log.d(TAG, "datas " + datas.size());
	}
	
	private void registerIntents() {
    	for(int i = 0; i < datas.size(); i++) {
    		ReminderObject reminderObject = datas.get(i);
    		setProximityAlert(reminderObject, i);
    	}
    }
	
	private void setProximityAlert(ReminderObject reminderObject, int requestCode)
    {
    	// 1000 meter radius
    	float radius = 1000f;
    	// Expiration is 10 Minutes (10mins * 60secs * 1000milliSecs)
    	long expiration = 600000;
    	
    	LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	
    	Intent intent = new Intent(PROXIMITY_INTENT_ACTION);
    	intent.putExtra(ProximityAlertReceiver.EVENT_ID_INTENT_EXTRA, reminderObject.id);
    	intent.putExtra(ProximityAlertReceiver.EVENT_NOTIFICATION_EXTRA, reminderObject.title);
    	PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    	
    	locationManager.addProximityAlert(reminderObject.getLatDouble(), reminderObject.getLonDouble(), radius, expiration, pendingIntent);
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
}
