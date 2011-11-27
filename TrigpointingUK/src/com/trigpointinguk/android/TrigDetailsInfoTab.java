package com.trigpointinguk.android;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.GpsStatus.NmeaListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.trigpointinguk.android.common.LatLon;

public class TrigDetailsInfoTab extends Activity {
	private static final String TAG="TrigDetailsInfoTab";
	private long mTrigId;
	private DbHelper mDb;
	private Uri 	mTUKUrl;
	private Uri 	mNavUrl;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.triginfo);

        // get trig_id from extras
        Bundle extras = getIntent().getExtras();
		if (extras == null) {return;}
		mTrigId = extras.getLong(DbHelper.TRIG_ID);
		Log.i("TrigInfo", "Trig_id = "+mTrigId);

		// get trig info from database
		mDb = new DbHelper(TrigDetailsInfoTab.this);
		mDb.open();		
		Cursor c = mDb.fetchTrigInfo(mTrigId);
		c.moveToFirst();

		
		mTUKUrl = Uri.parse( "http://www.trigpointinguk.com/trigs/trig-details.php?t="+c.getLong(c.getColumnIndex(DbHelper.TRIG_ID)) );
		mNavUrl = Uri.parse( "google.navigation:q=New+York+NY");
		
		
		TextView tv;
		ImageView iv;
		
		tv = (TextView)  findViewById(R.id.triginfo_name);
		tv.setText(c.getString(c.getColumnIndex(DbHelper.TRIG_NAME)));

		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity( new Intent( Intent.ACTION_VIEW, mTUKUrl ) );
			}
		});

		tv = (TextView)  findViewById(R.id.triginfo_waypoint);
		tv.setText(String.format("TP%04d", c.getLong(c.getColumnIndex(DbHelper.TRIG_ID))));
		
		iv = (ImageView) findViewById(R.id.triginfo_condition_icon);
		iv.setImageResource(Condition.fromCode(c.getString(c.getColumnIndex(DbHelper.TRIG_CONDITION))).icon());

		tv = (TextView) findViewById(R.id.triginfo_condition);
		tv.setText(Condition.fromCode(c.getString(c.getColumnIndex(DbHelper.TRIG_CONDITION))).toString());

		LatLon ll = new LatLon(c.getDouble(c.getColumnIndex(DbHelper.TRIG_LAT)), c.getDouble(c.getColumnIndex(DbHelper.TRIG_LON)));

		tv = (TextView)  findViewById(R.id.triginfo_gridref);
		tv.setText(ll.getOSGB10());
		
		tv = (TextView)  findViewById(R.id.triginfo_wgs84);
		tv.setText(ll.getWGS());
		
		tv = (TextView) findViewById(R.id.triginfo_current);
		tv.setText(Trig.Current.fromCode(c.getString(c.getColumnIndex(DbHelper.TRIG_CURRENT))).toString());

		tv = (TextView) findViewById(R.id.triginfo_historic);
		tv.setText(Trig.Historic.fromCode(c.getString(c.getColumnIndex(DbHelper.TRIG_HISTORIC))).toString());

		tv = (TextView) findViewById(R.id.triginfo_type);
		tv.setText(Trig.Physical.fromCode(c.getString(c.getColumnIndex(DbHelper.TRIG_TYPE))).toString());

		tv = (TextView) findViewById(R.id.triginfo_fb);
		tv.setText(c.getString(c.getColumnIndex(DbHelper.TRIG_FB)));

		c.close();
		mDb.close();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.trigdetailsinfomenu, menu);
		return result;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	       switch (item.getItemId()) {
	        // refresh the trig logs
	        case R.id.navigate:
	        	Log.i(TAG, "navigate");
				startActivity( new Intent( Intent.ACTION_VIEW, mNavUrl ) );
	        	return true;
	        case R.id.browser:
	        	Log.i(TAG, "browser");
				startActivity( new Intent( Intent.ACTION_VIEW, mTUKUrl ) );
	        	return true;
	        }
			return super.onOptionsItemSelected(item);
	}
	
}