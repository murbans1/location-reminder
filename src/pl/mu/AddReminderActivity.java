package pl.mu;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import pl.mu.DB.DatabaseHelper;
import pl.mu.data.ReminderObject;
import pl.mu.service.ProximityAlertService;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddReminderActivity extends Activity implements OnClickListener{
	private EditText titleEt;
	private EditText descriptionEt;
	private TextView latTv;
	private TextView lonTv;
	private DatePicker endDateDp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_reminder);
		initUIElements();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.button_choose_location:
			Intent intent = new Intent(getApplicationContext(), LocationSelectionActivity.class);
			startActivityForResult(intent, 007);
			break;
		case R.id.button_add_reminder_confirm:
			if(inputedDataCorrect()) {
				if(saveReminderObject())
					startService(new Intent(AddReminderActivity.this, ProximityAlertService.class));
			}
			clearInputedData();
			break;
		case R.id.button_add_reminder_cancel:
			clearInputedData();
			break;
		}
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 007){
             latTv.setText((String) data.getExtras().get("lat"));
             lonTv.setText((String) data.getExtras().get("lon"));
        }
    }
	
	private boolean inputedDataCorrect() {
		if(titleEt.length() > 0 && latTv.length() > 0 && lonTv.length() > 0) 
			return true;
	    makeToast("Invalid data");
	    return false;
	}
	
	private void clearInputedData() {
		titleEt.setText("");
		descriptionEt.setText("");
		latTv.setText("");
		lonTv.setText("");
		// TODO: endDateDp clear
	}
	
	private boolean saveReminderObject() {
		DatabaseHelper databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        Dao<ReminderObject, String> dao = null;
        
        try {
			dao = databaseHelper.getReminderDao();
			dao.create(prepareReminderObject());
			makeToast("data were saved");
			return true;
        } catch (SQLException e) {
        	makeToast("problem saving data");
        	return false;
		}
	}

	private ReminderObject prepareReminderObject() {
		ReminderObject reminderObject = new ReminderObject(-1, titleEt.getText().toString(), datePickerToLong(endDateDp), latTv.getText().toString(), lonTv.getText().toString(), descriptionEt.getText().toString(), 0);
		return reminderObject;
	}
	
	private String datePickerToLong(DatePicker dp) {
		int year = dp.getYear();
		int month = dp.getMonth();
		int day = dp.getDayOfMonth();
		int hour = 23;
		int minute = 59;

		final Calendar c = Calendar.getInstance();
		c.set(year, month, day, hour, minute);
		
		return String.valueOf(c.getTimeInMillis());
	}
	
	private void makeToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
	
	private void initUIElements() {
		titleEt = (EditText) findViewById(R.id.editText_title); 
	    descriptionEt = (EditText) findViewById(R.id.editText_description);
		latTv = (TextView) findViewById(R.id.add_reminder_lat);
		lonTv = (TextView) findViewById(R.id.add_reminder_lon);
		endDateDp = (DatePicker) findViewById(R.id.datePicker);
		
		Button chooseLocationBtn = (Button)findViewById(R.id.button_choose_location);
		chooseLocationBtn.setOnClickListener(this);
		Button confirmBtn = (Button)findViewById(R.id.button_add_reminder_confirm);
		confirmBtn.setOnClickListener(this);
		Button cancelBtn = (Button)findViewById(R.id.button_add_reminder_cancel);
		cancelBtn.setOnClickListener(this);
	}
	
}
