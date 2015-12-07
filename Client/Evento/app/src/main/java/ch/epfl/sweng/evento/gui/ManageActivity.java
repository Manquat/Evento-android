package ch.epfl.sweng.evento.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.gui.event_activity.EventActivity;
import ch.epfl.sweng.evento.list_view.ListEntryAdapter;

import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.GetEventListCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;

public class ManageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "ManageActivity";
    private ArrayList<ListEntryAdapter.Item> mItems = new ArrayList<ListEntryAdapter.Item>();
    private ListView mListView;
    private RestApi mRestAPI;
    private List<Event> mHostedEvent;
    private List<Event> mMatchedEvent;
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        TextView WelcomeView = (TextView) (findViewById(R.id.Welcome));
        WelcomeView.setText("Welcome " + Settings.INSTANCE.getUser().getUsername() + ".");

        mActivity = this;
        mRestAPI = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
        mListView=(ListView)findViewById(R.id.listViewManage);

        mHostedEvent = new ArrayList<Event>();;
        mItems.add(new ListEntryAdapter.Section("Hosted Events"));
        mRestAPI.getHostedEvent(new GetEventListCallback() {
            public void onEventListReceived(List<Event> eventArrayList) {
                if (eventArrayList != null) {
                    mHostedEvent = eventArrayList;
                    int i=1;
                    for (Event event : mHostedEvent) {
                        mItems.add(new ListEntryAdapter.Entry(Integer.toString(i++) + "/ " + event.getTitle(), "        " + event.getDescription()));
                    }
                    mItems.add(new ListEntryAdapter.Section("Joined Events"));
                    mRestAPI.getMatchedEvent(new GetEventListCallback() {
                        public void onEventListReceived(List<Event> eventArrayList) {
                            if (eventArrayList != null) {
                                mMatchedEvent = eventArrayList;
                                int i = 1;
                                for (Event event : mMatchedEvent) {
                                    mItems.add(new ListEntryAdapter.Entry(Integer.toString(i++) + "/ " +event.getTitle(), "        " + event.getDescription()));
                                }
                                ListEntryAdapter adapter = new ListEntryAdapter(mActivity, mItems);
                                mListView.setAdapter(adapter);
                            }
                        }

                    }, Settings.INSTANCE.getUser().getUserId());

                    ListEntryAdapter adapter = new ListEntryAdapter(mActivity, mItems);
                    mListView.setAdapter(adapter);
                }
            }

        }, Settings.INSTANCE.getUser().getUserId());

        mListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {

        ListEntryAdapter.Entry item = (ListEntryAdapter.Entry) mItems.get(position);
        Toast.makeText(this, "You clicked " + item.getTitle(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(mActivity, EventActivity.class);

        if(position < mHostedEvent.size()+1) {
            intent.putExtra(EventActivity.CURRENT_EVENT_KEY, mHostedEvent.get(position-1).getID());
        }
        if(position > mHostedEvent.size()+1) {
            intent.putExtra(EventActivity.CURRENT_EVENT_KEY, mMatchedEvent.get(position-mHostedEvent.size()-2).getID());
        }

        mActivity.startActivity(intent);

    }


}
