package com.example.falldetect;


import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;

import android.view.Window;


public class Activity_Splash extends Activity
 {

	private static int splashTime = 10000;

	
	@Override

	protected void onCreate(Bundle savedInstanceState)
 {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	
	setContentView(R.layout.activity_splash);
		


		Thread splashThread = new Thread() 
{
			public void run() {
				
                       try {

					
                     synchronized (this) {
						
                      wait(splashTime);
		
			}

		
		} catch (InterruptedException e)
 {
					// TODO Auto-generated catch block

					e.printStackTrace();
			
	}  finally {
			
		startActivity(new Intent(Activity_Splash.this,user_Login.class));
	

				finish();
				}

		
	}
		};
		splashThread.start();
	}

	
@Override

	public boolean onCreateOptionsMenu(Menu menu)
 {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.main, menu);
	
	return true;

	}
}
