package com.batistiana.bj.httprequestbj;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by bjbatistiana on 1/19/16.
 * Module for making Http Requests.
 * <p/>
 * This module requires a URL for requisition.
 * <p/>
 * This module defaults to a GET request when unspecified.
 * Call setMethod() to set it to a different
 * one.
 * This module can only handle GET, POST, PUT, and DELETE.
 * If an invalid method was set, the module will automatically set the method to GET.
 * <p/>
 * Calling execute() creates a background thread where the request will fire.
 * <p/>
 * Calling run() fires the request on wherever it was called. Please handle the exceptions
 * concerned.
 * <p/>
 * Two interfaces were created for the pre and post processes that execute() will process
 * respectively.
 * <p/>
 * Increase or decrease the timeout duration if necessary using setTimeout().
 * You can customize your timeout message using setTimeoutMessage().
 * <p/>
 * You can insert multiple request headers via putRequestHeader().
 */
public class HttpRequest {

    private ContentValues requestHeaders;
    private String parameter = "";
    private String url;
    private int responseCode = 0;
    private String responseString;
    private String timeoutMessage = "Connection timed out";
    private Response response;

    private String method;
    private int timeout = 10000;

    private AsyncPostExecuteInterface postExecute;
    private AsyncPreExecuteInterface preExecute;

    private boolean isSSL = false;

    /**
     * Request Methods
     */
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    /**
     * Constructor that initializes request method and assigns the base url.
     *
     * @param url    is the url to be used when requesting.
     * @param method is the method that will be used to request.
     */
    public HttpRequest(String url, String method) {

        this.url = url;
        this.method = method;
    }

    public HttpRequest(String url) {

        this.url = url;
        this.method = GET;
    }

