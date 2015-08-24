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
import android.content.pm.PackageManager;
import android.content.Context;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.provider.MediaStore;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GamePlayActivity extends FragmentActivity implements ActionBar.TabListener {
    protected String Phrase;
    public static String[] words;
    public static String opponent;
    public Button b;
    static Context c;

    Bitmap[] pics = new Bitmap[2];
    int count=0;

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

    public void exitPlan(){
        Intent intent = new Intent(GamePlayActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_game_play);

        Intent intent = getIntent();
        Phrase = intent.getStringExtra("MOVIE");
        words = Phrase.split(" ");

        opponent = intent.getStringExtra("OPPONENT");

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.



        final ActionBar actionBar = getActionBar();

        if(actionBar==null){
            Log.i("ALERT", "NULL");
        }
        else{
            Log.i("ALERT", "NOT NULL");
        }

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
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
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount() - 1; i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Submit")
                        .setTabListener(this));


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


        public AppSectionsPagerAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if(i>words.length-1)
            {
                i=100;
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
                    case 3:
                        // The first section of the app is the most interesting -- it offers
                        // a launchpad into the other demonstrations in this example application.
                        LaunchpadSectionFragment l3 = new LaunchpadSectionFragment(i);
                        return l3;
                    default:

                        Fragment fragment = new DummySectionFragment();
                        return fragment;
                }
            }

        @Override
        public int getCount() {
            return words.length + 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return words[position];
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class LaunchpadSectionFragment extends Fragment {
        public final String BITMAP_STORAGE_KEY = "viewbitmap";
        public final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
        public ImageView mImageView;
        public Bitmap mImageBitmap;
        public ImageButton picBtn;
        public TextView t;
        String mCurrentPhotoPath;
        private AlbumStorageDirFactory mAlbumStorageDirFactory;
        int position;

        String JPEG_FILE_PREFIX = "IMG_";
        String JPEG_FILE_SUFFIX = ".jpg";

        public LaunchpadSectionFragment(int i){
            position=i;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
            picBtn = (ImageButton) rootView.findViewById(R.id.imageButton);
            mImageView = (ImageView) rootView.findViewById(R.id.imageView);
            mImageBitmap = null;
            picBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent();
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
            } else {
                mAlbumStorageDirFactory = new BaseAlbumDirFactory();
            }


            return rootView;


        }


        /* Photo album for this application */

        private String getAlbumName() {
            return getString(R.string.album_name);
        }

        private File getAlbumDir() {
            File storageDir = null;

            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

                storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

                if (storageDir != null) {
                    if (!storageDir.mkdirs()) {
                        if (!storageDir.exists()) {
                            Log.d("CameraSample", "failed to create directory");
                            return null;
                        }
                    }
                }

            } else {
                Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
            }

            return storageDir;
        }

        private File createImageFile() throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
            File albumF = getAlbumDir();
            File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
            return imageF;
        }

        private File setUpPhotoFile() throws IOException {

            File f = createImageFile();
            mCurrentPhotoPath = f.getAbsolutePath();

            return f;
        }

        private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
            int targetW = mImageView.getWidth();
            int targetH = mImageView.getHeight();


		/* Get the size of the image */
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
            int scaleFactor = 1;
            if ((targetW > 0) || (targetH > 0)) {
                scaleFactor = Math.min(targetW / photoW, targetH / photoH);
            }

		/* Set bitmap options to scale the image decode target */
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */

            byte[] scaledData = getBytesFromBitmap(bitmap);

            ParseFile photoFile = new ParseFile("image.jpg",scaledData);

            ParseObject imageup;

            imageup = new ParseObject("Selfie");
            imageup.put("photo",photoFile);

            imageup.put("OpponentName",opponent);
            imageup.put("Owner", ParseUser.getCurrentUser());
            imageup.put("OwnerName",ParseUser.getCurrentUser().getUsername());
            imageup.put("Count",words.length);
            imageup.put("Word", words[position]);
            imageup.put("WordNum",position);

            String movie=words[0];
            for(int i=1;i<words.length;i++){
                movie = movie + " " + words[i];
            }
            imageup.put("Movie", movie);

            imageup.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    //nothing to do
                }
            });

            mImageView.setImageBitmap(bitmap);


            mImageView.setVisibility(View.VISIBLE);

        }

        public byte[] getBytesFromBitmap(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            return stream.toByteArray();
        }

        private void galleryAddPic() {
            Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);


        }

        private void dispatchTakePictureIntent() {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


            File f = null;

            try {
                f = setUpPhotoFile();
                mCurrentPhotoPath = f.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            } catch (IOException e) {
                e.printStackTrace();
                f = null;
                mCurrentPhotoPath = null;
            }


            startActivityForResult(takePictureIntent, 1);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (resultCode == RESULT_OK) {
                handleBigCameraPhoto();
            }
        }


        private void handleBigCameraPhoto() {

            if (mCurrentPhotoPath != null) {
                setPic();
                galleryAddPic();
                mCurrentPhotoPath = null;
            }


        }

    }

    public static class DummySectionFragment extends Fragment {

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.submit_layout, container, false);

            Button submitBtn = (Button) rootView.findViewById(R.id.submitButton);
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    Toast.makeText(getActivity()," Turn Completed!",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            });

            return rootView;
        }
    }
}






