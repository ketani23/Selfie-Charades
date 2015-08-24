package com.example.jenil.parsedemo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class GuessingTurnActivity extends Activity {

    private static final int MAX_PICTURES = 5;
    private static final int PAGE_MARGIN = 15;

    private ViewPager vPager;
    private MyPagerAdapter pagerAdapter;

    private PagerContainer container;

    private String from;
    private int turn_number;
    private String movie;

    private Button giveUp;

    private static final String[] whitelist = {"the", "of", "and"};
    private int count_words_white=0;

    private int check_count=0;
    private int et_count=0;
    private int correct_count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guessing_turn);

        giveUp = (Button)findViewById(R.id.giveUp);

        movie="n";

        Intent intent = getIntent();
        from = intent.getStringExtra("From");
        turn_number = intent.getIntExtra("TURN_NUMBER", 0);

        container = (PagerContainer) findViewById(R.id.pager_container_guessing);
        vPager = container.getViewPager();
        pagerAdapter = new MyPagerAdapter();
        vPager.setAdapter(pagerAdapter);
        vPager.setOffscreenPageLimit(pagerAdapter.getCount());
        vPager.setPageMargin(PAGE_MARGIN);
        vPager.setClipChildren(false);

        loadPics();

        giveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(GuessingTurnActivity.this)
                        .setTitle("Give up?")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yup!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new AlertDialog.Builder(GuessingTurnActivity.this)
                                        .setTitle("Oh well")
                                        .setMessage("The movie was " + movie)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                gaveUp();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_menu_info_details)
                                        .show();
                            }
                        })
                        .setNegativeButton("Nope!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //nothing to do
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_info_details)
                        .show();
            }
        });
    }

    public void gaveUp(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GAME");
        query.whereEqualTo("TurnGuessing", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("TurnActing",from);
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

        Intent mainIntent = new Intent(GuessingTurnActivity.this,MainActivity.class);
        startActivity(mainIntent);
    }

    public void loadPics(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("IMAGE");
        query.whereEqualTo("For", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("From", from);
        query.whereEqualTo("TurnNumber", turn_number);
        query.orderByDescending("updatedAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject p : list) {
                        ParseFile pic = (ParseFile) p.get("photo");
                        movie = p.getString("Movie");
                        pic.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                ImageView img = new ImageView(GuessingTurnActivity.this);
                                img.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                addView(img);

                            }
                        });
                    }
                    parseMovieName();
                } else {
                    Log.i(" ERROR: ", e.getLocalizedMessage());
                }
            }
        });
    }

    public void parseMovieName(){
        LinearLayout myLayout = (LinearLayout)findViewById(R.id.guessing_layout);
        myLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        final String[] parts = movie.split(" ");
        //Log.i("Number of words is: ",Integer.toString(parts.length));

        int[] movie_word_list = new int[parts.length];
        //Log.i("Length: ", Integer.toString(movie_word_list.length));

        for(int i=0;i<parts.length;i++){
            for(String word:whitelist){
                if(parts[i].equalsIgnoreCase(word)) {
                    movie_word_list[i] = 1;
                    count_words_white++;
                }
            }
            if(movie_word_list[i]!=1){
                movie_word_list[i]=0;
                et_count++;
            }
        }

        final TextView[] myTextViews = new TextView[parts.length];
        final EditText[] myEditTextViews = new EditText[parts.length];

        for(;check_count<movie_word_list.length;check_count++){
            if(movie_word_list[check_count]==1){
                final TextView tv = new TextView(this);
                tv.setText(parts[check_count]);
                tv.setTextSize(20f);
                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                tv.setPadding(20, 20, 20, 20);
                myLayout.addView(tv);
                myTextViews[check_count] = tv;
            }else{
                final ViewSwitcher vs= new ViewSwitcher(this);
                // load the two animations
                Animation animIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                Animation animOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                // set them on the ViewSwitcher
                vs.setInAnimation(animIn);
                vs.setOutAnimation(animOut);

                final TextView tv = new TextView(this);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                tv.setTextColor(Color.parseColor("#00CC66"));
                tv.setText(parts[check_count]);
                tv.setTextSize(20f);
                final EditText et = new EditText((this));
                et.setLayoutParams(new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.WRAP_CONTENT));
                et.setPadding(20, 20, 20, 20);
                et.setTextSize(20f);
                et.addTextChangedListener(new TextWatcher() {
                    private boolean done = false;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //nothing to do
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //nothing to do
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String temp = et.getText().toString();
                        temp = temp.trim();

                        int i;
                        for (i = 0; i < parts.length; i++) {
                            if (myEditTextViews[i] == et)
                                break;
                        }
                        if (temp.equalsIgnoreCase(parts[i]) && !done) {
                            correct_count++;
                            done = true;
                            et.setFocusableInTouchMode(false);
                            vs.showNext();
                            if (correct_count == et_count){
                                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                mgr.hideSoftInputFromWindow(et.getWindowToken(),0);
                                new AlertDialog.Builder(GuessingTurnActivity.this)
                                        .setTitle("SUCCESS!")
                                        .setMessage("YOU GOT IT!")
                                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                                endTurn();
                                            }
                                        })
                                        .setIcon(android.R.drawable.checkbox_on_background)
                                        .show();
                            }
                            Log.i("something", " happening");
                        } else {
                            et.setTextColor(Color.parseColor("#000000"));
                        }
                        //Log.i("Word is: ", parts[check_count]);
                    }
                });
                vs.addView(et);
                vs.addView(tv);
                myLayout.addView(vs);
                myEditTextViews[check_count]=et;
            }
        }
    }

    public void endTurn(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GAME");
        query.whereEqualTo("TurnGuessing", ParseUser.getCurrentUser().getUsername());
        ParseObject game = new ParseObject("GAME");
        try {
            game = query.getFirst();
        }catch(Exception e){
            Log.i("Exception", e.getLocalizedMessage());
        }
        game.put("TurnActing", from);
        game.put("TurnGuessing", ParseUser.getCurrentUser().getUsername());
        game.put("TurnNumber", turn_number);
        game.put("Guessed",1);
        try {
            game.save();
        }catch(Exception e){
            Log.i("Exception: ", e.getLocalizedMessage());
        }

        Intent intent = new Intent(GuessingTurnActivity.this, NewTurnActivity.class);

        intent.putExtra("OPPONENT",from);
        intent.putExtra("TURN_NUMBER",turn_number);

        startActivity(intent);
    }

    public void addView(View newView){
        int pageIndex = pagerAdapter.addView(newView);
        pagerAdapter.notifyDataSetChanged();
        vPager.setCurrentItem(pageIndex);
    }

    private class MyPagerAdapter extends PagerAdapter {

        private int count;

        private ArrayList<View> views = new ArrayList<View>();

        public MyPagerAdapter(){
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position){
            View v = views.get(position);
            container.addView(v);
            return v;
        }

        public int addView(View v){
            return addView(v, views.size());
        }

        public int addView(View v, int position){
            views.add(position, v);
            return position;
        }

        public int removeView(ViewPager pager, int position){
            pager.setAdapter(null);
            views.remove(position);
            pager.setAdapter(this);

            return position;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object){
            container.removeView((View) object);
        }

        @Override
        public int getCount(){
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object){
            return (view==object);
        }
    }
}
