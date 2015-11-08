package com.example.test;

import swipe_adpater.TabsPagerAdapter;
import android.app.ActionBar;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

public class SecondActivity extends FragmentActivity {
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_activity);	
		
		// Init		
		viewPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		
		this.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
		    public void onPageScrollStateChanged(int state) {}
		    
		    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		    	setSlider((position * slider) + slider * positionOffset);
		    }

		    public void onPageSelected(int position) {}
		});
				
		setSlider();
	}
	
	
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ProgressBar pbarSlider;
	//private OnPageChangeListener onPageChangeListener;
	
	private int totalWidth;
	private float sliderBackground, slider, offsetLeft;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.action_accept:
	    	break;
	    case R.id.action_cancel:
	     	this.finish();
	    	break;        
	    default:
	        return super.onOptionsItemSelected(item);
	    }
		return false;
	}
		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_second_actions, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	private void setSlider(){
		ProgressBar pbarBackground = (ProgressBar)findViewById(R.id.pBarBackground);
		pbarSlider = (ProgressBar)findViewById(R.id.pBarSlider);
		
		pbarSlider.setProgress(pbarSlider.getMax());
		
		Display display = this.getWindowManager().getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		
		totalWidth = point.x;
		sliderBackground = (float)(totalWidth * 0.75);
		slider = sliderBackground / mAdapter.getCount();
		offsetLeft = (float)(totalWidth * 0.125);
		
		// Set Slider Background width.
		pbarBackground.getLayoutParams().width = (int)sliderBackground;
		pbarBackground.invalidate();
		
		// Set Slider width.
		pbarSlider.getLayoutParams().width = (int)slider;	
		pbarSlider.invalidate();
	}
	
	private void setSlider(float OffSet) {
		
		pbarSlider.setX(offsetLeft + OffSet);
		pbarSlider.invalidate();
	}
	
	
}
