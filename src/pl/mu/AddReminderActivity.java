package pl.mu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddReminderActivity extends Activity implements OnClickListener{
	private EditText titleEt;
	private EditText descriptionEt;
	private TextView latTv;
	private TextView lonTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_reminder);
		
		titleEt = (EditText)findViewById(R.id.editText_title); 
	    descriptionEt = (EditText)findViewById(R.id.editText_description);
		latTv = (TextView)findViewById(R.id.add_reminder_lat);
		lonTv = (TextView)findViewById(R.id.add_reminder_lon);
		
		Button chooseLocationBtn = (Button)findViewById(R.id.button_choose_location);
		chooseLocationBtn.setOnClickListener(this);
		Button confirmBtn = (Button)findViewById(R.id.button_add_reminder_confirm);
		confirmBtn.setOnClickListener(this);
		Button cancelBtn = (Button)findViewById(R.id.button_add_reminder_cancel);
		cancelBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.button_choose_location:
			Intent intent = new Intent(getApplicationContext(), LocationSelectionActivity.class);
			//startActivityForResult(intent, 007);
			startActivity(intent);
			break;
		case R.id.button_add_reminder_confirm:
			break;
		case R.id.button_add_reminder_cancel:
			break;
		}
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 007){
             String result = (String) data.getExtras().get("result");
             latTv.setText("");
             lonTv.setText("");
        }
    }
}
