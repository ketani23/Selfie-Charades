package com.example.jenil.parsedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Random;


public class PickMovieActivity extends Activity {

    protected TextView msg;
    protected TextView prompt;

    private String opponent;
    private int turn_number;


    private String[] easyMovies = {"Star Wars", "American Pie", "The Hunger Games", "Beauty and the Beast", "21 Jump Street"};
    private String[] mediumMovies = {"Slumdog Millionaire", "The Hangover", "Lion King", "Psycho", "The Dark Knight"};
    private String[] hardMovies = {"Inception", "Interstellar", "The Fountainhead", "Pi" , "The Matrix"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_movie);

        Intent intent = getIntent();

        opponent = intent.getStringExtra("USERNAME");
        turn_number = intent.getIntExtra("TURN_NUMBER",0);

        msg = (TextView)findViewById(R.id.Msg);
        msg.setText(" You are playing a new game against " + opponent);

        prompt = (TextView)findViewById(R.id.prompt);

        final ArrayAdapter<String> movieAdapter = new ArrayAdapter<String>(this, R.layout.movie_row_layout);
        final ListView movieList = (ListView)findViewById(R.id.listMovie);

        movieList.setAdapter(movieAdapter);

        movieAdapter.add(easyMovies[new Random().nextInt(easyMovies.length)]);
        movieAdapter.add(mediumMovies[new Random().nextInt(mediumMovies.length)]);
        movieAdapter.add(hardMovies[new Random().nextInt(hardMovies.length)]);

        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent (PickMovieActivity.this, ActingTurnActivity.class);
                intent1.putExtra("OPPONENT",opponent);
                intent1.putExtra("MOVIE",(String)movieList.getItemAtPosition(position));
                intent1.putExtra("TURN_NUMBER",turn_number);
                startActivity(intent1);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pick_movie, menu);
        return true;
    }

    @Override
    public void onBackPressed(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GAME");
        query.whereEqualTo("TurnActing", ParseUser.getCurrentUser().getUsername());
        ParseObject game = new ParseObject("GAME");
        try {
            game = query.getFirst();
        }catch(Exception e){
            Log.i("Exception", e.getLocalizedMessage());
        }
        try {
            game.delete();
        }catch(Exception e){
            Log.i("Exception: ", e.getLocalizedMessage());
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
