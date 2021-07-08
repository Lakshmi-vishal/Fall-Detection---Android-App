package com.example.fall.detection;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

	// define views
	EditText edtPhoneNo, edtName, edtAge, edtHeight, edtWeight;
	RadioGroup sexRadioGroup;
	Button btn_Start_Stop;

	private SensorManager sensorManager;
	private boolean fall_detection = false;
	private boolean man_fall = false;
	private long lastUpdate;
	private int flag = 1;

	String careMobile, name, age, height, weight, sex = "Male";
	String strCareMobile, strName, strAge, strHeight, strWeight,
			strSex = "Male";
	int intSex;
	int selectedSex;

	// setting
	private int alaram_on_off = 0;
	private int check_alarmFlag, check_cusMsgFlag, check_useGPSFlag;
	private String sms;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// declare Ids
		edtAge = (EditText) findViewById(R.id.main_editText_age);
		edtHeight = (EditText) findViewById(R.id.main_editText_height);
		edtName = (EditText) findViewById(R.id.main_editText_Name);
		edtPhoneNo = (EditText) findViewById(R.id.main_editText_Pnone_No);
		edtWeight = (EditText) findViewById(R.id.main_editText_weight);

		sexRadioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		btn_Start_Stop = (Button) findViewById(R.id.main_button_start_stop);

		SharedPreferences preferences2 = getSharedPreferences("alarm", 0);
		alaram_on_off = preferences2.getInt("alarm_on_off", 0);
		check_cusMsgFlag = preferences2.getInt("cusMsgFlag", 0);
		check_useGPSFlag = preferences2.getInt("useGPSFlag", 0);
		sms = preferences2.getString("custom_message", null);

		SharedPreferences preferences = getSharedPreferences("pref_name", 0);
		strCareMobile = preferences.getString("key_care_mobile", null);
		strAge = preferences.getString("key_age", null);
		strHeight = preferences.getString("key_height", null);
		strWeight = preferences.getString("key_weight", null);
		strName = preferences.getString("key_name", null);
		intSex = preferences.getInt("key_sex", 0);
		Log.i("value", strAge + " " + strCareMobile + " " + strHeight + " "
				+ strName + " " + strWeight);
		// strSex = preferences.getString("key_sex_name", null);
		// Log.i("sex", strSex);

		if (strSex.equalsIgnoreCase("Male")) {

			RadioButton raoMale = (RadioButton) findViewById(R.id.main_radio_Sex_Male);
			raoMale.setChecked(true);

		} else if (strSex.equalsIgnoreCase("Female")) {
			RadioButton raoFeMale = (RadioButton) findViewById(R.id.main_radio_Sex_Female);
			raoFeMale.setChecked(true);
		}
		edtAge.setText(strAge);
		edtHeight.setText(strHeight);
		edtName.setText(strName);
		edtPhoneNo.setText(strCareMobile);
		edtWeight.setText(strWeight);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		lastUpdate = System.currentTimeMillis();
		sexRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				selectedSex = group.getCheckedRadioButtonId();

				RadioButton radioButton = (RadioButton) findViewById(selectedSex);
				sex = radioButton.getText().toString();
				Log.i("sex", sex);
			}
		});

		btn_Start_Stop.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				careMobile = edtPhoneNo.getText().toString();
				name = edtName.getText().toString();
				age = edtAge.getText().toString();
				height = edtHeight.getText().toString();
				weight = edtWeight.getText().toString();

				try {
					SharedPreferences sharedPreferences = getSharedPreferences(
							"pref_name", 0);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("key_care_mobile", careMobile);
					editor.putString("key_name", name);
					editor.putString("key_age", age);
					editor.putString("key_height", height);
					editor.putString("key_weight", weight);
					editor.putInt("key_sex", selectedSex);
					editor.putString("key_sex_name", sex);

					editor.commit();

					if (flag == 1) {
						man_fall = true;
						flag = 2;
						btn_Start_Stop
								.setBackgroundResource(R.drawable.btn_stop);
					} else {
						man_fall = false;
						flag = 1;
						btn_Start_Stop
								.setBackgroundResource(R.drawable.btn_start2);
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem menuItem) {

		switch (menuItem.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(MainActivity.this, SettingActivity.class));
			finish();
			break;
		}
		return true;

	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			if (man_fall) {
				getAccelerometer(event);
			}
		}
	}

	private void getAccelerometer(SensorEvent event) {
		// TODO Auto-generated method stub
		float[] values = event.values;
		// Movement
		float x = values[0];
		float y = values[1];
		float z = values[2];

		float accelationSquareRoot = (x * x + y * y + z * z)
				/ (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
		long actualTime = System.currentTimeMillis();
		if (accelationSquareRoot >= 2) //
		{
			if (actualTime - lastUpdate < 200) {
				return;
			}
			lastUpdate = actualTime;
			Toast.makeText(this, "Device was shuffed", Toast.LENGTH_SHORT)
					.show();
			if (fall_detection) {
				// view.setBackgroundColor(Color.GREEN);
				if (alaram_on_off == 1) {
					startAlarm();

					if (check_cusMsgFlag == 1) {
						sendSMS(strCareMobile, sms);
					} else {
						sendSMS(strCareMobile, " I am Falling ");
					}
				}

			} else {
				// view.setBackgroundColor(Color.RED);
			}
			fall_detection = !fall_detection;
		}
	}

	private void startAlarm() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 1);

		// Create a new PendingIntent and add it to the AlarmManager

		Intent intent = new Intent(this, AlarmReceiverActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 12345,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

	}

	@Override
	protected void onResume() {
		super.onResume();
		// register this class as a listener for the orientation and
		// accelerometer sensors
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		// unregister listener
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	private void sendSMS(String phoneNumber, String message) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}
}
