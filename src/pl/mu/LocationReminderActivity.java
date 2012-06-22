package pl.mu;

import java.sql.SQLException;
import java.util.ArrayList;
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

import pl.mu.DB.DatabaseHelper;
import pl.mu.adapter.ReminderArrayAdapter;
import pl.mu.data.ReminderObject;

public class LocationReminderActivity extends Activity implements OnClickListener{
    private List<ReminderObject> reminderList;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button addNewReminderBtn = (Button)findViewById(R.id.button_add_reminder);
        addNewReminderBtn.setOnClickListener(this);
        
        reminderList = new ArrayList<ReminderObject>();
        loadReminderObjectList();
        
        Log.d("LocationReminderActivity", String.valueOf(reminderList.size()));
        
        ReminderArrayAdapter reminderArrayAdapter = new ReminderArrayAdapter(getApplicationContext(), reminderList);
        
        ListView listView = (ListView)findViewById(R.id.listview_reminders);
        listView.setAdapter(reminderArrayAdapter);
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
        
        try {
			dao = databaseHelper.getReminderDao();
			reminderList = dao.queryForAll();
//			makeToast("data were saved");
        } catch (SQLException e) {
//        	makeToast("problem saving data");
		}
	}
}