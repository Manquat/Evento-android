package ch.epfl.sweng.evento.event;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;


/**
 * The event class that holds all the information related to the event :
 * date of beginning, date of end, title, owner, .....
 */
public class Event implements ClusterItem {
    private static final String TAG = "Event";
    private final int mID; // the id of the event, unique and attributed by the server
    private final String mTitle; // the title of the event
    private final String mDescription; // the long description of the event
    private final LatLng mLocation; // the location of the event as a LatLng of the Google Maps API
    private final String mAddress; // the address of the
    private final int mOwnerId; // the unique Id of the user that own this event
    private final Set<String> mTags; // the tags of the event
    private final int mNumberMaxOfParticipants; // number maximum of user that can join the event
    private Calendar mStartDate; // the beginning of the event as a Calendar
    private Calendar mEndDate; // the end of the event as a Calendar
    private String mPicture; // the picture of the event as a string


    /**
     * Constructor of the event
     *
     * @param id          the unique id given by the server
     * @param title       the title of the event
     * @param description the detailed description
     * @param latitude    the latitude where the event take place
     * @param longitude   the longitude where the event take place
     * @param address     the address where the event take place
     * @param ownerId     the unique id of the user that own this event
     * @param tags        the tags associate with this event
     * @param startDate   the beginning of the event
     * @param endDate     the end of the event
     * @param image       the image of the event
     */
    public Event(int id,
                 String title,
                 String description,
                 double latitude,
                 double longitude,
                 String address,
                 int ownerId,
                 Set<String> tags,
                 Calendar startDate,
                 Calendar endDate,
                 String image) {
        mID = id;
        mTitle = title;
        mDescription = description;
        mLocation = new LatLng(latitude, longitude);
        mAddress = address;
        mOwnerId = ownerId;
        mTags = tags;
        mStartDate = startDate;
        mEndDate = endDate;
        mPicture = image;
        mNumberMaxOfParticipants = 10;
    }

    /**
     * Constructor of the event
     *
     * @param id          the unique id given by the server
     * @param title       the title of the event
     * @param description the detailed description
     * @param latitude    the latitude where the event take place
     * @param longitude   the longitude where the event take place
     * @param address     the address where the event take place
     * @param ownerId     the unique id of the user that own this event
     * @param tags        the tags associate with this event
     * @param startDate   the beginning of the event
     * @param endDate     the end of the event
     * @param picture     the image of the event
     */
    public Event(int id, String title, String description, double latitude, double longitude,
                 String address, int ownerId, Set<String> tags, Calendar startDate,
                 Calendar endDate, Bitmap picture) {
        this(id, title, description, latitude, longitude, address, ownerId, tags,
                startDate, endDate, bitmapToString(picture));
    }

    /**
     * Constructor of the event
     *
     * @param id          the unique id given by the server
     * @param title       the title of the event
     * @param description the detailed description
     * @param latitude    the latitude where the event take place
     * @param longitude   the longitude where the event take place
     * @param address     the address where the event take place
     * @param ownerId     the unique id of the user that own this event
     * @param tags        the tags associate with this event
     * @param startDate   the beginning of the event
     * @param endDate     the end of the event
     */
    public Event(int id, String title, String description, double latitude, double longitude,
                 String address, int ownerId, Set<String> tags, Calendar startDate,
                 Calendar endDate) {
        this(id, title, description, latitude, longitude, address, ownerId, tags,
                startDate, endDate, samplePicture());
    }

    /**
     * Constructor of the event
     *
     * @param id          the unique id given by the server
     * @param title       the title of the event
     * @param description the detailed description
     * @param latitude    the latitude where the event take place
     * @param longitude   the longitude where the event take place
     * @param address     the address where the event take place
     * @param ownerId     the unique id of the user that own this event
     * @param tags        the tags associate with this event
     */
    public Event(int id, String title, String description, double latitude, double longitude,
                 String address, int ownerId, Set<String> tags, String image) {
        this(id, title, description, latitude, longitude, address, ownerId, tags,
                new GregorianCalendar(), new GregorianCalendar(), image);

    }

