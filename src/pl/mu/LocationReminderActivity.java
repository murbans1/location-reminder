package pl.mu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LocationReminderActivity extends Activity implements OnClickListener{
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button addNewReminderBtn = (Button)findViewById(R.id.button_add_reminder);
        addNewReminderBtn.setOnClickListener(this);
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
}