package ch.epfl.sweng.evento.rest_api.callback;

/**
 * Created by cerschae on 15/10/2015.
 */

/**
 * Class definition for a callback to be invoked when the response for the data
 * submission is available.
 */
public interface HttpResponseCodeCallback {
    /**
     * Called when a POST success response is received. <br/>
     * This method is guaranteed to execute on the UI thread.
     *
     * @param httpResponseCode
     */
    void onSuccess(String httpResponseCode);

}