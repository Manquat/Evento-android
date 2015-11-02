package ch.epfl.sweng.evento.RestApi;

/**
 * Created by cerschae on 15/10/2015.
 */

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import ch.epfl.sweng.evento.NetworkProvider;

/**
 * An AsyncTask implementation for performing POSTs.
 */
public class PostTask extends AsyncTask<String, String, String> {
    private static final String charset = "UTF-8";
    private static final int HTTP_SUCCESS_START = 200;
    private static final int HTTP_SUCCESS_END = 299;
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
        String response = null;
        try {
//            String bodyToSend = URLEncoder.encode(requestBody, charset);
//            URL url = new URL(restUrl);
//            HttpURLConnection conn = networkProvider.getConnection(url);
//            conn.setRequestMethod("POST");
//            //conn.setDoInput(true);
//            conn.setDoOutput(true);
//            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            //String bodyToSend = URLEncoder.encode(requestBody, charset);
            String urlParameters = requestBody;
//            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            String postData = urlParameters;
            int postDataLength = postData.length();
            URL url = new URL(restUrl);
            HttpURLConnection conn = networkProvider.getConnection(url);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
//            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
//                wr.write(postData);
//            }
            try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
                wr.write(postData);
            }
            int responseCode = 0;
            responseCode = conn.getResponseCode();
            if (responseCode < HTTP_SUCCESS_START || responseCode > HTTP_SUCCESS_END) {
                throw new RestException("Invalid HTTP response code");
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RestException e) {
            e.printStackTrace();
        }
        return response;
    }

        @Override
    protected void onPostExecute(String result) {
        callback.onTaskComplete(result);
        super.onPostExecute(result);
    }
}