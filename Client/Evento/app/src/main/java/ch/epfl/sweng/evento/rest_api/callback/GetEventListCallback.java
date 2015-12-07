package ch.epfl.sweng.evento.rest_api.callback;

import java.util.List;

import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.event.Event;

/**
 * Created by joachimmuth on 13.11.15.
 */
public abstract class GetEventListCallback {

    public abstract void onEventListReceived(List<Event> eventArrayList);
    public abstract void onUserListReceived(List<User> userArrayList);
}
