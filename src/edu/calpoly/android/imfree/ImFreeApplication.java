package edu.calpoly.android.imfree;

import android.app.Application;

import com.parse.Parse;

public class ImFreeApplication extends Application {
   
   @Override
   public void onCreate() {
      Parse.initialize(this, "IRL0T2KM6IP9GjaXU4ai7NAHLNnqli1iVVaPfV1U", 
            "ADfT5SkIThn2a4uEAg1Vf5ZjiIAEx6S863jgguQn");
   }

}
