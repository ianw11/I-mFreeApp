package edu.calpoly.android.imfree;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity implements OnClickListener {
   
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
   }
   
   private void initLayout() {
      
      mName = (EditText)findViewById(R.id.signUpNameEditText);
      mEmail = (EditText)findViewById(R.id.signUpEmailEditText);
      mPassword = (EditText)findViewById(R.id.signUpPasswordEditText);
      mConfirm = (Button)findViewById(R.id.signUpConfirmButton);
      
      mConfirm.setOnClickListener(this);
   }
   
   @Override
   public void onClick(View v) {
      switch (v.getId()) {
      
      case R.id.signUpConfirmButton:  
         performSignup();
         break;
         
      default:
         break;
      }
   }
   
   private void performSignup() {
      name = mName.getText().toString();
      email = mEmail.getText().toString();
      password = mPassword.getText().toString();
      
      /**
       * TODO: Perform a check on the email to ensure it's formatted correctly
       */
      if (!name.equals("") && !email.equals("") && !password.equals("")) {
         ParseUser user = new ParseUser();
         user.setUsername(email);
         user.setEmail(email);
         user.setPassword(password);
         user.put("FriendlyName", name);
         
         user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
               if (e == null) {
                  // Successful Signup
                  SharedPreferences prefs = SignUpActivity.this.getSharedPreferences("edu.calpoly.android.imfree", Context.MODE_PRIVATE);
                  prefs.edit().putString("username", email).putString("password", password).commit();
                  ParseUser.logInInBackground(email, password, new LoginHelper(SignUpActivity.this));
               } else {
                  // Failed Signup
                  Toast.makeText(SignUpActivity.this, "Sign up failed...", Toast.LENGTH_SHORT).show();
                  Log.e("Sign up Failure", e.toString());
               }
            }
         });
      }
   }

}
