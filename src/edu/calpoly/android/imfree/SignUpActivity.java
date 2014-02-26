package edu.calpoly.android.imfree;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {
   
   private EditText mName;
   private EditText mEmail;
   private EditText mPassword;
   private Button mConfirm;
   
   private String name;
   private String email;
   private String password;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.layout_signup);
      
      initLayout();
      
      mConfirm.setOnClickListener(new OnClickListener() {

         @Override
         public void onClick(View v) {
            name = mName.getText().toString();
            email = mEmail.getText().toString();
            password = mPassword.getText().toString();
            if (!name.equals("") && !email.equals("") && !password.equals("")) {
               ParseUser user = new ParseUser();
               user.setUsername(email);
               user.setPassword(password);
               user.put("FriendlyName", name);
               user.signUpInBackground(new SignUpCallback() {

                  @Override
                  public void done(ParseException e) {
                     
                     if (e == null) {
                        
                        ParseUser.logInInBackground(email, password, new LogInCallback() {
                           public void done(ParseUser user, ParseException e) {
                              if (user != null) {
                                 Intent i = new Intent(SignUpActivity.this, ImFree.class);
                                 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                 i.putExtra("ParseUser", user.getUsername());
                                 i.putExtra("ParseObjectId", user.getObjectId());
                                 startActivity(i);
                                 finish();
                              } else {
                                 Toast.makeText(SignUpActivity.this, "Failed Login", Toast.LENGTH_SHORT).show();
                                 Log.d("ParseException", e.toString());
                              }
                           }
                        });
                        
                     } else {
                        Toast.makeText(SignUpActivity.this, "Sign up failed...", Toast.LENGTH_SHORT).show();
                        Log.e("Sign up Failure", e.toString());
                     }
                     
                  }
                  
               });
            }
         }
         
      });
   }
   
   private void initLayout() {
      
      mName = (EditText)findViewById(R.id.signUpNameEditText);
      mEmail = (EditText)findViewById(R.id.signUpEmailEditText);
      mPassword = (EditText)findViewById(R.id.signUpPasswordEditText);
      mConfirm = (Button)findViewById(R.id.signUpConfirmButton);
      
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.sign_up, menu);
      return true;
   }

}
