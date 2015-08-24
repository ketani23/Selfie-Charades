package com.example.jenil.parsedemo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ActingTurnActivity extends Activity {

    private static final int MAX_PICTURES = 5;
    private static final int PAGE_MARGIN = 5;
    private static final int CAMERA_REQUEST = 1888;

    private ViewPager vPager;
    private MyPagerAdapter pagerAdapter;

    private int picture_Count = 0;
    private String movie;
    private String opponent;
    private int turn_number;

    //private Button takePicture;
    private Button submitPics;
    private ImageButton addPic;
    //private ImageButton deletePic;

    private TextView pictureCount;
    private TextView movieName;
    private TextView opponentName;

    private PagerContainer container;

    private ArrayList<Bitmap> pictures;

    static private int currentViewIndex=0;

    private boolean submitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acting_turn);

        Intent intent = getIntent();
        movie = intent.getStringExtra("MOVIE");
        opponent = intent.getStringExtra("OPPONENT");
        turn_number = intent.getIntExtra("TURN_NUMBER",0);
        if(turn_number==0){
            Log.i("ERROR:", "NO TURN NUMBER IN INTENT");
        }

        pictures = new ArrayList<Bitmap>();

        //takePicture = (Button)findViewById(R.id.TakePicture);
        submitPics = (Button) findViewById(R.id.submitPics);
        addPic = (ImageButton)findViewById(R.id.addPic);
        //deletePic = (ImageButton)findViewById(R.id.deletePic);

        pictureCount = (TextView)findViewById(R.id.pictureCount);
        movieName = (TextView)findViewById(R.id.movieName);
        opponentName = (TextView)findViewById(R.id.opponentName);

        int left = MAX_PICTURES - picture_Count;

        pictureCount.setText(" You can add " + left + " pictures");
        movieName.setText(movie);
        opponentName.setText(" vs. " + opponent);

        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picture_Count<MAX_PICTURES) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST);
                }else{
                    Toast.makeText(getApplicationContext(), "Sorry! A maximum of 5 pictures is allowed",Toast.LENGTH_SHORT).show();
                }
            }
        });

//        deletePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(picture_Count==0){
//                    Toast.makeText(getApplicationContext(), "Oops! There are no pictures to delete!", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    removeView(currentViewIndex);
//                    if(picture_Count==1){
//                        TextView tview = new TextView(ActingTurnActivity.this);
//                        tview.setText("No pictures");
//                        tview.setGravity(Gravity.CENTER);
//                        tview.setBackgroundColor(Color.WHITE);
//
//                        addView(tview);
//                        currentViewIndex=0;
//                    }
//                    picture_Count--;
//                    int left = MAX_PICTURES - picture_Count;
//
//                    pictureCount.setText(" You can add " + left + " more pictures");
//                }
//            }
//        });

//        takePicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(picture_Count<=MAX_PICTURES) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent, CAMERA_REQUEST);
//                }else{
//                    Toast.makeText(getApplicationContext(), "Sorry! A maximum of 5 pictures is allowed",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        submitPics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picture_Count==0){
                    new AlertDialog.Builder(ActingTurnActivity.this)
                            .setTitle("Oops!")
                            .setMessage("You have to take a picture first you know...")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // nothing to do
                                }
                            })
                            .setIcon(android.R.drawable.ic_menu_info_details)
                            .show();
                }else {
                    new AlertDialog.Builder(ActingTurnActivity.this)
                            .setTitle("Submit your pictures")
                            .setMessage("Are you ready to submit your pics?")
                            .setPositiveButton("Yup!", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    submitPictures();
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
            }
        });

        container = (PagerContainer) findViewById(R.id.pager_container);

        vPager = container.getViewPager();
        pagerAdapter = new MyPagerAdapter();
        vPager.setAdapter(pagerAdapter);

        vPager.setOffscreenPageLimit(pagerAdapter.getCount());

        vPager.setPageMargin(PAGE_MARGIN);

        vPager.setClipChildren(false);

        /*
        TextView view = new TextView(ActingTurnActivity.this);
        view.setText("Item ");
        view.setGravity(Gravity.CENTER);
        view.setBackgroundColor(Color.argb(255, 150, 30, 150));

        pagerAdapter.addView(view, 1);*/
    }

    /*
    public void findGame(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GAME");
        query.whereEqualTo("CurrentTurnOwner", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                submitPictures(list.get(0));
            }
        });
    }*/

