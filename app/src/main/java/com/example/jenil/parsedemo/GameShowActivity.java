/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.jenil.parsedemo;
import android.content.Context;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class GameShowActivity extends FragmentActivity implements ActionBar.TabListener {

    public Button b;
    static Context c;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_game_show_adapter);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pagerShow);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }



    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public int count;
        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override

        public Fragment getItem(int i) {
            {

                if (i>getCount()) {
                    i = 100;
                }
                switch (i) {


                    case 0:
                        // The first section of the app is the most interesting -- it offers
                        // a launchpad into the other demonstrations in this example application.
                        LaunchpadSectionFragment l = new LaunchpadSectionFragment(i);
                        return l;
                    case 1:
                        // The first section of the app is the most interesting -- it offers
                        // a launchpad into the other demonstrations in this example application.
                        LaunchpadSectionFragment l1 = new LaunchpadSectionFragment(i);
                        return l1;
                    case 2:
                        // The first section of the app is the most interesting -- it offers
                        // a launchpad into the other demonstrations in this example application.
                        LaunchpadSectionFragment l2 = new LaunchpadSectionFragment(i);
                        return l2;
                   /* case 3:
                        // The first section of the app is the most interesting -- it offers
                        // a launchpad into the other demonstrations in this example application.
                        LaunchpadSectionFragment l3 = new LaunchpadSectionFragment();
                        return l3;*/
                    default:

                        Fragment fragment = new DummySectionFragment();
                        return fragment;
                }
            }

        }

        @Override
        public int getCount() {
            /*
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Selfie");
            query.whereEqualTo("OpponentName", ParseUser.getCurrentUser().getUsername());
            query.whereEqualTo("OwnerName","Aniket");
            query.whereEqualTo("WordNum", 0);

            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if(parseObject!=null){
                        Log.i("CHECK ", "NOT NULL");
                        count = (int)parseObject.get("Count");
                        Log.i(" COUNT INSIDE ",Integer.toString(count));
                    }
                    else{
                        Log.i(" CHECK ", "NULL");
                    }
                }
            });

            Log.i("COUNT IS ", Integer.toString(count));
            return count;*/
            return 1;

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "image" + (position + 1);
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class LaunchpadSectionFragment extends Fragment {

        int position;

        public ImageView img;

        public LaunchpadSectionFragment(int i){
            position = i;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_game_show, container, false);

            img = (ImageView)rootView.findViewById(R.id.imageView);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Selfie");
            query.whereEqualTo("OpponentName", ParseUser.getCurrentUser().getUsername());
            query.whereEqualTo("OwnerName","Aniket");
            query.whereEqualTo("WordNum",position);

            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if(parseObject!=null){
                        Log.i("CHECK ", "NOT NULL");
                        ParseFile pic = (ParseFile)parseObject.get("photo");
                        pic.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                if(e==null){
                                    img.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
                                }
                            }
                        });
                    }
                    else{
                        Log.i(" CHECK ", "NULL");
                    }
                }
            });

            return rootView;


        }
    }


    public static class DummySectionFragment extends Fragment {
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.submit_layout, container, false);
            return rootView;
        }
    }


}


