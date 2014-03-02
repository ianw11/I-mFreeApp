package edu.calpoly.android.imfree;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

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
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_imfree:
			Intent imFree = new Intent(BaseActivity.this, ImFree.class);
            imFree.putExtra("ParseUser", mBaseUsername);
            startActivity(imFree);
			break;
		case R.id.menu_whosfree:
			Intent whosFree = new Intent(BaseActivity.this, WhosFree.class);
            whosFree.putExtra("ParseUser", mBaseUsername);
            startActivity(whosFree);
			break;
		case R.id.menu_logout:
			DataStore.clearData();
            Intent logout = new Intent(BaseActivity.this, LoginActivity.class);
            logout.putExtra("intent", mActivityName);
            startActivity(logout);
            finish();
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