//    static public void updateIndex(int inc){
//        currentViewIndex = currentViewIndex + inc;
//    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
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
        Intent backIntent = new Intent(ActingTurnActivity.this,MainActivity.class);
        startActivity(backIntent);
    }

    public void submitPictures(){
        for(Bitmap b: pictures){
            byte[] imgdata = getBytesFromBitmap(b);
            ParseFile photo = new ParseFile("photo.jpg",imgdata);

            ParseObject image = new ParseObject("IMAGE");

            image.put("photo",photo);
            image.put("For", opponent);
            image.put("From", ParseUser.getCurrentUser().getUsername());
            image.put("Movie",movie);
            image.put("TurnNumber",turn_number);

            image.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e!=null){
                        Log.i("ERROR: ", "IMAGE NOT UPLOADED");
                    }
                }
            });
        }

        ParseQuery pQuery = ParseInstallation.getQuery();
        pQuery.whereEqualTo("username", opponent);

        ParsePush push = new ParsePush();
        push.setQuery(pQuery);
        push.setMessage(ParseUser.getCurrentUser().getUsername() + " just sent a turn! Guess now!");
        push.sendInBackground();

        submitted=true;

        Intent intent = new Intent(ActingTurnActivity.this,MainActivity.class);
        startActivity(intent);


        /*
        ParseObject turn = game.getParseObject("CurrentTurnObject");
        ArrayList<ParseFile> pictureList = new ArrayList<ParseFile>();
        for(Bitmap b: pictures){
            byte[] imgData = getBytesFromBitmap(b);
            ParseFile img = new ParseFile("Image.jpg",imgData);
            pictureList.add(img);
        }
        for(ParseFile p:pictureList){
            try {
                ParseObject image = new ParseObject("IMAGE");
                image.put("Photo",p);
                image.put("TurnObjectId",turn.getObjectId());
                image.save();
                //turn.put("Picture",image);
                p.save();
            }catch(Exception e){
                //Log.d(e.getLocalizedMessage());
            }
        }

        //turn.addAll("Pictures", pictureList);
        turn.saveInBackground();

        //Intent intent = new Intent(ActingTurnActivity.this,MainActivity.class);
        //startActivity(intent);
        */
    }

    public void addView(View newView){
        int pageIndex = pagerAdapter.addView(newView);
        pagerAdapter.notifyDataSetChanged();
        vPager.setCurrentItem(pageIndex);
        currentViewIndex = pageIndex;
    }

    public void removeView(int position){
        int pageIndex = pagerAdapter.removeView(vPager, position);
        pagerAdapter.notifyDataSetChanged();

        if(pageIndex == pagerAdapter.getCount()){
            pageIndex--;
        }
        vPager.setCurrentItem(pageIndex);
        currentViewIndex = pageIndex;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            if (picture_Count == 0) {
                removeView(0);
            }
            Bitmap photo = (Bitmap)data.getExtras().get("data");
            ImageView imageView = new ImageView(ActingTurnActivity.this);
            imageView.setImageBitmap(photo);
            pictures.add(photo);

            //Log.i("TAG: ", "Back from temp");

            addView(imageView);
            picture_Count++;

            int left = MAX_PICTURES - picture_Count;

            pictureCount.setText(" You can add " + left + " more pictures");
        }
    }

    private class MyPagerAdapter extends PagerAdapter{

        private int count;

        private ArrayList<View> views = new ArrayList<View>();

        public MyPagerAdapter(){
            TextView view = new TextView(ActingTurnActivity.this);
            view.setText("No pictures");
            view.setGravity(Gravity.CENTER);
            view.setBackgroundColor(Color.WHITE);

            views.add(view);
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
