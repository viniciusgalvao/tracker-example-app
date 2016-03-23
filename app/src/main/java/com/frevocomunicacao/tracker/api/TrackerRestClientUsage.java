package com.frevocomunicacao.tracker.api;

import com.frevocomunicacao.tracker.Constants;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TrackerRestClientUsage {

    public static void login(RequestParams params, JsonHttpResponseHandler responseHandler) {
        TrackerRestClient.post(Constants.ENDPOINT_LOGIN, params, responseHandler);
    }

    public static void importData(RequestParams params, JsonHttpResponseHandler responseHandler) {
        TrackerRestClient.post(Constants.ENDPOINT_IMPORT_DATA, params, responseHandler);
    }
}
