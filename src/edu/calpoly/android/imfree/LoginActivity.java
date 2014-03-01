package edu.calpoly.android.imfree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

public class LoginActivity extends Activity implements OnClickListener {
   
   private Button mLoginButton;
   private EditText mUsernameEditText;
   private EditText mPasswordEditText;
   private CheckBox mRememberMeCheckBox;
   private TextView mForgotPasswordTextView;
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
         //mPasswordEditText.setText(prefs.getString("password", ""));
      }
      
   }
   
   private void initLayout() {
      
      this.mLoginButton = (Button)findViewById(R.id.loginButton);
      this.mUsernameEditText = (EditText)findViewById(R.id.usernameEditText);
      this.mPasswordEditText = (EditText)findViewById(R.id.passwordEditText);
      this.mRememberMeCheckBox = (CheckBox)findViewById(R.id.rememberMeCheckBox);
      this.mForgotPasswordTextView = (TextView)findViewById(R.id.forgotPasswordTextView);
      this.mSignUpButton = (Button)findViewById(R.id.signUpButton);
      
      mRememberMeCheckBox.setChecked(true);
      
   }
   
   private void initializeListeners() {
      mForgotPasswordTextView.setOnClickListener(this);
      mLoginButton.setOnClickListener(this);
      mSignUpButton.setOnClickListener(this);
   }


   @Override
   public void onClick(View v) {
      switch(v.getId()) {
      
         case R.id.loginButton:
            performLogin();
            break;
            
         case R.id.forgotPasswordTextView:
            Toast.makeText(this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show();
            break;
            
         case R.id.signUpButton:
            Intent i = new Intent(this, SignUpActivity.class);
            startActivity(i);
            break;
            
         default:
            break;
      }
      
   }
   
   private void performLogin() {
      final String username = mUsernameEditText.getText().toString();
      final String password = mPasswordEditText.getText().toString();
      if (!username.equals("") && !password.equals("")) {

         // Save login data for future use, if desired
         if (mRememberMeCheckBox.isChecked()) {
            SharedPreferences prefs = this.getSharedPreferences("edu.calpoly.android.imfree", Context.MODE_PRIVATE);
            prefs.edit().putString("username", username).putString("password", password).commit();
         } else {
            SharedPreferences prefs = this.getSharedPreferences("edu.calpoly.android.imfree", Context.MODE_PRIVATE);
            prefs.edit().putString("username", "").putString("password", "").commit();
         }

         ParseUser.logInInBackground(username, password, new LoginHelper(LoginActivity.this));
      }
   }

}