    /**
     * This method executes the request in another thread. Filters method via a switch.
     * This method contains an AsyncTask that has a post and pre execute.
     * This method handles timeout and malformed url exceptions.
     */
    public void execute() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (preExecute != null) {
                    preExecute.OnPreExecute();
                }
            }

            @Override
            protected Void doInBackground(Void... val) {
                try {
                    switch (method) {
                        case GET:
                        case DELETE:
                            response = GET(url, requestHeaders, isSSL);
                            break;
                        case POST:
                        case PUT:
                            response = POST(url, parameter, requestHeaders, isSSL);
                            break;
                    }
                } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {

                    if (e instanceof SocketTimeoutException)
                        responseString = timeoutMessage;
                    else if (e instanceof MalformedURLException)
                        responseString = "Oops, something went wrong. Please contact the administrator for additional details. E#002";
                    else
                        responseString = "Oops, something went wrong. Please contact the administrator for additional details. E#001";
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (postExecute != null) {
                    postExecute.OnPostExecute();
                }
            }
        }.execute();
    }

    public Response run() {

        Response response = new Response();

        if (preExecute != null){
            preExecute.OnPreExecute();
        }

        try {
            switch (method) {
                case GET:
                case DELETE:
                    response = GET(url, requestHeaders, isSSL);
                    break;
                case POST:
                case PUT:
                    response = POST(url, parameter, requestHeaders, isSSL);
                    break;
            }
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {

            if (e instanceof SocketTimeoutException)
                responseString = timeoutMessage;
            else if (e instanceof MalformedURLException)
                responseString = "Oops, something went wrong. Please contact the administrator for additional details. E#002";
            else
                responseString = "Oops, something went wrong. Please contact the administrator for additional details. E#001";

            response.response = responseString;
            response.statusCode = responseCode;
        }

        if (postExecute != null) {
            postExecute.OnPostExecute();
        }
        return response;
    }

    /**
     * Setters
     */

    public void setSSL(boolean isSSL) {
        this.isSSL = isSSL;
    }

    public void setParameter(Object parameter) {
        if (parameter != null)
            this.parameter = (String) parameter;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setMethod(String method) {

        switch (method) {

            case GET:
            case POST:
            case PUT:
            case DELETE:
                this.method = method;
                break;
            default:
                this.method = GET;
                break;
        }
    }

    public void setPostExecute(AsyncPostExecuteInterface postExecute) {
        this.postExecute = postExecute;
    }

    public void setPreExecute(AsyncPreExecuteInterface preExecute) {
        this.preExecute = preExecute;
    }

    public void setTimeoutMessage(String message) {
        this.timeoutMessage = message;
    }


    public void putRequestHeader(String name, String value) {

        if (requestHeaders == null) {
            requestHeaders = new ContentValues();
        }
        requestHeaders.put(name, value);
    }

    /**
     * Getters
     */

    public int getResponseCode() {
        return responseCode;
    }

    public Response getResponse() {
        return response;
    }

    /**
     * Method that requests from the http using GET Method.
     *
     * @param url            is the url of the http, including the request file.
     * @param requestHeaders contains the headers of the request
     * @param isSSL          checks if the request is in a form of ssl
     * @return is a Response object that contains the status code and the response string.
     */
    public Response GET(String url, ContentValues requestHeaders, boolean isSSL) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        Log.e("Base", "URL: " + url);
        Log.e("Base", "Request Type: " + method);
        Response result = new Response();

        if (isSSL) {
            HttpsURLConnection conn = (HttpsURLConnection)
                    new URL(url)
                            .openConnection();
            conn.setSSLSocketFactory(new TLSSocketFactory());
            conn.setRequestMethod(method);

            if (requestHeaders != null) {
                Set<Map.Entry<String, Object>> requestHeadersSet = requestHeaders.valueSet();

                for (Object aRequestHeadersSet : requestHeadersSet) {
                    Map.Entry entry = (Map.Entry) aRequestHeadersSet;
                    String key = entry.getKey().toString();
                    String value = (String) entry.getValue();

                    conn.setRequestProperty(key, value);

                }
            }

            result.statusCode = conn.getResponseCode();

            if (result.statusCode == 200){
                StringBuilder response = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;

                while ((line = br.readLine()) != null) {
                    response.append(line).append("\n");
                }

                br.close();

                result.response = response.toString();
            } else
                result.response = conn.getResponseMessage();

        } else {
            HttpURLConnection conn = (HttpURLConnection)
                    new URL(url)
                            .openConnection();
            conn.setRequestMethod(method);

            if (requestHeaders != null) {
                Set<Map.Entry<String, Object>> requestHeadersSet = requestHeaders.valueSet();

                for (Object aRequestHeadersSet : requestHeadersSet) {
                    Map.Entry entry = (Map.Entry) aRequestHeadersSet;
                    String key = entry.getKey().toString();
                    String value = (String) entry.getValue();

                    conn.setRequestProperty(key, value);

                }
            }

            result.statusCode = conn.getResponseCode();

            if (result.statusCode == 200){
                StringBuilder response = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;

                while ((line = br.readLine()) != null) {
                    response.append(line).append("\n");
                }

                br.close();

                result.response = response.toString();
            } else
                result.response = conn.getResponseMessage();

        }

        return result;
    }

    /**
     * Method that sends an http request to a given url. Also takes parameters and request headers.
     *
     * @param url            is the url where the request will be sent.
     * @param obj            is the parameters that will be sent.
     * @param requestHeaders is/are the header/s that will be used.
     * @param isSSL          checks if the request is in a form of ssl
     * @return is a Request object with status code and string response.
     */
    public Response POST(String url, String obj, ContentValues requestHeaders, boolean isSSL) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        Log.e("Base", "URL: " + url);
        Log.e("Base", "Request Type: " + method);
        Log.e("Base", "Params: " + obj);
        Response result = new Response();

        if (isSSL) {
            HttpsURLConnection conn = (HttpsURLConnection)
                    new URL(url)
                            .openConnection();
            conn.setSSLSocketFactory(new TLSSocketFactory());
            conn.setRequestMethod(method);

            if (requestHeaders != null) {
                Set<Map.Entry<String, Object>> requestHeadersSet = requestHeaders.valueSet();

                for (Object aRequestHeadersSet : requestHeadersSet) {
                    Map.Entry entry = (Map.Entry) aRequestHeadersSet;
                    String key = entry.getKey().toString();
                    String value = (String) entry.getValue();

                    conn.setRequestProperty(key, value);

                }
            }

            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(obj);
            wr.flush();
            wr.close();

            result.statusCode = conn.getResponseCode();

            if (result.statusCode == 200){
                StringBuilder response = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;

                while ((line = br.readLine()) != null) {
                    response.append(line).append("\n");
                }

                br.close();

                result.response = response.toString();
            } else
                result.response = conn.getResponseMessage();
        } else {
            HttpURLConnection conn = (HttpURLConnection)
                    new URL(url)
                            .openConnection();
            conn.setRequestMethod(method);

            if (requestHeaders != null) {
                Set<Map.Entry<String, Object>> requestHeadersSet = requestHeaders.valueSet();

                for (Object aRequestHeadersSet : requestHeadersSet) {
                    Map.Entry entry = (Map.Entry) aRequestHeadersSet;
                    String key = entry.getKey().toString();
                    String value = (String) entry.getValue();

                    conn.setRequestProperty(key, value);

                }
            }

            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(obj);
            wr.flush();
            wr.close();

            result.statusCode = conn.getResponseCode();

            if (result.statusCode == 200){
                StringBuilder response = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;

                while ((line = br.readLine()) != null) {
                    response.append(line).append("\n");
                }

                br.close();

                result.response = response.toString();
            } else
                result.response = conn.getResponseMessage();
        }

        return result;
    }

    /**
     * Interfaces
     */

    public interface AsyncPostExecuteInterface {
        void OnPostExecute();
    }

    public interface AsyncPreExecuteInterface {
        void OnPreExecute();
    }


}
