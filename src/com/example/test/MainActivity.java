package com.example.test;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
				
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);			
		
		btnStart = (Button)findViewById(R.id.btnStart);
		txtCountdown = (TextView)findViewById(R.id.txtCountdown);
		
		btnStart.setOnClickListener(btnStartOnClickListener);		
	}		
	
	private Button btnStart;
	private TextView txtCountdown;
	
	private OnClickListener btnStartOnClickListener = new OnClickListener() {
		public void onClick(View v) {			
			 new CountDownTimer(30000, 10) {

			     public void onTick(long millisUntilFinished) {
			    	 int sek = ((int)millisUntilFinished / 1000) % 60;
			    	 int min = (((int)millisUntilFinished / 1000) / 60) % 60;
			    	 
			    	 txtCountdown.setText(autoFill(String.valueOf(min), 2) + ":" 
			    			 		+ autoFill(String.valueOf(sek), 2));
			     }

			     public void onFinish() {
			    	 txtCountdown.setText("done!");
			     }
			  }.start();
		}
	};
	
    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
        	case R.id.action_edit:
        		
        		Intent intent = new Intent();
        		intent.setClass(this, SecondActivity.class);
        		startActivity(intent);        		
        		
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);        		
        }
    }
	
	private String autoFill(String strToFill, int maxLength) {
		
		if (strToFill.length() < maxLength) {
			
			while (strToFill.length() < maxLength)
			{
				strToFill = "0" + strToFill;
			}
			
		}
		
		return strToFill;
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	

}
