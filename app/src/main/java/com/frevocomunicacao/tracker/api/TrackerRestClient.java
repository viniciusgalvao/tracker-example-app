package com.frevocomunicacao.tracker.api;

import com.loopj.android.http.*;

/**
 * Created by Vinicius Galvão on 29/01/16.
 */
public class TrackerRestClient {

    private static final String BASE_URL    = "192.168.25.55/";
    private static AsyncHttpClient client   = new AsyncHttpClient();

    public static void get(String endpoint, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(endpoint), params, responseHandler);
    }

    public static void put(String endpoint, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.put(getAbsoluteUrl(endpoint), params, responseHandler);
    }

    public static void post(String endpoint, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(endpoint), params, responseHandler);
    }

    private static String getAbsoluteUrl(String endpoint) {
        return BASE_URL + endpoint;
    }

}
