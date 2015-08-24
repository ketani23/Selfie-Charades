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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PickAFriendActivity extends Activity {

    TextView prompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_afriend);

        prompt = (TextView)findViewById(R.id.promptFriend);

        //Set up listview
        //ArrayList<String> playerList =  new ArrayList<String>();
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        final ListView playerLV = (ListView)findViewById(R.id.list_view);
        playerLV.setAdapter(listAdapter);

        //Log.i("tag","here");

        final ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    //Toast.makeText(getApplicationContext(),objects.toString(),Toast.LENGTH_LONG).show();
                    for (int i = 0; i < objects.size(); i++) {
                        ParseUser u = (ParseUser) objects.get(i);
                        String name = u.getString("username");
                        //Log.i("name",name);
                        //String email = u.getString("email").toString();
                        listAdapter.add(name);
                        //listAdapter.add(email);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });

        playerLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((TextView) playerLV.getChildAt(position)).getText().toString();

                if (gameExists(name)) {
                    Log.i("ALERT","Game Exists");
                    Toast.makeText(getApplicationContext(), "You are already playing a game against " + name, Toast.LENGTH_LONG).show();
                } else {
                    //Log.i("selected",name);
                    //Toast.makeText(getApplicationContext(),name,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PickAFriendActivity.this, PickMovieActivity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TURN_NUMBER", 1);

                    createGameParseObject(name);

                    startActivity(intent);
                }
            }
        });
    }

    public boolean gameExists(String opponent){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GAME");
        query.whereEqualTo("TurnActing",ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("TurnGuessing",opponent);
        ParseObject game;
        try {
            game = query.getFirst();
        }catch(Exception e){
            Log.i("Exception: ", e.getLocalizedMessage());
            return false;
        }
        if(game==null){
            return false;
        }
        return true;
    }

    public void createGameParseObject(String opponent){

        ParseObject game = new ParseObject("GAME");
        game.put("Players", Arrays.asList(ParseUser.getCurrentUser().getUsername(),opponent));
        game.put("TurnActing",ParseUser.getCurrentUser().getUsername());
        game.put("TurnGuessing",opponent);
        game.put("TurnNumber",1);
        game.put("Guessed",0);
        game.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.i("ERROR (Game Object): ", e.getLocalizedMessage());
                }
            }
        });

        /*
        //Getting User
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username",opponent);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if(e==null){
                    //success
                    ParseUser user = list.get(0);

                    //getting games array for current user
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    //ArrayList<ParseObject> games = (ArrayList<ParseObject>)currentUser.get("Games");
                    //ArrayList<ParseObject> opp_games = (ArrayList<ParseObject>)user.get("Games");

                    //creating game object
                    ParseObject game = new ParseObject("GAME");
                    game.put("Player1", currentUser);
                    game.put("Player2", user);
                    game.put("CurrentTurnOwner", currentUser);

                    //Log.i("USER", currentUser.getUsername());
                    //Log.i("OPP",user.getUsername());
                    //creating turn object
                    ParseObject turn = new ParseObject("TURN");
                    game.put("CurrentTurnObject", turn);
                    //games.add(game);
                    //turn.put("Games", "games");
                    //turn.saveInBackground();
                    //opp_games.add(game);

                    game.saveInBackground();


                    //Log.i("Size of game array",Integer.toString(games.size()));

                    //adding games arrays back to users
                    //currentUser.put("Games", games);
                    //user.put("Games", opp_games);

                    //currentUser.saveInBackground();
                    //user.saveInBackground();
                    //user.saveInBackground();
                }
            }
        });*/
    }
}
