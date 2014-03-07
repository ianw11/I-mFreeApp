package edu.calpoly.android.imfree;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.parse.PushService;

public class BaseActivity extends SherlockFragmentActivity {
	private String mBaseUsername;
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
			Intent whosFree = new Intent(BaseActivity.this, WhosFree.class);
         whosFree.putExtra("ParseUser", mBaseUsername);
         startActivity(whosFree);
			break;
			
		case R.id.menu_logout:
		   PushService.unsubscribe(this, "channel"+DataStore.getCurrentUser().getObjectId());
			DataStore.clearData();
			this.getSharedPreferences("edu.calpoly.android.imfree", Context.MODE_PRIVATE).edit().putString("username", "").commit();
         Intent logout = new Intent(BaseActivity.this, LoginActivity.class);
         logout.putExtra("intent", mActivityName);
         logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         startActivity(logout);
         finish();
			break;
		
		default:
		   break;
		}
		return true;
	}
	
	public void setBaseUsername(String name) {
		mBaseUsername = name;
	}
	
	public void setActivityName(String name) {
		mActivityName = name;
	}
}
