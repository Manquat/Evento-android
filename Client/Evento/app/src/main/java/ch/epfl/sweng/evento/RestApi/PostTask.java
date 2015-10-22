package ch.epfl.sweng.evento.RestApi;

/**
 * Created by cerschae on 15/10/2015.
 */

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ch.epfl.sweng.evento.NetworkProvider;

/**
 * An AsyncTask implementation for performing POSTs.
 */
public class PostTask extends AsyncTask<String, String, String> {
    private final NetworkProvider networkProvider;
    private String restUrl;
    private RestTaskCallback callback;
    private String requestBody;

    /**
     * Creates a new instance of PostTask with the specified URL, callback, and
     * request body.
     *
     * @param networkProvider
     * @param restUrl The URL for the REST API.
     * @param requestBody The body of the POST request.
     * @param callback The callback to be invoked when the HTTP request
*            completes.
     *
     */
    public PostTask(String restUrl, NetworkProvider networkProvider, String requestBody, RestTaskCallback callback){
        this.networkProvider = networkProvider;
        this.restUrl = restUrl;
        this.requestBody = requestBody;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... arg0) {
        URL url = null;
        String response = null;
        try {
            url = new URL(restUrl);


            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            HttpURLConnection conn = networkProvider.getConnection(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        callback.onTaskComplete(result);
        super.onPostExecute(result);
    }
}