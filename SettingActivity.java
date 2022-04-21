package com.example.fall.detection;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ToggleButton;

public class SettingActivity extends Activity {

	LocationManager mlocManager;
	Location location;
	Geocoder geocoder;
	private String provider;
	double curLatitude, curLongitude;
	StringBuilder sb;

	// define views
	CheckBox chk_Custom_Message, chk_Use_GPS;
	EditText edtCustomMessage;
	Button btnDone;
	ToggleButton togAlarm_on_off;
	private int alarmFlag = 0, cusMsgFlag = 0, useGPSFlag = 0;
	private int check_alarmFlag, check_cusMsgFlag, check_useGPSFlag;
	private String customTextMessage, sms;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_setting);

		SharedPreferences preferences = getSharedPreferences("alarm", 0);
		check_alarmFlag = preferences.getInt("alarm_on_off", 0);
		check_cusMsgFlag = preferences.getInt("cusMsgFlag", 0);
		check_useGPSFlag = preferences.getInt("useGPSFlag", 0);
		sms = preferences.getString("custom_message", null);

		Log.i("flags", "" + check_alarmFlag + " " + check_cusMsgFlag + " "
				+ check_useGPSFlag + sms);
		// declare id's
		chk_Custom_Message = (CheckBox) findViewById(R.id.setting_checkBox_Custom_Message);
		chk_Use_GPS = (CheckBox) findViewById(R.id.Setting_checkBox_Use_GPS);
		edtCustomMessage = (EditText) findViewById(R.id.setting_editText_Custom_Message);
		btnDone = (Button) findViewById(R.id.setting_button_Done);
		togAlarm_on_off = (ToggleButton) findViewById(R.id.setting_toggleButton_Alarm_on_off);

		edtCustomMessage.setText(sms);

		if (check_alarmFlag == 1) {
			togAlarm_on_off.setChecked(true);
		} else {
			togAlarm_on_off.setChecked(false);
		}

		if (check_cusMsgFlag == 1) {
			chk_Custom_Message.setChecked(true);
		} else {
			chk_Custom_Message.setChecked(false);
		}

		if (check_useGPSFlag == 1) {
			chk_Use_GPS.setChecked(true);
		} else {
			chk_Use_GPS.setChecked(false);
		}

		chk_Custom_Message
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if (isChecked) {

							cusMsgFlag = 1;
						} else {
							cusMsgFlag = 0;
						}
					}
				});
		chk_Use_GPS.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					useGPSFlag = 1;

					myGPSPosition();
				} else {
					useGPSFlag = 0;
				}
			}

		});
		togAlarm_on_off
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							alarmFlag = 1;
						} else {
							alarmFlag = 0;
						}
					}
				});
		btnDone.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				customTextMessage = edtCustomMessage.getText().toString();
				SharedPreferences sharedPreferences1 = getSharedPreferences(
						"alarm", 0);
				SharedPreferences.Editor editor = sharedPreferences1.edit();
				editor.putInt("alarm_on_off", alarmFlag);
				editor.putString("custom_message", customTextMessage + sb);
				editor.putInt("cusMsgFlag", cusMsgFlag);
				editor.putInt("useGPSFlag", useGPSFlag);
				editor.commit();
				startActivity(new Intent(SettingActivity.this,
						MainActivity.class));
				finish();

			}
		});
	}

	void myGPSPosition() {
		try {
			mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Log.i("latitude", "1");
			LocationListener mlocListener = new MyLocationListener();
			Log.i("latitude", "2");
			mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
					0, mlocListener);
			Log.i("latitude", "3");
			Criteria criteria = new Criteria();
			provider = mlocManager.getBestProvider(criteria, false);
			Log.i("latitude", "4");
			location = mlocManager.getLastKnownLocation(provider);
			Log.i("latitude", "5");
			curLatitude = location.getLatitude();
			curLongitude = location.getLongitude();
			Log.i("latitude", "6");
			Log.i("location", "" + curLatitude + curLongitude);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		geocoder = new Geocoder(this, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(curLatitude,
					curLatitude, 1);
			sb = new StringBuilder();
			if (addresses.size() > 0) {
				Address address = addresses.get(0);
				for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
					sb.append(address.getAddressLine(i)).append("\n");
				sb.append(address.getLocality()).append("\n");
				sb.append(address.getPostalCode()).append("\n");
				sb.append(address.getCountryName());
				Log.i("location", sb.toString());

			}

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class MyLocationListener implements LocationListener {

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}

		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			// editLocation.setText("");
			// pb.setVisibility(View.INVISIBLE);
			Toast.makeText(
					getBaseContext(),
					"Location changed: Lat: " + location.getLatitude()
							+ " Lng: " + location.getLongitude(),
					Toast.LENGTH_SHORT).show();
			String longitude = "Longitude: " + location.getLongitude();
			Log.v("latitude", longitude);
			String latitude = "Latitude: " + location.getLatitude();
			Log.v("latitude", latitude);

			/*-------to get City-Name from coordinates -------- */
			String cityName = null;
			Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());

			List<Address> addresses;
			try {
				addresses = gcd.getFromLocation(location.getLatitude(),
						location.getLongitude(), 1);
				if (addresses.size() > 0)
					System.out.println(addresses.get(0).getLocality());
				cityName = addresses.get(0).getLocality();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String s = longitude + "\n" + latitude
					+ "\n\nMy Currrent City is: " + cityName;
			// editLocation.setText(s);
			Log.i("location", s);

		}
	}

}
