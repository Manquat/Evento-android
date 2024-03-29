/*
* Copyright 2013 The Android Open Source Project
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


package ch.epfl.sweng.evento.gui;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;
import ch.epfl.sweng.evento.rest_api.network_provider.NetworkProvider;
import ch.epfl.sweng.evento.tabs_fragment.Refreshable;
import ch.epfl.sweng.evento.tabs_layout.SlidingTabLayout;

/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p/>
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class MainActivity extends AppCompatActivity implements Refreshable {

    private static final int NOTIFICATION_ID = 1234567890;
    private static final String TAG = "MainActivity";
    private static final int MOSAIC_POSITION = 1; // The mosaic position in the tabs (from 0 to 3)
    private static final NetworkProvider networkProvider = new DefaultNetworkProvider();
    private static final String urlServer = Settings.getServerUrl();
    private ViewPager mPager;
    private ViewPageAdapter mAdapter;
    private SlidingTabLayout mTabs;
    private List<CharSequence> mTitles = new ArrayList<CharSequence>(
            Arrays.asList("Maps", "Events", "Calendar"));
    private ArrayList<Event> mEventArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assert (Settings.getUser() != null);
        Log.d(TAG, Settings.getUser().getUsername());

        // Creating the Toolbar and setting it as the Toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        new AutoRefreshToolbar(this, toolbar);

        // Creating the ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs.
        mAdapter = new ViewPageAdapter(getSupportFragmentManager(), mTitles);

        // Assigning ViewPager View and setting the adapter
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        // Set the mosaic as the first launched screen
        mPager.setCurrentItem(MOSAIC_POSITION);

        // Assigning the Sliding Tab Layout View
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true); // This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getApplicationContext(), R.color.tabsScrollColor);
            }
        });
        // Setting the ViewPager For the SlidingTabsLayout
        mTabs.setViewPager(mPager);
    }

    @Override
    public void onStart() {
        super.onStart();

        //adding the mainActivity to the observer of the eventDatabase
        EventDatabase.INSTANCE.addObserver(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        //removing the mainActivity to the observer of the eventDatabase
        EventDatabase.INSTANCE.removeObserver(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void makeNotifications(List<Event> eventArrayList) {
        if (eventArrayList != null) {
            Calendar currentDate = Calendar.getInstance();

            boolean notif_needed = false;
            String notif_description = "";
            for (Event event : eventArrayList) {
                double diffTime = (event.getStartDate().getTimeInMillis() - currentDate.getTimeInMillis())
                        / (1000 * 3600 * 24);


                if (diffTime < 1.0) {
                    notif_needed = true;
                    notif_description += "The event " + event.getTitle() + " is starting tomorrow. \n";

                    //Toast.makeText(getApplicationContext(), "Notified event : " + event.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }
            if (notif_needed) {
                notif_description += "Don't forget to attend !";
                Notification n = new Notification.Builder(this)
                        .setContentTitle("You've got events soon !")
                        .setContentText(notif_description)
                        .setSmallIcon(R.drawable.notification)
                        .setAutoCancel(true).build();

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                notificationManager.notify(NOTIFICATION_ID, n);
            }
        }
    }


    @Override
    public void refresh() {
        makeNotifications(EventDatabase.INSTANCE.getAllEvents());
        Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
    }
}
