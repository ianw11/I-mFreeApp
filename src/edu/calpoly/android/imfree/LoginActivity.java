package edu.calpoly.android.imfree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity implements OnClickListener {
   
   private Button mLoginButton;
   private EditText mUsernameEditText;
   private EditText mPasswordEditText;
   private CheckBox mRememberMeCheckBox;
   private TextView mForgotPasswordTextView;
   private TextView mFacebookSignInTextView;
   private Button mSignUpButton;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      setContentView(R.layout.layout_login);
      
      initLayout();
      initializeListeners();
      
      SharedPreferences prefs = getSharedPreferences("edu.calpoly.android.imfree", Context.MODE_PRIVATE);
      if (!prefs.getString("username", "").equals("")) {
         mUsernameEditText.setText(prefs.getString("username", ""));
         mPasswordEditText.setText(prefs.getString("password", ""));
         mRememberMeCheckBox.setChecked(true);
      }
      
      if (!getIntent().hasExtra("intent")) {
      
         Parse.initialize(this, "IRL0T2KM6IP9GjaXU4ai7NAHLNnqli1iVVaPfV1U", 
               "ADfT5SkIThn2a4uEAg1Vf5ZjiIAEx6S863jgguQn");
         ParseUser.enableAutomaticUser();
         ParseACL defaultACL = new ParseACL();
         
         if (!prefs.getString("username", "").equals("")) {
            ParseUser.logInInBackground(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString(), new LogInCallback() {
               public void done(ParseUser user, ParseException e) {
                  if (user != null) {
                     Intent i = new Intent(LoginActivity.this, ImFree.class);
                     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     i.putExtra("ParseUser", user.getUsername());
                     i.putExtra("ParseObjectId", user.getObjectId());
                     startActivity(i);
                     finish();
                  } else {
                     Toast.makeText(LoginActivity.this, "Failed Login", Toast.LENGTH_SHORT).show();
                     Log.d("ParseException", e.toString());
                  }
               }
            });
         }
      }
      
   }
   
   private void initLayout() {
      
      this.mLoginButton = (Button)findViewById(R.id.loginButton);
      this.mUsernameEditText = (EditText)findViewById(R.id.usernameEditText);
      this.mPasswordEditText = (EditText)findViewById(R.id.passwordEditText);
      this.mRememberMeCheckBox = (CheckBox)findViewById(R.id.rememberMeCheckBox);
      this.mForgotPasswordTextView = (TextView)findViewById(R.id.forgotPasswordTextView);
      this.mFacebookSignInTextView = (TextView)findViewById(R.id.facebookSignUpTextView);
      this.mSignUpButton = (Button)findViewById(R.id.signUpButton);
      
   }
   
   private void initializeListeners() {
      mForgotPasswordTextView.setOnClickListener(this);
      mLoginButton.setOnClickListener(this);
      mFacebookSignInTextView.setOnClickListener(this);
      mSignUpButton.setOnClickListener(this);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.login, menu);
      return true;
   }

   @Override
   public void onClick(View v) {
      switch(v.getId()){
         case R.id.loginButton:
            String username = mUsernameEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            if (!username.equals("") && !password.equals("")) {
               
               if (mRememberMeCheckBox.isChecked()) {
                  SharedPreferences prefs = this.getSharedPreferences("edu.calpoly.android.imfree", Context.MODE_PRIVATE);
                  prefs.edit().putString("username", username).putString("password", password).commit();
               } else {
                  SharedPreferences prefs = this.getSharedPreferences("edu.calpoly.android.imfree", Context.MODE_PRIVATE);
                  prefs.edit().putString("username", "").putString("password", "").commit();
               }
               
               ParseUser.logInInBackground(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString(), new LogInCallback() {
                  public void done(ParseUser user, ParseException e) {
                     if (user != null) {
                        Intent i = new Intent(LoginActivity.this, ImFree.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("ParseUser", user.getUsername());
                        i.putExtra("ParseObjectId", user.getObjectId());
                        startActivity(i);
                        finish();
                     } else {
                        Toast.makeText(LoginActivity.this, "Failed Login", Toast.LENGTH_SHORT).show();
                        Log.d("ParseException", e.toString());
                     }
                  }
               });
            }
            
            break;
         case R.id.forgotPasswordTextView:
            Toast.makeText(this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show();
            break;
         case R.id.facebookSignUpTextView:
            Toast.makeText(this, "Facebook Clicked", Toast.LENGTH_SHORT).show();
            break;
         case R.id.signUpButton:
            Intent i = new Intent(this, SignUpActivity.class);
            startActivity(i);
            break;
         default:
            break;
      }
      
   }

}
