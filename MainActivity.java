package com.example.falldetect;
 
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
 
public class MainActivity extends Activity implements AccelerometerListener,LocationListener{
	
	TextView latlong,timer;

	String get_latlong="";
	
	Button emergency,stop_record,play,signout;
	
	

final static int RQS_1 = 1;

private MediaRecorder myRecorder;
private MediaPlayer myPlayer;
private String outputFile = null;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        emergency =(Button)findViewById(R.id.button1);
        stop_record =(Button)findViewById(R.id.button2);
        play =(Button)findViewById(R.id.button3);
        latlong =(TextView)findViewById(R.id.location);
        
       

		

        
        signout=(Button)findViewById(R.id.button4);
        
        signout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
			      alertDialogBuilder.setMessage("Are you sure,You wanted to logout");
			      
			      alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
			         @Override
			         public void onClick(DialogInterface arg0, int arg1) {
			        	 
			        	 Intent i = new Intent(MainActivity.this,User_Login.class);
			        	 startActivity(i);
			            Toast.makeText(MainActivity.this,"You clicked logout",Toast.LENGTH_LONG).show();
			         }
			      });
			      
			      alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
			         @Override
			         public void onClick(DialogInterface dialog, int which) {
			        	 
			        	 Intent i = new Intent(MainActivity.this,MainActivity.class);
			        	 startActivity(i);
			        	 
			            //finish();
			         }
			      });
			      
			      AlertDialog alertDialog = alertDialogBuilder.create();
			      alertDialog.show();
			}
				
			
		});
        outputFile = Environment.getExternalStorageDirectory().
      		  getAbsolutePath() + "/FalldetectedRecording.mp3";

        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myRecorder.setOutputFile(outputFile);
        
        stop_record.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				stop(v);
			}
		});
        
        play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				play(v);
				
			}
		});

        
       
        emergency.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				start(v);
				
				
			}
		});
        
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Requesting locationmanager for location updates
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 1, 1, this);
         
        // Check onResume Method to start accelerometer listener
    }
    @Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		// To get lattitude value from location object
				double latti = location.getLatitude();
				// To get longitude value from location object
				double longi = location.getLongitude();

				// To hold lattitude and longitude values
				LatLng position = new LatLng(latti, longi);
				
		        
		        
		        
		        Geocoder geocoder;
		        
		        List<Address> addresses;
		        
		        geocoder = new Geocoder(this, Locale.getDefault());
		       
		        
		        //latlong.setText("Longitude:" + location.getLatitude()+"Latitude:"+location.getLongitude());
		        try {
		            Log.e("latitude", "inside latitude--" + latti);
		            addresses = geocoder.getFromLocation(latti, longi,1);
		            
		            
		            	if (addresses != null && addresses.size() > 0) {
		            		
		                String address = addresses.get(0).getAddressLine(0);
		            	String s = addresses.get(0).getSubLocality(); 
		                String city = addresses.get(0).getLocality();
		                String state = addresses.get(0).getAdminArea();
		                String country = addresses.get(0).getCountryName();
		                String postalCode = addresses.get(0).getPostalCode();
		                String knownName = addresses.get(0).getFeatureName(); 
		             

		                

		               latlong.setText(address + " " + city + " " + country+ "" +state +""+postalCode);
		                
		              
		                latlong.setText("Pazhavanthangal");
		                
		               get_latlong = latlong.getText().toString();
		                
		               
		            }
		        
		            
		        }catch (IOException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        }

		       
		        

		       
		     
				

				// Creating object to pass our current location to the map
				MarkerOptions markerOptions = new MarkerOptions();
				// To store current location in the markeroptions object
				markerOptions.position(position);

				/*// Zooming to our current location with zoom level 17.0f
				map.animateCamera(CameraUpdateFactory
						.newLatLngZoom(position, 17f));*/

				// adding markeroptions class object to the map to show our current
				// location in the map with help of default marker
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
    
    public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub
         
    }
 
    public void onShake(float force) {
         
        // Do your stuff here
         
        // Called when Motion Detected
    	//Getting intent and PendingIntent instance  \\get_latlong+
    	

        
       	
     	   startAlert(); 


          Toast.makeText(getBaseContext(), "FALL Detected",Toast.LENGTH_SHORT).show();
          
     	  Intent intent=new Intent(getApplicationContext(),MainActivity.class);  
         	PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);  
         	
         	get_latlong = latlong.getText().toString();
         	  
         	//Get the SmsManager instance and call the sendTextMessage method to send message                 
         	SmsManager sms=SmsManager.getDefault();  
         	sms.sendTextMessage("9790182429", null, "Your Mobile is Fall Detected Location https://www.google.co.in/maps/place/Shrimathi+Devkunvar+Nanalal+Bhatt+Vaishnav+College+For+Women/@12.9560892,80.1438097,17z/data=!4m2!3m1!1s0x0000000000000000:0x0e45b3eb23e88728", pi,null);//change the number   

         	Intent callIntent = new Intent(Intent.ACTION_CALL);  
         	callIntent.setData(Uri.parse("tel:"+"9790182429"));//change the number  
         	startActivity(callIntent);
         	
     	  
     	  }
 		  
    	 
       
       
    
 
   
	@Override
    public void onResume() {
            super.onResume();
            Toast.makeText(getBaseContext(), "onResume Accelerometer Started", 
                    Toast.LENGTH_SHORT).show();
             
            //Check device supported Accelerometer senssor or not
            if (AccelerometerManager.isSupported(this)) {
                 
                //Start Accelerometer Listening
                AccelerometerManager.startListening(this);
            }
    }
     
    @Override
    public void onStop() {
            super.onStop();
             
            //Check device supported Accelerometer senssor or not
            if (AccelerometerManager.isListening()) {
                 
                //Start Accelerometer Listening
                AccelerometerManager.stopListening();
                 
                Toast.makeText(getBaseContext(), "onStop Accelerometer Stoped", 
                         Toast.LENGTH_SHORT).show();
            }
            
    }
     
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Sensor", "Service  distroy");
         
        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {
             
            //Start Accelerometer Listening
            AccelerometerManager.stopListening();
             
            Toast.makeText(getBaseContext(), "onDestroy Accelerometer Stoped", 
                   Toast.LENGTH_SHORT).show();
        }
             
    }
 
    public void startAlert() {  
        /* EditText text = (EditText) findViewById(R.id.time);  
         int i = Integer.parseInt(text.getText().toString());  */
 	
 		int i = 1;
         
 		Intent intent = new Intent(MainActivity.this, MyBroadcastReceiver.class);  
         PendingIntent pendingIntent = PendingIntent.getBroadcast(  
                                       getApplicationContext(), RQS_1, intent, 0);  
         AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);  
         alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()  
                                       + (i * 100), pendingIntent);  
         Toast.makeText(this, "Alarm set in " + i + " seconds",Toast.LENGTH_LONG).show();  
     }  
    
    public void start(View view){
 	   try {
           myRecorder.prepare();
           myRecorder.start();
        } catch (IllegalStateException e) {
           // start:it is called before prepare()
     	  // prepare: it is called after start() or before setOutputFormat() 
           e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
         }
 	   
        emergency.setEnabled(false);
        stop_record.setEnabled(true);
        
        Toast.makeText(getApplicationContext(), "Start recording...", 
     		   Toast.LENGTH_SHORT).show();
    }

    public void stop(View view){
 	   try {
 	      myRecorder.stop();
 	      myRecorder.release();
 	      myRecorder  = null;
 	      
 	      stop_record.setEnabled(false);
 	      play.setEnabled(true);
 	      
 	      Toast.makeText(getApplicationContext(), "Stop recording...",
 	    		  Toast.LENGTH_SHORT).show();
 	   } catch (IllegalStateException e) { 
 			//  it is called before start()
 			e.printStackTrace();
 	   } catch (RuntimeException e) {
 			// no valid audio/video data has been received
 			e.printStackTrace();
 	   }
    }
   
    public void play(View view) {
 	   try{
 		   myPlayer = new MediaPlayer();
 		   myPlayer.setDataSource(outputFile);
 		   myPlayer.prepare();
 		   myPlayer.start();
 		   
 		   play.setEnabled(false);
 		   stop_record.setEnabled(true);
 		   
 		   
 		   Toast.makeText(getApplicationContext(), "Start play the recording...",
 				   Toast.LENGTH_SHORT).show();
 	   } catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
    }
    

	
    public void onBackPressed() 
	{
		Intent intent = new Intent(MainActivity.this,MainActivity.class);
		startActivity(intent);
	}
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
            switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    //TODO
                	   
               	  Intent intent=new Intent(getApplicationContext(),MainActivity.class);  
                   	PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);  
                   	
                   	get_latlong = latlong.getText().toString();
                   	  
                   	//Get the SmsManager instance and call the sendTextMessage method to send message                 
                   	SmsManager sms=SmsManager.getDefault();  
                   	sms.sendTextMessage("9790182429", null, get_latlong+"Your Mobile is Fall Detected Location https://www.google.co.in/maps/place/Shrimathi+Devkunvar+Nanalal+Bhatt+Vaishnav+College+For+Women/@12.9560892,80.1438097,17z/data=!4m2!3m1!1s0x0000000000000000:0x0e45b3eb23e88728", pi,null);//change the number   

                   	Intent callIntent = new Intent(Intent.ACTION_CALL);  
                   	callIntent.setData(Uri.parse("tel:"+"9790182429"));//change the number  
                   	startActivity(callIntent);
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    //TODO
                	
                	   
               	  Intent intent=new Intent(getApplicationContext(),MainActivity.class);  
                   	PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);  
                   	
                   	get_latlong = latlong.getText().toString();
                   	  
                   	//Get the SmsManager instance and call the sendTextMessage method to send message                 
                   	SmsManager sms=SmsManager.getDefault();  
                   	sms.sendTextMessage("9790182429", null, get_latlong+" Fall is Detected and the location is chrompet ", pi,null);//change the number   

                   	Intent callIntent = new Intent(Intent.ACTION_CALL);  
                   	callIntent.setData(Uri.parse("tel:"+"9790182429"));//change the number  
                   	startActivity(callIntent);

                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
            }
        }
    
   
}
