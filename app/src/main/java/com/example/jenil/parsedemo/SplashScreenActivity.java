package com.example.jenil.parsedemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by adas on 5/10/2015.
 */
public class SplashScreenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(isNetworkAvailable()){
            Toast.makeText(getApplicationContext(), "Network Available", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Network Not Available", Toast.LENGTH_LONG).show();

        }

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(1123);

                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    // Intent openMainActivity= new Intent("com.nyu.cs9033.eta.controllers.MAINACTIVITY");
                    // startActivity(openMainActivity);

                    Intent newIntent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(newIntent);


                    /*Intent viewTripIntent = new Intent(this,TripHistoryActivity.class);

                    startActivity(viewTripIntent);*/
                }
            }
        };
        timer.start();


    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
