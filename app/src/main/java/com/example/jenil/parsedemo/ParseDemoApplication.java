package com.example.jenil.parsedemo;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Nikul on 3/31/15.
 */

public class ParseDemoApplication  extends Application {

    static final String TAG = "MyApp";
    static final String APP_KEY = "00z7ZO7Svd18FpRCVooAVKyR5PTRDhK5z9utvYgt";
    static final String CLIENT_KEY = "NzZSN9LSn2RJIAQmYNZDnw23eQ1ciDX2xjBaAhf9";
    @Override
    public void onCreate() {
        super.onCreate();

        //Parse.initialize(this,APP_KEY,CLIENT_KEY);
        Parse.initialize(this, "aIN3xNI4GBn2Q3csZn4ishryw47IqSs4BGgAypHJ", "JbURifqwQLin0JhtD9FfCGYSypbHPrA7Etsg623r");




    }
}
/*
References:
1. https://parse.com/
2. http://aws.amazon.com/solutions/case-studies/parse/
3. https://parse.com/docs/android_guide
4. http://www.sitepoint.com/creating-cloud-backend-android-app-using-parse/
 */