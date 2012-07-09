package pl.mu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import pl.mu.DB.DatabaseHelper;
import pl.mu.adapter.ReminderArrayAdapter;
import pl.mu.data.ReminderObject;
import pl.mu.service.ProximityAlertService;

public class LocationReminderActivity extends Activity implements OnClickListener {
	private String TAG = getClass().getSimpleName();
    private List<ReminderObject> reminderList;
    private ReminderArrayAdapter reminderArrayAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button addNewReminderBtn = (Button)findViewById(R.id.button_add_reminder);
        addNewReminderBtn.setOnClickListener(this);
        
        reminderList = new ArrayList<ReminderObject>();
        loadReminderObjectList();
        
        Log.d("LocationReminderActivity", String.valueOf(reminderList.size()));
        
        reminderArrayAdapter = new ReminderArrayAdapter(getApplicationContext(), reminderList);
        
        ListView listView = (ListView)findViewById(R.id.listview_reminders);
        listView.setAdapter(reminderArrayAdapter);
        
        if(reminderList.size() > 0)
        	startService(new Intent(LocationReminderActivity.this, ProximityAlertService.class));
    }
    
    @Override
	protected void onResume() {
		loadReminderObjectList();
    	reminderArrayAdapter.notifyDataSetChanged();
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.button_add_reminder:
			Intent intent = new Intent(getApplicationContext(), AddReminderActivity.class);
			startActivity(intent);	
			break;
		}
	}
	
	private void loadReminderObjectList() {
		DatabaseHelper databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        Dao<ReminderObject, String> dao = null;
        List<ReminderObject> tempList = new ArrayList<ReminderObject>();
        
		Date now = new Date();
		Long nowTimestamp = now.getTime(); 
        
        reminderList.clear();
        
        try {
			dao = databaseHelper.getReminderDao();			
			tempList = dao.queryForAll();
			for(int i = 0; i < tempList.size(); i++) {	
				if(nowTimestamp < Long.valueOf(tempList.get(i).endDateTimestamp))
					reminderList.add(tempList.get(i));
			}
			
			Log.d(TAG, "list length " + reminderList.size());
        } catch (SQLException e) {
        	makeToast("problem saving data");
		}
	}
	
	private void makeToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
}