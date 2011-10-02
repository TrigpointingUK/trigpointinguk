package com.trigpointinguk;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class TrigInfo extends Activity {
	private long mTrigId;
	private TrigDbHelper mDb;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.triginfo);

        // get trig_id from extras
        Bundle extras = getIntent().getExtras();
		if (extras == null) {return;}
		mTrigId = extras.getLong(TrigDbHelper.TRIG_ID);
		Log.i("TrigInfo", "Trig_id = "+mTrigId);

		// get trig info from database
		mDb = new TrigDbHelper(TrigInfo.this);
		mDb.open();		
		Cursor c = mDb.fetchTrigInfo(mTrigId);
		c.moveToFirst();
		
		TextView tv;
		ImageView iv;
		
		tv = (TextView)  findViewById(R.id.triginfo_name);
		tv.setText(c.getString(c.getColumnIndex(TrigDbHelper.TRIG_NAME)));
		
		tv = (TextView)  findViewById(R.id.triginfo_waypoint);
		tv.setText(String.format("TP%04d", c.getLong(c.getColumnIndex(TrigDbHelper.TRIG_ID))));
		
		iv = (ImageView) findViewById(R.id.triginfo_condition_icon);
		iv.setImageResource(R.drawable.c0_unknown + c.getInt(c.getColumnIndex(TrigDbHelper.TRIG_CONDITION)));

		tv = (TextView) findViewById(R.id.triginfo_condition);
		tv.setText(R.string.condition0 + c.getInt(c.getColumnIndex(TrigDbHelper.TRIG_CONDITION)));

		LatLon ll = new LatLon(c.getDouble(c.getColumnIndex(TrigDbHelper.TRIG_LAT)), c.getDouble(c.getColumnIndex(TrigDbHelper.TRIG_LON)));

		tv = (TextView)  findViewById(R.id.triginfo_gridref);
		tv.setText(ll.getOSGB10());
		
		tv = (TextView)  findViewById(R.id.triginfo_wgs84);
		tv.setText(ll.getWGS());
		
		tv = (TextView) findViewById(R.id.triginfo_current);
		tv.setText(R.string.current0 + c.getInt(c.getColumnIndex(TrigDbHelper.TRIG_CURRENT)));

		tv = (TextView) findViewById(R.id.triginfo_historic);
		tv.setText(R.string.historic0 + c.getInt(c.getColumnIndex(TrigDbHelper.TRIG_HISTORIC)));

		tv = (TextView) findViewById(R.id.triginfo_type);
		tv.setText(R.string.physical00 + c.getInt(c.getColumnIndex(TrigDbHelper.TRIG_TYPE)));

		tv = (TextView) findViewById(R.id.triginfo_fb);
		tv.setText(c.getString(c.getColumnIndex(TrigDbHelper.TRIG_FB)));

		c.close();
    }
}