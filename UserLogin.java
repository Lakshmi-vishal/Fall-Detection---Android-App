package com.example.falldetect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class User_Login extends Activity {
	
	
	EditText name,pass;
	
	
	String get_name,get_pass;
	
	
	Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        setContentView(R.layout.user_login);
       
       
       name=(EditText)findViewById(R.id.editText1);
       pass=(EditText)findViewById(R.id.editText2);
       signin=(Button)findViewById(R.id.button1);
     
      
       
       signin.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			get_name=name.getText().toString();
			get_pass=pass.getText().toString();
			
			if (containsWhitespace(get_name) || get_name.length() == 0) {
				
				Toast.makeText(getApplicationContext(), "Please Enter the UserName", Toast.LENGTH_LONG).show();
			}

			else if(containsWhitespace(get_pass) || get_pass.length() == 0)
			{
				
				Toast.makeText(getApplicationContext(), "Please Enter the Password", Toast.LENGTH_LONG).show();

			}
			else {
				
				if(get_name.equals("Admin") && get_pass.equals("Admin"))
				{
					Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();

					startActivity(new Intent(User_Login.this,MainActivity.class));
					finish();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "do not match username and password", Toast.LENGTH_LONG).show();
				}
			}

		}
		
		
	});
    
       
       
      
    }









	protected void Connectvitymessage1(String tAG_ERR_PASSWORD) {
		// TODO Auto-generated method stub
		
	}









	protected void Connectvitymessage(String tAG_ERR_USERNAME) {
		// TODO Auto-generated method stub
		
	}









	protected boolean containsWhitespace(String get_name2) {
		// TODO Auto-generated method stub
		return false;
	}









	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