    /**
     * Convert a bitmap into a string at the format Base64
     *
     * @param bitmap the bitmap to transform
     * @return a string of the bitmap
     */
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT).replace(System.getProperty("line.separator"), "");
    }

    /**
     * Return a nicely formatted date for a given calendar, by using the local parameter of the phone
     *
     * @param calendar the calendar wanted as a string
     * @return a string with the formatted date
     */
    public static String asNiceString(Calendar calendar) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * Default picture when the user don't give one
     *
     * @return a big string that contains the default image
     */
    public static String samplePicture() {
        return "Qk1mKgAAAAAAADYAAAAoAAAAPAAAADwAAAABABgAAAAAADAqAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP// AP//AAAAAAAASIT/SIT/SIT/k8bpk8bpk8bpk8bpk8Xnk8bplcjrk8Xnk8bpk8bpk8bpk8bpSIT/ SIT/SIT/AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP// AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP// AP//AP//AAAAAAAASIT/SIT/SIT/lcjrk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bp k8bpk8bpk8bplcjrSIT/SIT/SIT/AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AP// AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AP// AP//AP//AP//AAAAAAAASIT/SIT/SIT/k8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8Xnk8Xnlcjr k8Xnk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpSIT/SIT/SIT/AAAAAAAAAP//AP//AP//AP//AP// AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP// AP//AP//AP//AP//AAAAAAAASIT/SIT/SIT/k8bpk8bpk8bpk8XnjsHlk8bpk8bpk8bplcjrlcjr k8bpk8Xnk8bpk8bplcjrlcjrk8bpk8bpk8bpjsHlk8Xnk8bpk8bpk8bpSIT/SIT/SIT/AAAAAAAA AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP// AP//AP//AP//AP//AP//AP//AAAASIT/SIT/SIT/k8bpk8bpk8bpk8bpk8bpicPqjsHlk8Xnk8bp lcjrlcjrlcjrk8bpk8Xnk8bpk8bplcjrlcjrlcjrk8bpk8XnjsHlicPqk8bpk8bpk8bpk8bpk8bp SIT/SIT/SIT/AAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP// AP//AP//AP//AP//AP//AP//AP//AP//AAAASIT/SIT/lcjrk8bpk8bpk8bpk8bpk8bpk8bpk8bp k8bpk8bpAAAAAAAAAAAAAAAAAAAAk8bpk8bpAAAAAAAAAAAAAAAAAAAAk8bpk8bpk8bpk8bpk8bp k8bpk8bpk8bpk8bplcjrSIT/SIT/AAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP// AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AAAASIT/SIT/k8bpk8bpk8bpk8bpk8bpk8bp lcjrk8bpk8bplcjrAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAlcjr k8bpk8bplcjrk8bpk8bpk8bpk8bpk8bpk8bpSIT/SIT/AAAAAP//AP//AP//AP//AP//AP//AP// AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AAAASIT/k8Xnk8Xnlcjrk8bp k8bpk8XnlcjrAAAAAAAAAAAAAAAAAAAABAULAAAAAAAAAAAABAULAAAAAAAABAULAAAAAAAAAAAA BAULAAAAAAAAAAAAAAAAAAAAlcjrk8Xnk8bpk8bplcjrk8Xnk8XnSIT/AAAAAP//AP//AP//AP// AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AAAASIT/k8bp lcjrk8Xnlcjrlcjrk8bpAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAk8bplcjrlcjrk8Xnlcjrk8bpSIT/AAAAAP// AP//AP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AAAA SIT/SIT/k8bpk8bpk8bpk8bpicPqAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAicPqk8bpk8bpk8bpk8bp SIT/SIT/AAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP// AP//AP//AAAASIT/lcjrk8bpk8bplcjrlcjrk8bpAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA SIT/SIT/SIT/SIT/SIT/SIT/SIT/SIT/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAk8bplcjr lcjrk8bpk8bplcjrSIT/AAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP// AP//AP//AAAAAAAAAAAASIT/SIT/lcjrk8bpk8bpicPqk8bpk8bpAAAAAAAAAAAAAAAAAAAAAAAA AAAASIT/SIT/SIT/k8bpk8bpk8bpk8bpk8bpk8bpSIT/SIT/SIT/AAAAAAAAAAAAAAAAAAAAAAAA AAAAk8bpk8bpicPqk8bpk8bplcjrSIT/SIT/AAAAAAAAAAAAAP//AP//AP//AP//AP//AP//AAAA AAAAAP//AP//AAAAAAAASIT/SIT/AAAASIT/k8bpk8Xnk8bpk8bpk8bpAAAAAAAAAAAAAAAAAAAA AAAAAAAABAULSIT/SIT/k8bpk8bplcjrk8bpk8bpicPqk8bplcjrk8bpk8bpSIT/SIT/BAULAAAA AAAAAAAAAAAAAAAAAAAAAAAAk8bpk8bpk8bpk8Xnk8bpSIT/AAAASIT/SIT/AAAAAAAAAP//AP// AP//AP//AAAAAAAAAP//AAAASIT/SIT/SIT/jsHlAAAASIT/k8bpk8bpk8bpk8bpk8bpAAAAAAAA AAAAAAAAAAAAAAAAAAAASIT/SIT/lcjricPqk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpicPqlcjr SIT/SIT/AAAAAAAAAAAAAAAAAAAAAAAAAAAAk8bpk8bpk8bpk8bpk8bpSIT/AAAAjsHlSIT/SIT/ SIT/AAAAAP//AP//AP//AAAAAAAAAAAASIT/SIT/k8bpjsHljsHlAAAASIT/k8bpk8bpk8bpk8bp k8bpBAULAAAAAAAAAAAAk8bpk8bpAAAASIT/lcjrlcjrk8XnjsHlicPqk8bpk8bpk8bpk8bpicPq jsHlk8XnlcjrlcjrSIT/AAAAk8bpk8bpAAAAAAAAAAAABAULk8bpk8bpk8bpk8bpk8bpSIT/AAAA jsHljsHlk8bpSIT/SIT/AAAAAP//AP//AAAAAAAAAAAASIT/jsHlk8bpjsHljsHlAAAASIT/k8bp k8bpk8bpk8bpk8bpAAAAAAAAAAAAAAAAk8bpk8bpAAAASIT/k8bpk8Xnlcjrlcjrk8bpk8bplcjr k8Xnk8bpk8bplcjrlcjrk8Xnk8bpSIT/AAAAk8bpk8bpAAAAAAAAAAAAAAAAk8bpk8bpk8bpk8bp k8bpSIT/AAAAjsHljsHlk8bpjsHlSIT/AAAAAP//AP//AAAAAAAAAAAASIT/jsHlk8bpjsHljsHl AAAASIT/k8bpk8bpk8bpk8bpk8bpAAAAAAAAAAAAk8bpk8bpk8bpAAAASIT/k8bpk8bpk8bpk8Xn k8bpk8bpnMfjk8Xnk8bpk8bpk8Xnk8bpk8bpk8bpSIT/AAAAk8bpk8bpk8bpAAAAAAAAAAAAk8bp k8bpk8bpk8bpk8bpSIT/AAAAk8bpjsHljsHljsHlSIT/SIT/AAAAAP//AAAAAAAASIT/SIT/jsHl jsHljsHlk8bpAAAASIT/k8bpk8bpk8bpk8bpk8bpAAAAAAAAk8bpk8bpk8bpk8bpAAAASIT/k8bp k8bpk8Xnk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8Xnk8bpk8bpSIT/AAAAk8bpk8bpk8bpk8bp AAAAAAAAk8bpk8bpk8bpk8bpk8bpSIT/AAAAlcjrk8bpjsHlk8bpk8bpSIT/AAAAAP//AAAAAAAA SIT/k8bpk8bpjsHlk8bplcjrAAAASIT/k8bpk8bpk8bpk8bpk8bpAAAAAAAAk8bpk8bpk8bpk8bp AAAAAAAAk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpAAAAAAAAk8bp k8bpk8bpk8bpAAAAAAAAk8bpk8bpAAAAAAAAAAAAAAAAAAAAk8bpjsHljsHljsHljsHlSIT/SIT/ AAAAAAAAAAAASIT/jsHljsHljsHljsHlk8bpAAAAAAAAAAAAAAAAAAAAk8bpk8bpk8bpk8bpk8bp k8bpk8bpAAAAwMDAAAAAAAAAAAAAk8bpk8bpnMnok8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpAAAA AAAAwMDAAAAAk8bpk8bpk8bpAAAAk8bpk8bpAAAAACNGACNGACNGACNGAAAAjsHljsHljsHlk8bp k8XnSIT/SIT/AAAAAAAAAAAASIT/k8Xnk8bpjsHljsHljsHlAAAAACNGACNGACNGACNGAAAAk8bp k8bpnMnolcjrk8bpAAAAwMDAwMDA////AAAAgAAAAAAAk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bp k8bpAAAAAACAAAAAwMDAwMDAAAAAk8bpk8bpk8bpk8bpAAAAHjZhHjZhHjZhHjZhACNGAAAAAAAA jsHljsHljsHljsHlSIT/AAAAAP//AAAAAAAASIT/jsHljsHljsHljsHlAAAAAAAAACNGHjZhHjZh HjZhHjZhAAAAk8bplcjrnMnoAAAAwMDAwMDA////////AAAAgAAAgAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAACAAACAAAAA////wMDAwMDAAAAAk8bpk8bpk8bpAAAAAAAAHjZhHjZhHjZh ACNGAAAASIT/AAAAAAAAAAAASIT/SIT/AAAAAP//AAAAAAAASIT/SIT/AAAAAAAAAAAASIT/AAAA ACNGHjZhHjZhHjZhAAAAAAAAk8bplcjrlcjrAAAAwMDA////////AAAAgAAAAAAAAAAAAAAAAAAA k8bpk8bpk8bpk8bpk8bpAAAAAAAAAAAAAAAAAACAAAAA////wMDAAAAAk8bpk8bpk8bpk8bpAAAA HjZhHjZhHjZhACNGAAAASIT/SIT/jsHljsHlSIT/AAAAAP//AP//AAAAAAAAAAAASIT/jsHljsHl SIT/SIT/AAAAACNGHjZhHjZhHjZhAAAAk8bpk8bpk8bpjsHlAAAAwMDA////////AAAA/wAAAAAA AAAAAAAAAAAAk8bpk8bpk8bpk8bpk8bpAAAAAAAAAAAAAAAAAAD/AAAA////wMDAAAAAk8bplcjr k8bpk8bpk8bpAAAAAAAAHjZhACNGAAAAAAAASIT/SIT/SIT/SIT/AAAAAP//AP//AAAAAAAAAAAA SIT/SIT/SIT/SIT/AAAAAAAAACNGHjZhAAAAAAAAk8bpk8bpk8bpk8bpjsHlAAAAwMDA//////// AAAA/wAAAAAAAAAAAAAAAAAAk8bpk8bpk8bpk8bpk8bpAAAAAAAAAAAAAAAAAAD/AAAA////wMDA AAAAk8bplcjrk8bpk8bpk8bpk8bpAAAAHjZhACNGAAAAAAAAAAAASIT/SIT/AAAAAP//AP//AP// AAAAAAAAAP//AAAASIT/SIT/AAAAAAAAAAAAACNGHjZhAAAAk8bpk8bpk8bpk8bpk8bpjsHlAAAA wMDA////////AAAA/wAAAAAAAAAAAAAAAAAAk8bpk8bpk8bpk8bpk8bpAAAAAAAAAAAAAAAAAAD/ AAAA////wMDAAAAAk8bplcjrlcjricPqAAAAAAAAAAAAAAAAACNGAAAAgIAAgIAAAAAAFhsmAAAA AP//AP//AP//AAAAAAAAAP//AAAAFhsmAAAAgIAAgIAAAAAAACNGAAAAAAAAAAAAAAAAicPqk8bp k8bpk8XnAAAAwMDAwMDA////AAAA/wAA/wAA/wAA/wAAAAAApMvmk8bpk8bpk8bpk8bpAAAAAAD/ AAD/AAD/AAD/AAAAwMDAwMDAAAAAk8bpk8XnlcjrAAAAHjZhHjZhHjZhAAAAAAAAAAAAgIAAgIAA gIAAgIAAgIAAAAAAAP//AP//AAAAAAAAAAAAgIAAgIAAgIAAgIAAgIAAAAAAAAAAAAAAHjZhHjZh HjZhAAAAAAAAlcjrlcjrk8bpAAAAwMDA////////AAAA/wAA/wAA/wAAAAAAk8bpk8bpk8bpk8bp k8bpAAAAAAD/AAD/AAD/AAAA////wMDAAAAAk8bplcjrlcjrAAAAHjZhAAAAAAAAAAAAgIAAgIAA AAAAgIAA//8A//8A//8AgIAAgIAAAAAAAP//AAAAAAAAgIAAgIAA//8A//8A//8AgIAAAAAAgIAA gIAAAAAAAAAAAAAAHjZhHjZhAAAAlcjrk8bpAAAAwMDAwMDA////////AAAAAAAAAAAAk8bpk8bp AAAAk8bpAAAAk8bpAAAAAAAAAAAAAAAA////wMDAwMDAAAAAk8bplcjrAAAAHjZhAAAAgIAAgIAA gIAAgIAAgIAAAAAAgIAA//8A//8A//8A//8AgIAAgIAAAAAAAAAAAAAAgIAA//8A//8A//8A//8A gIAAAAAAgIAAgIAAgIAAgIAAgIAAAAAAAAAAHjZhAAAAlcjrk8bpAAAAwMDAwMDAwMDAwMDAAAAA k8bplcjrAAAAAAAAk8bpAAAAAAAAk8bpAAAAwMDAwMDAwMDAwMDAAAAAk8bpk8bpAAAAHjZhAAAA gIAAgIAA//8A//8AgIAAAAAAgIAAgIAA//8A//8A//8A//8A//8AgIAAgIAAAAAAAAAA//8A//8A //8A//8A//8AgIAAgIAAAAAAgIAA//8A//8AgIAAgIAAgIAAAAAAHjZhAAAAAAAAAAAAAAAAAAAA AAAAAAAAk8bpk8bpAAAAAAAAk8bpk8bpAAAAHjZhAAAAk8bpAAAAAAAAAAAAAAAAk8bplcjrAAAA HjZhAAAAgIAAgIAA//8A//8AgIAAgIAAAAAAgIAA//8A//8A//8A//8A//8A//8A//8AgIAAAAAA AAAA//8A//8A//8A//8A//8A//8AgIAAAAAAgIAAgIAA//8A//8AgIAAgIAAgIAAAAAAHjZhHjZh AAAAk8bpk8bpk8bpk8bplcjrAAAAAAAAk8bpk8bpk8bpk8XnAAAAHjZhAAAAk8bpk8bpk8bpk8bp k8bpAAAAHjZhAAAAgIAAgIAA//8A//8AgIAAgIAAAAAAAAAAgIAA//8A//8A//8A//8A//8A//8A //8A//8AAAAAAAAA//8A//8A//8A//8A//8A//8AgIAAAAAAAAAAgIAAgIAA//8A//8A//8AgIAA gIAAAAAAAAAAHjZhAAAAk8bpk8bpk8bpAAAAHjZhAAAAk8Xnlcjrk8bpk8bpAAAAHjZhHjZhAAAA k8bpk8bpk8bpAAAAHjZhAAAAgIAAgIAA//8A//8AgIAAgIAAAAAAgIAAgIAAgIAA//8A//8A//8A //8A//8A//8A//8A//8AAAAAAAAA//8A//8A//8A//8A//8A//8AgIAAgIAAgIAAAAAAgIAAgIAA //8A//8A//8AgIAAgIAAgIAAAAAAHjZhAAAAAAAAAAAAHjZhAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAHjZhHjZhAAAAAAAAAAAAHjZhAAAAgIAAgIAA//8AgIAAgIAAgIAAAAAAgIAAgIAA//8A//8A //8A//8A//8A//8A//8A//8A//8A//8AAAAAAAAA//8A//8A//8A//8A//8A//8A//8A//8AgIAA gIAAAAAAgIAAgIAAgIAA//8A//8A//8AgIAAgIAAAAAAAAAAAAAAHjZhHjZhAAAAHjZhHjZhHjZh HjZhHjZhHjZhAAAAHjZhAAAAHjZhHjZhAAAAAAAAgIAAgIAA//8AgIAAgIAAAAAAAAAAgIAAgIAA //8A//8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAAAAAAAAA//8A//8A//8A//8A//8A//8A //8A//8A//8AgIAAgIAAAAAAAAAAgIAAgIAAgIAA//8A//8AgIAAgIAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgIAAgIAA//8AgIAAgIAAAAAAgIAA gIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAAAAAAAAAAP//AP//AAAA gIAAgIAA//8A//8A//8A//8A//8A//8AgIAAgIAAAAAAAAAAgIAAgIAAgIAA//8AgIAAgIAAgIAA gIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAA//8AgIAAgIAA AAAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAAAAAAAAA AP//AP//AAAAgIAA//8A//8A//8A//8A//8A//8A//8A//8AgIAAgIAAgIAAAAAAAAAAgIAAgIAA //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AgIAA gIAAgIAAAAAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A gIAAAAAAAAAAAP//AAAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAgIAA gIAAAAAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAA gIAAgIAAgIAAAAAAAAAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8AgIAAAAAAAAAAAP//AAAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8AgIAAgIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAgIAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8AgIAAAAAAAAAAAAAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8AgIAAgIAAgIAAgIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAgIAAgIAAgIAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8AAAAAAAAAgIAAgIAA//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AAAAAwMDAwMDAwMDAwMDAwMDA wMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAAAAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AAAAAAAAAgIAA//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AAAAAwMDAwMDA//// ////////gIAAgIAAgIAAgIAAgIAAgIAA////////wMDAwMDAAAAA//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AAAAAAAAAgIAA //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AAAAA wMDA////////////////////gIAA////////////////////////////wMDAwMDAAAAA//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A AAAAAAAA//8A//8A//8A//8A//8AgIAAgIAAgIAAgIAAgIAAgIAA//8A//8A//8A//8A//8A//8A //8A//8AAAAAwMDA//////////////////8A//8AgIAAgIAAgIAA////////////////////wMDA AAAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAgIAAgIAAgIAA//8A //8A//8A//8AAAAAAAAAgIAAgIAAgIAAgIAAgIAAgIAAAAAAAAAAAAAAAAAAgIAA//8A//8A//8A //8A//8A//8A//8A//8AAAAAwMDAwMDA//////////////////////////8A//////////////// ////wMDAwMDAAAAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAAAAA AAAAgIAAgIAAgIAAgIAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AP//AP//AAAAgIAA //8A//8A//8A//8A//8A//8A//8A//8A//8AAAAAwMDAwMDA//////////////////////////8A ////////////wMDAwMDAAAAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A gIAAgIAAAAAAAP//AAAAAAAAAAAAgIAAgIAAAAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP// AAAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AAAAAwMDAwMDA//////8A//8A //8A//8A//8A//8A////wMDAwMDAAAAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8AgIAAAAAAAP//AP//AP//AP//AP//AAAAAAAAAAAAAAAAAP//AP//AP//AP//AP// AP//AP//AP//AAAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AAAAAwMDA wMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAAAAA//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8AgIAAAAAAAP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP// AP//AP//AP//AP//AP//AP//AAAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAAAAAAP//AP//AP//AP//AP//AP//AP//AAAA AAAAAP//AP//AP//AP//AP//AP//AP//AAAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAgIAAAAAAAP//AP//AP//AP//AP// AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AAAAgIAA//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8AgIAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8AgIAAAAAAAP//AP//AP// AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AAAAgIAA//8A//8A//8A //8A//8A//8A//8AgIAAgIAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8AgIAAgIAAAAAAgIAAgIAA//8A//8A//8A//8A//8A//8AgIAAAAAA AP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AAAAgIAA //8A//8A//8A//8A//8AgIAAgIAAgIAAAAAAAAAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAAAAAAP//AAAAgIAAgIAA//8A//8A//8A//8A //8AgIAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP// AAAAgIAAgIAA//8A//8AgIAAgIAAgIAAgIAAAAAAAAAAAP//AAAAgIAA//8A//8A//8A//8A//8A //8AgIAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8AgIAAgIAAAAAAAP//AP//AAAAgIAAgIAA //8A//8A//8AgIAAgIAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP// AP//AP//AP//AAAAgIAAgIAAgIAAgIAAgIAAAAAAAAAAAAAAAP//AP//AP//AAAAgIAA//8A//8A //8A//8A//8AgIAAgIAAAAAAgIAAgIAA//8A//8A//8A//8A//8A//8AgIAAAAAAAP//AP//AP// AP//AAAAgIAAgIAA//8A//8AgIAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAA AP//AP//AP//AP//AP//AP//AAAAgIAAgIAAAAAAAAAAAAAAAP//AP//AP//AP//AP//AP//AAAA gIAA//8A//8A//8A//8AgIAAgIAAAAAAAP//AAAAgIAA//8A//8A//8A//8A//8AgIAAgIAAAAAA AP//AP//AP//AP//AP//AAAAgIAAgIAA//8AgIAAAAAAAP//AP//AP//AP//AP//AP//AP//AP// AP//AAAAAAAAAP//AP//AP//AP//AP//AAAAgIAAAAAAAAAAAP//AP//AP//AP//AP//AP//AP// AP//AP//AAAAgIAA//8A//8A//8AgIAAgIAAAAAAAP//AP//AAAAgIAAgIAA//8A//8A//8A//8A gIAAAAAAAP//AP//AP//AP//AP//AP//AP//AAAAgIAAgIAAgIAAAAAAAP//AP//AP//AP//AP// AP//AP//AP//AP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    }

    /**
     * Convert a string in the format Base64 into a bitmap at the format Base64
     *
     * @param encodedString the string to transform
     * @return a bitmap representation of the string
     */
    public static Bitmap stringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    /**
     * Easy way to print a event in a log
     * Not equivalent to serialized event (RestApi.Serializer) which provide string event acceptable
     * for the server
     */
    public String toString() {
        return this.getTitle() + ", " + this.getDescription() + ", " + this.getAddress()
                + ", (" + Double.toString(this.getLatitude()) + ", " + Double.toString(this.getLongitude())
                + "), " + this.getOwner() + ", (" + this.getProperDateString();
    }

    /**
     * The number maximum of user joined in the event
     *
     * @return the maximum number of user
     */
    public int getMaxNumberOfParticipant() {
        return mNumberMaxOfParticipants;
    }

    /**
     * Return a string formatted with norm ISO8601
     *
     * @return a string formatted for the server
     */
    public String getProperDateString() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.FRANCE);
        timeFormat.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
        return timeFormat.format(mStartDate.getTime());
    }

    /**
     * The starting date as string by using the asNiceString method
     *
     * @return the starting date as a nice string
     */
    public String getStartDateAsString() {
        return asNiceString(mStartDate);
    }

    /**
     * The ending date as string by using the asNiceString method
     *
     * @return the ending date as a nice string
     */
    public String getEndDateAsString() {
        return asNiceString(mEndDate);
    }

    /**
     * Getter of the unique ID
     *
     * @return the ID
     */
    public int getID() {
        return mID;
    }

    /**
     * Getter of the title
     *
     * @return the title
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Getter of the description
     *
     * @return the description
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Getter of the latitude where the event take place
     *
     * @return the latitude as a double
     */
    public double getLatitude() {
        return mLocation.latitude;
    }

    /**
     * Getter of the longitude where the event take place
     *
     * @return the longitude as a double
     */
    public double getLongitude() {
        return mLocation.longitude;
    }

    /**
     * Getter of the address
     *
     * @return the address
     */
    public String getAddress() {
        return mAddress;
    }

    /**
     * Getter of the unique Id of the owner of this event
     *
     * @return the Id of the owner
     */
    public int getOwner() {
        return mOwnerId;
    }

    /**
     * Getter of the tags as a string
     *
     * @return the tags as string
     */
    public String getTagsString() {
        String res = " ";
        for (String str : mTags) {
            res = str + ";";
        }
        return res;
    }

    /**
     * Getter of the tags
     *
     * @return the tags
     */
    public Set<String> getTags() {
        return mTags;
    }

    /**
     * Getter of the begging of the event
     *
     * @return the start date as a Calendar
     */
    public Calendar getStartDate() {
        return mStartDate;
    }

    /**
     * Getter of the ending of the event
     *
     * @return the end date as a Calendar
     */
    public Calendar getEndDate() {
        return mEndDate;
    }

    /**
     * Getter of the picture as a string
     *
     * @return the picture
     */
    public String getPictureAsString() {
        return mPicture;
    }

    /**
     * Converts the String member named mPicture that represents a Bitmap image encoded in base64
     * into an actual Bitmap.
     *
     * @return The Bitmap converted from mPicture
     */
    public Bitmap getPicture() {

        return stringToBitMap(mPicture);
    }

    /**
     * Getter of the position as a LatLng
     * needed by the ClusterItem interface
     *
     * @return the position as a LatLng
     */
    @Override
    public LatLng getPosition() {
        return mLocation;
    }

    /**
     * Compare the events. Two events are equals only if there Id are the same
     *
     * @param object the object to compare
     * @return true if it's the same, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Event) {
            Event event = (Event) object;
            return mID == event.mID;
        }
        return false;
    }

    /**
     * Generate an hash code base only on the event id
     *
     * @return an hash code
     */
    @Override
    public int hashCode() {
        return mID;
    }


}


