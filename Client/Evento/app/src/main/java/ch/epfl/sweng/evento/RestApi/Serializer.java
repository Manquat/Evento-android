package ch.epfl.sweng.evento.RestApi;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.Events.EventFilter;

/**
 * Created by joachimmuth on 16.10.15.
 *
 * Provide the serialization method to transform android class into string understandable by the server
 *
 */
public final class Serializer {


    public static String event(Event e){
        String res;
        res = "{\n"
                + "  \"Event_name\": \"" + e.getTitle() + "\",\n"
                + "  \"description\": \n"
                + "    \"" + e.getDescription() + "\" ,\n"
                + "  \"latitude\": " + e.getLatitude() + ",\n"
                + "  \"longitude\": " + e.getLongitude() + ",\n"
                + "  \"address\": \"" + e.getAddress() + "\", \n "
                + "  \"creator\": \"" + e.getCreator() + "\"\n"
                + "}\n";
        return res;
    }

    public static String filter(EventFilter f){
        String res;
        res = null;
        return res;
    }

}
