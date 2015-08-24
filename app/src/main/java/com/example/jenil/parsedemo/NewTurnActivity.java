package com.example.jenil.parsedemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;


public class NewTurnActivity extends Activity {

    private ViewSwitcher vs;
    private TextView caption;
    private TextView old_turn;
    private TextView new_turn;

    private Button play_button;

    private int old_turn_num;
    private String opponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_turn);

        vs = (ViewSwitcher)findViewById(R.id.viewSwitcher);
        caption = (TextView)findViewById(R.id.caption);
        old_turn = (TextView)findViewById(R.id.captionNumberOld);
        new_turn = (TextView)findViewById(R.id.captionNumberNew);
        play_button = (Button)findViewById(R.id.play_button);

        Intent intent = getIntent();
        opponent = intent.getStringExtra("OPPONENT");
        old_turn_num = intent.getIntExtra("TURN_NUMBER",0);
        if(old_turn_num == 0){
            Log.i("ERROR: ","No turn number received");
        }
        else{

            old_turn.setText(Integer.toString(old_turn_num));
            new_turn.setText(Integer.toString(old_turn_num + 1));

            Animation animIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
            Animation animOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);
            // set them on the ViewSwitcher
            vs.setInAnimation(animIn);
            vs.setOutAnimation(animOut);

            Thread time = new Thread(){
                public void run() {
                    try {
                        sleep(1123);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            time.start();

            vs.showNext();

            play_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNextTurn();
                }
            });
        }

    }

    public void startNextTurn(){
        createGameObject();
        int new_turn_num = old_turn_num + 1;
        Intent intent = new Intent(NewTurnActivity.this, PickMovieActivity.class);
        intent.putExtra("USERNAME", opponent);
        intent.putExtra("TURN_NUMBER", new_turn_num);

        startActivity(intent);
    }

    public void createGameObject(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GAME");
        query.whereEqualTo("TurnGuessing", ParseUser.getCurrentUser().getUsername());
        ParseObject game = new ParseObject("GAME");
        try {
            game = query.getFirst();
        }catch(Exception e){
            Log.i("Exception", e.getLocalizedMessage());
        }
        game.put("TurnActing", ParseUser.getCurrentUser().getUsername());
        game.put("TurnGuessing", opponent);
        final int new_turn_num = old_turn_num + 1;
        game.put("TurnNumber", new_turn_num);
        game.put("Guessed",0);
        try {
            game.save();
        }catch(Exception e){
            Log.i("Exception: ", e.getLocalizedMessage());
        }
//        game.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e != null) {
//                    Log.i("ERROR (Game Object): ", e.getLocalizedMessage());
//                }
//            }
//        });

        /*
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject game, ParseException e) {
                //ParseObject game = list.get(0);
                game.put("TurnActing", ParseUser.getCurrentUser().getUsername());
                game.put("TurnGuessing", opponent);
                final int new_turn_num = old_turn_num + 1;
                game.put("TurnNumber", new_turn_num);
                game.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.i("ERROR (Game Object): ", e.getLocalizedMessage());
                        } else {
                            Intent intent = new Intent(NewTurnActivity.this, PickMovieActivity.class);
                            intent.putExtra("USERNAME", opponent);
                            intent.putExtra("TURN_NUMBER", new_turn_num);

                            startActivity(intent);
                        }
                    }
                });
            }
        });*/
    }
}
