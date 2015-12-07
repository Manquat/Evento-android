package ch.epfl.sweng.evento.rest_api;

import android.util.Log;

import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.event.Event;

/**
 * Created by joachimmuth on 16.10.15.
 * <p/>
 * Provide the serialization method to transform android class into string understandable by the server
 */
public final class Serializer {
    private static final String TAG = "Serializer";

    private Serializer() {
        // private constructor
    }

    public static String event(Event e) {

        String res = "{\n"
                + "  \"Event_name\": \"" + e.getTitle() + "\",\n"
                + "  \"tags\": \"" + e.getTagsString() + "\",\n"
                + "  \"image\": \n"
                + "    \"" + e.getPictureAsString() + "\" ,\n"
                + "  \"description\": \n"
                + "    \"" + e.getDescription() + "\" ,\n"
                + "  \"latitude\": " + e.getLatitude() + ",\n"
                + "  \"longitude\": " + e.getLongitude() + ",\n"
                + "  \"address\": \"" + e.getAddress() + "\",\n"
                + "  \"date\":\"" + e.getProperDateString() + "\",\n"
                + "  \"owner\":\"" + Settings.INSTANCE.getUser().getUserId() + "\"\n"
                + "}\n";
        return res;
    }

    public static String user(String name, String email, String googleid) {
        String res = "{\n"
                + "  \"name\": \"" + name + "\", \n"
                + "  \"email\": \"" + email + "\", \n"
                + "  \"googleid\": \"" + googleid + "\" \n"
                + "}\n";

        Log.d(TAG, "Information sent to Server: " + res);

        return res;
    }

    public static String comment(int userId, String userName, int eventId, String commentBody) {
        String res = "{\n"
                + "\"body\": \"" + commentBody + "\",\n"
                + "\"creator\": " + userId + ",\n"
                + "\"creator_name\": \"" + userName + "\",\n"
                + "\"event\": " + eventId + "\n"
                + "}\n";
        return res;
    }
}
