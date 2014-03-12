package edu.calpoly.android.imfree;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity implements OnClickListener {
   
   private EditText mFirstName;
   private EditText mLastName;
   private EditText mEmail;
   private EditText mPassword;
   private Button mConfirm;
   
   private String firstName;
   private String lastName;
   private String email;
   private String password;
   
   public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
         "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
         "\\@" +
         "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
         "(" +
         "\\." +
         "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
         ")+"
     );

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.layout_signup);
      
      initLayout();
   }
   
   private void initLayout() {
      
      mFirstName = (EditText)findViewById(R.id.signUpFirstNameEditText);
      mLastName = (EditText)findViewById(R.id.signUpLastNameEditText);
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
      firstName = mFirstName.getText().toString(); 
      lastName = mLastName.getText().toString();
      email = mEmail.getText().toString();
      password = mPassword.getText().toString();
      
      if (!firstName.equals("") && !lastName.equals("") && 
    		  !email.equals("") && !password.equals("")) {
         
         if (!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            Toast.makeText(this, R.string.signUp_enterValidEmail, Toast.LENGTH_SHORT).show();
            return;
         }
         
         ParseUser user = new ParseUser();
         user.setUsername(email);
         user.setEmail(email);
         user.setPassword(password);
         user.put("FirstName", firstName);
         user.put("LastName", lastName);
         
         user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
               if (e == null) {
                  // Successful Signup
                  
                  SharedPreferences prefs = SignUpActivity.this.getSharedPreferences("edu.calpoly.android.imfree", Context.MODE_PRIVATE);
                  prefs.edit().putString("username", email).putString("password", password).commit();
                  ParseUser.logInInBackground(email, password, new LoginHelper(SignUpActivity.this, false, true));
               } else {
                  // Failed Signup
                  Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
               }
            }
         });
      }
      else {
    	  Toast.makeText(this, R.string.signUp_notAllFieldsFilledIn, Toast.LENGTH_SHORT).show();
      }
   }
}
