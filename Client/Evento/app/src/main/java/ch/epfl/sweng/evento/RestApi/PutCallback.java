package ch.epfl.sweng.evento.RestApi;

/**
 * Created by cerschae on 15/10/2015.
 */
/**
 *
 * Class definition for a callback to be invoked when the response for the data
 * submission is available.
 *
 */
public abstract class PutCallback {
    /**
     * Called when a POST success response is received. <br/>
     * This method is guaranteed to execute on the UI thread.
     */
    public abstract void onPostSuccess();

}