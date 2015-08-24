package com.example.jenil.parsedemo;

import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    //protected EditText etMessage;
    protected Button btnNewGame;
    protected Button btnLogout;
    protected TextView tvWelcome;
    //protected ParseObject userMessage;
    protected ImageView refreshButton;

    private YourListAdapter mAdapter1;
    private YourListAdapter mAdapter2;
    private ArrayList<YourListObject> yourTurn_list;
    private ArrayList<YourListObject> theirTurn_list;

    private String from;
    private int turn_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        yourTurn_list = new ArrayList<YourListObject>();
        theirTurn_list = new ArrayList<YourListObject>();

        refreshButton = (ImageButton)findViewById(R.id.refreshButton);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            loadLoginView();
        }

        //ArrayList<ParseObject> games = (ArrayList<ParseObject>)currentUser.get("Games");
        //Log.i("SIZE OF GAMES",Integer.toString(games.size()));

        /*
        etMessage =(EditText) findViewById(R.id.etMessage);
        getUserMessage();

        byte[] data = "I want this Job !".getBytes();
        final ParseFile file =new ParseFile("resume.txt",data);
        */


        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        if(currentUser!=null)
            tvWelcome.setText("Welcome, "+currentUser.getUsername()+"!");

        btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                userMessage.put("msg",etMessage.getText().toString());
                userMessage.put("user",ParseUser.getCurrentUser()); // Give Reference of ParseUser
                userMessage.put("resume",file);
                userMessage.saveInBackground(); // Insert or Update Message*/
                Intent intent = new Intent (MainActivity.this,PickAFriendActivity.class);
                startActivity(intent);
            }
        });

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                loadLoginView();
            }
        });

        final ListView yourTurnList = (ListView) findViewById(R.id.test_cont_yourTurnList);
        final ListView theirTurnList =(ListView) findViewById(R.id.test_cont_theirTurnList);

        if(currentUser!=null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("GAME");
            query.whereEqualTo("Players", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e == null) {
                        for (ParseObject p : list) {
                            if (ParseUser.getCurrentUser().getUsername().equalsIgnoreCase(p.get("TurnGuessing").toString())) {
                                from = (String) p.get("TurnActing");
                                turn_number = (int) p.get("TurnNumber");
                                YourListObject temp = new YourListObject(from, turn_number);
                                yourTurn_list.add(temp);

                            } else {
                                //theirTurn_list.add("vs. " + p.get("TurnGuessing").toString());
                                String opp = (String) p.get("TurnGuessing");
                                int turn_num = (int) p.get("TurnNumber");
                                YourListObject temp = new YourListObject(opp, turn_num);
                                theirTurn_list.add(temp);
                            }
                            Log.i("Turn Acting: ", p.get("TurnActing").toString());
                            Log.i("Turn Guessing: ", p.get("TurnGuessing").toString());
                        }
                        mAdapter1 = new YourListAdapter(getBaseContext(), yourTurn_list);
                        yourTurnList.setAdapter(mAdapter1);
                        yourTurnList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                YourListObject object = (YourListObject) parent.getItemAtPosition(position);

                                ParseQuery<ParseObject> query = ParseQuery.getQuery("GAME");
                                query.whereEqualTo("TurnGuessing", ParseUser.getCurrentUser().getUsername());
                                ParseObject game = new ParseObject("GAME");
                                try {
                                    game = query.getFirst();
                                }catch(Exception e){
                                    Log.i("Exception", e.getLocalizedMessage());
                                }
                                if(game!=null) {
                                    int temp = (int) game.get("Guessed");
                                    if (temp == 1) {
                                        Intent intent = new Intent(MainActivity.this, NewTurnActivity.class);
                                        intent.putExtra("OPPONENT", object.getName());
                                        intent.putExtra("TURN_NUMBER", Integer.parseInt(object.getTurnNumber()));
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(MainActivity.this, GuessingTurnActivity.class);
                                        intent.putExtra("From", object.getName());
                                        intent.putExtra("TURN_NUMBER", Integer.parseInt(object.getTurnNumber()));
                                        startActivity(intent);
                                    }
                                }
//                                Intent intent = new Intent(MainActivity.this, GuessingTurnActivity.class);
//                                intent.putExtra("From", object.getName());
//                                intent.putExtra("TURN_NUMBER", Integer.parseInt(object.getTurnNumber()));
//                                startActivity(intent);
                            }
                        });

                        mAdapter2 = new YourListAdapter(getBaseContext(), theirTurn_list);
                        theirTurnList.setAdapter(mAdapter2);
                    } else {
                        Log.i("ERROR: ", "Did not receive a game");
                    }
                }
            });
        }
    }


    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /*
    private void getUserMessage(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        setProgressBarIndeterminateVisibility(true);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> msgList, ParseException e) {
                setProgressBarIndeterminateVisibility(false);
                if (e == null) {
                    for (ParseObject msg : msgList) {
                        // Retrieve Saved Message
                        etMessage.setText(msg.get("msg").toString());
                        userMessage = msg;
                    }
                }
                else{
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }
                if(userMessage==null){
                    // Create New Message
                    userMessage = new ParseObject("Message");
                }

            }
        });
    }*/

}

/*
References:
1. https://parse.com/
2. http://aws.amazon.com/solutions/case-studies/parse/
3. https://parse.com/docs/android_guide
4. http://www.sitepoint.com/creating-cloud-backend-android-app-using-parse/
 */