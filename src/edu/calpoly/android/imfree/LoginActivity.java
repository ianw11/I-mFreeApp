package edu.calpoly.android.imfree;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class LoginActivity extends Activity implements OnClickListener {
   
   private Button mLoginButton;
   private EditText mUsernameEditText;
   private EditText mPasswordEditText;
   private CheckBox mRememberMeCheckBox;
   private TextView mForgotPasswordTextView;
   private Button mSignUpButton;
   private ProgressBar mProgressBar;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.layout_login);
      
      initLayout();
      initializeListeners();
      
      SharedPreferences prefs = getSharedPreferences("edu.calpoly.android.imfree", Context.MODE_PRIVATE);
      if (!prefs.getString("username", "").equals("")) {
         mUsernameEditText.setText(prefs.getString("username", ""));
      }
      
   }
   
   private void initLayout() {
      
      this.mLoginButton = (Button)findViewById(R.id.loginButton);
      this.mUsernameEditText = (EditText)findViewById(R.id.usernameEditText);
      this.mPasswordEditText = (EditText)findViewById(R.id.passwordEditText);
      this.mRememberMeCheckBox = (CheckBox)findViewById(R.id.rememberMeCheckBox);
      this.mForgotPasswordTextView = (TextView)findViewById(R.id.forgotPasswordTextView);
      this.mSignUpButton = (Button)findViewById(R.id.signUpButton);
      this.mProgressBar = (ProgressBar)findViewById(R.id.loginProgressBar);
      
      mProgressBar.setVisibility(View.GONE);
      mLoginButton.setVisibility(View.VISIBLE);
      
      mRememberMeCheckBox.setChecked(true);
      
      mUsernameEditText.requestFocus();
      
   }
   
   private void initializeListeners() {
      mForgotPasswordTextView.setOnClickListener(this);
      mLoginButton.setOnClickListener(this);
      mSignUpButton.setOnClickListener(this);
      mLoginButton.setClickable(true);
   }

   public void resetViews() {
      mProgressBar.setVisibility(View.GONE);
      mLoginButton.setVisibility(View.VISIBLE);
   }

   @Override
   public void onClick(View v) {
      switch(v.getId()) {
      
         case R.id.loginButton:
            performLogin();
            break;
            
         case R.id.forgotPasswordTextView:
            resetPassword();
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
         mProgressBar.setVisibility(View.VISIBLE);
         mLoginButton.setVisibility(View.INVISIBLE);
         // To reshow the button and rehide the bar, call resetViews() from callback

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

   private void resetPassword() {
	   final String username = mUsernameEditText.getText().toString();
	   AlertDialog.Builder resetDialog = new AlertDialog.Builder(this);

	   resetDialog.setTitle("Reset Password");
	   final EditText emailView = new EditText(this);
	   emailView.setHint(R.string.email);
	   emailView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
	   resetDialog.setView(emailView);
	   
	   resetDialog.setPositiveButton(R.string.reset_confirm, new DialogInterface.OnClickListener() {

		   @Override
		   public void onClick(DialogInterface dialog, int which) {
			   String email = emailView.getText().toString();
			   if (!email.equals("")) {
				   
				   ParseUser.requestPasswordResetInBackground(email,
						   new RequestPasswordResetCallback() {
					   public void done(ParseException e) {
						   if (e == null) {
							   Toast.makeText(LoginActivity.this, R.string.reset_sent, Toast.LENGTH_LONG).show();
						   } else if (e.getCode() == ParseException.EMAIL_NOT_FOUND){
							   Toast.makeText(LoginActivity.this, R.string.reset_emailNotFound, Toast.LENGTH_LONG).show();
						   } else {
							   Toast.makeText(LoginActivity.this, R.string.reset_genericError, Toast.LENGTH_LONG).show();
						   }
					   }
				   });
			   }
		   }
	   });
	   resetDialog.setNegativeButton(R.string.reset_cancel, new DialogInterface.OnClickListener() {
		   
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
			   dialog.dismiss();
		   }
	   });
	   
	   resetDialog.show();
   }
}
