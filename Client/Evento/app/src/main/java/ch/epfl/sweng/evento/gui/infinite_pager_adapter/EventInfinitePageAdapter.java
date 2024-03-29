package ch.epfl.sweng.evento.gui.infinite_pager_adapter;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.gui.ConversationAdapter;
import ch.epfl.sweng.evento.gui.ListOfParticipantListener;
import ch.epfl.sweng.evento.gui.UpdateEventListener;
import ch.epfl.sweng.evento.gui.event_activity.AddingComment;
import ch.epfl.sweng.evento.gui.event_activity.JoinEvent;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.GetUserCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;

/**
 * An infinite page adapter for the event activity
 */
public class EventInfinitePageAdapter extends InfinitePagerAdapter<Integer> {
    public static final String TAG = "EventInfPageAdapter";
    Activity mActivity;
    private RestApi mRestApi;

    public EventInfinitePageAdapter(Integer initialEventId, Activity activity) {
        super(initialEventId);
        mActivity = activity;
        mRestApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
    }

    @Override
    public Integer getNextIndicator() {
        Event currentEvent = EventDatabase.INSTANCE.getEvent(getCurrentIndicator());
        return EventDatabase.INSTANCE.getNextEvent(currentEvent).getID();
    }

    @Override
    public Integer getPreviousIndicator() {
        Event currentEvent = EventDatabase.INSTANCE.getEvent(getCurrentIndicator());
        return EventDatabase.INSTANCE.getPreviousEvent(currentEvent).getID();
    }

    @Override
    public ViewGroup instantiateItem(Integer currentEventId) {
        // getting the event
        Event event = EventDatabase.INSTANCE.getEvent(currentEventId);

        // inflating  the layout
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        LinearLayout rootLayout = (LinearLayout) inflater.inflate(R.layout.event_adapter,
                (ViewGroup) mActivity.getWindow().getDecorView().getRootView(), false);

        ConversationAdapter conversationAdapter = new ConversationAdapter(mActivity, currentEventId);
        ListView listOfComment = (ListView) rootLayout.findViewById(R.id.event_list_comment);
        listOfComment.setAdapter(conversationAdapter);

        GridLayout layout = (GridLayout) inflater.inflate(R.layout.fragment_event,
                listOfComment, false);

        updateFields(layout, event);

        listOfComment.addHeaderView(layout);

        AddingComment.initialize(mActivity, listOfComment, currentEventId, conversationAdapter);


        return rootLayout;
    }

    private void getHost(Event event, View view) {
        final View innerView = view;

        mRestApi.getUser(new GetUserCallback() {
            public void onDataReceived(User user) {
                TextView creatorView = (TextView) innerView.findViewById(R.id.event_creator_view);
                if (user != null) {
                    creatorView.setText(user.getUsername());
                } else {
                    Toast.makeText(mActivity, "The owner don't exist", Toast.LENGTH_SHORT);
                }
            }
        }, event.getOwner());
    }


    private void updateFields(ViewGroup rootView, final Event currentEvent) {
        TextView titleView = (TextView) rootView.findViewById(R.id.event_title_view);
        TextView creatorView = (TextView) rootView.findViewById(R.id.event_creator_view);
        TextView startDateView = (TextView) rootView.findViewById(R.id.event_start_date_view);
        TextView endDateView = (TextView) rootView.findViewById(R.id.event_end_date_view);
        TextView addressView = (TextView) rootView.findViewById(R.id.event_address_view);
        TextView descriptionView = (TextView) rootView.findViewById(R.id.event_description_view);

        getHost(currentEvent, rootView);

        titleView.setText(currentEvent.getTitle());
        creatorView.setText(Integer.toString(currentEvent.getOwner()));
        startDateView.setText(currentEvent.getStartDateAsString());
        endDateView.setText(currentEvent.getEndDateAsString());
        addressView.setText(currentEvent.getAddress());
        Point screenSize = new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(screenSize);
        addressView.setMaxWidth((screenSize.x * 3) / 4);

        descriptionView.setText(currentEvent.getDescription());
        descriptionView.setMaxWidth((screenSize.x * 3) / 4);

        ImageView pictureView = (ImageView) rootView.findViewById(R.id.eventPictureView);
        pictureView.setImageBitmap(currentEvent.getPicture());
        Button listOfParticipant = (Button) rootView.findViewById(R.id.list_participant_exp);
        ListOfParticipantListener participant =
                new ListOfParticipantListener(mActivity, currentEvent.getID(), listOfParticipant);

        // configure the joint and unJoin button
        Button joinEventButton = (Button) rootView.findViewById(R.id.joinEvent);
        Button unJoinEventButton = (Button) rootView.findViewById(R.id.remove_user_from_event);

        JoinEvent.initialize(mActivity, currentEvent.getID(), joinEventButton, unJoinEventButton,
                participant);

        // configure the delete event button
        Button deleteEvent = (Button) rootView.findViewById(R.id.delete_event);
        deleteEvent.setVisibility(View.INVISIBLE);
        deleteEvent.setOnClickListener(new DeleteEventListener(mActivity, currentEvent.getID()));

        Button updateEvent = (Button) rootView.findViewById(R.id.update_event);
        updateEvent.setVisibility(View.INVISIBLE);
        updateEvent.setOnClickListener(new UpdateEventListener(mActivity, currentEvent.getID()));

        boolean isTheCurrentUserTheOwner =
                (currentEvent.getOwner() == Settings.getUser().getUserId());
        Log.d(TAG, "is currentOwner : creator " + currentEvent.getOwner());
        Log.d(TAG, "is currentOwner : user " + Settings.getUser().getUserId());
        Log.d(TAG, "current event : " + currentEvent.getTitle());
        if (isTheCurrentUserTheOwner) {
            deleteEvent.setVisibility(View.VISIBLE);
            updateEvent.setVisibility(View.VISIBLE);
        }
    }
}
