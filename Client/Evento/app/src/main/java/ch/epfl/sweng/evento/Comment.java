package ch.epfl.sweng.evento;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A comment about an Event made by one user
 */
public class Comment {
    private int mID;
    private String mMessage;
    private MockUser mOwner;
    private final Calendar mDateOfCreation;

    public Comment(MockUser owner, String message) {
        mOwner = owner;
        mMessage = message;
        mDateOfCreation = new GregorianCalendar();
    }

    public String getMessage(){
        return mMessage;
    }

    public MockUser getOwner() {
        return mOwner;
    }

    public int getID() {
        return mID;
    }

    public Calendar getTimeOfCreation() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(mDateOfCreation.getTime());
        return calendar;
    }
}
