package edu.calpoly.android.imfree;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.parse.PushService;

public class BaseActivity extends SherlockFragmentActivity {
   
	private String mActivityName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main_menu, menu);
		
		if (this.mActivityName.equals("ImFree")) {
		   menu.getItem(1).setVisible(false);
		   menu.getItem(2).setVisible(true);
		} else if (this.mActivityName.equals("WhosFree")) {
		   menu.getItem(1).setVisible(true);
         menu.getItem(2).setVisible(false);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.menu_imfree:
         finish();
			break;
			
		case R.id.menu_whosfree:
		   DataStore.addActivityToCallStack(BaseActivity.this);
			Intent whosFree = new Intent(BaseActivity.this, WhosFree.class);
         startActivity(whosFree);
			break;
			
		case R.id.menu_logout:
		   PushService.unsubscribe(this, "channel"+DataStore.getCurrentUser().getObjectId());
			DataStore.clearData();
			DataStore.clearCallStack();
			
			this.getSharedPreferences("edu.calpoly.android.imfree", Context.MODE_PRIVATE).edit().putString("username", "").commit();
			
         Intent logout = new Intent(BaseActivity.this, LoginActivity.class);
         logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         startActivity(logout);
         finish();
			break;
		
		default:
		   break;
		}
		return true;
	}
	
	public void setActivityName(String name) {
		mActivityName = name;
	}
}
