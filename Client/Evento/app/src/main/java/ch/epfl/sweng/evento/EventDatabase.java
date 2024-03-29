package ch.epfl.sweng.evento;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.event.EventSet;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.GetEventListCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;
import ch.epfl.sweng.evento.tabs_fragment.Refreshable;

/**
 * Created by Val on 28.10.2015.
 */
public enum EventDatabase implements Refreshable {
    INSTANCE;

    private static final String TAG = "EventDatabase";

    private RestApi mRestAPI;

    private Set<Refreshable> mObserver;

    /**
     * This EventSet represents all the Events currently stored on the device
     */
    private EventSet mEventSet;

    EventDatabase() {
        mEventSet = new EventSet();
        mRestAPI = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
        mObserver = new HashSet<>();

        loadNewEvents();
    }


    public void loadNewEvents() {
        mRestAPI.getAll(new GetEventListCallback() {
            @Override
            public void onEventListReceived(List<Event> events) {
                addAll(events);
            }
        });
    }

    public boolean addObserver(Refreshable observer) {
        return mObserver.add(observer);
    }


    public boolean removeObserver(Refreshable observer) {
        return mObserver.remove(observer);
    }

    public void addAll(List<Event> events) {
        if (events == null) {
            return;
        }
        mEventSet.clear();

        for (Event e : events) {
            mEventSet.addEvent(e);
            Log.i(TAG, "EVENT LOADED " + e.getTitle());
        }

        updateObserver();
    }


    public void addOne(Event e) {
        if (e == null) {
            return;
        }
        mEventSet.addEvent(e);
        updateObserver();
    }


    public void loadByDate(GregorianCalendar start, GregorianCalendar end) {
        mRestAPI.getMultiplesEventByDate(start, end, new GetEventListCallback() {
            @Override
            public void onEventListReceived(List<Event> eventArrayList) {
                mEventSet.clear();
                addAll(eventArrayList);
            }
        });
    }

    /**
     * Returns the Event corresponding to the ID passed in argument
     *
     * @param id the ID of the desired Event
     * @return the Event corresponding to the Signature.
     */
    public Event getEvent(int id) {
        return mEventSet.get(id);
    }

    public Event getFirstEvent() {
        return mEventSet.getFirst();
    }

    public List<Event> getAllEvents() {
        return mEventSet.toArrayList();
    }

    /**
     * This method returns the next Event after the one passed in argument, in the order of starting
     * CustomDate and ID. If 'current' is the last one, it will return it instead.
     *
     * @param current the current Event which is the reference to get the next Event
     * @return the Event that is right after the 'current' Event in the starting CustomDate order
     */
    public Event getNextEvent(Event current) {
        return mEventSet.getNext(current);
    }

    /**
     * This method returns the previous Event before the one passed in argument, in the order of starting
     * CustomDate and ID. If 'current' is the first one, it will return it instead.
     *
     * @param current the current Event which is the reference to get the previous Event
     * @return the Event that is right before the 'current' Event in the starting CustomDate order
     */
    public Event getPreviousEvent(Event current) {
        return mEventSet.getPrevious(current);
    }

    public Event get(int position) {
        Event currentEvent = getFirstEvent();
        for (int i = 0; i <= position; i++) {
            currentEvent = getNextEvent(currentEvent);
        }
        return currentEvent;
    }

    public EventSet filter(LatLng latLng, double distance) {
        return mEventSet.filter(latLng, distance);
    }

    public EventSet filter(Set<String> tags) {
        return mEventSet.filter(tags);
    }

    public EventSet filter(String tag) {
        return mEventSet.filter(tag);
    }

    public EventSet filter(Calendar startDate) {
        return mEventSet.filter(startDate);
    }

    public EventSet filterOnDay(Calendar calendar) {
        return mEventSet.filterOnDay(calendar);
    }


    public void refresh() {
        loadNewEvents();
    }

    private void updateObserver() {
        for (Refreshable observer : mObserver) {
            observer.refresh();
        }
    }

    public void clear() {
        mEventSet.clear();
    }


    public int getNumberOfEvent() {
        if (mEventSet == null) {
            return 0;
        } else {
            return mEventSet.size();
        }
    }

    public int getSize() {
        if (mEventSet == null) {
            return 0;
        } else {
            return mEventSet.size();
        }
    }
}
