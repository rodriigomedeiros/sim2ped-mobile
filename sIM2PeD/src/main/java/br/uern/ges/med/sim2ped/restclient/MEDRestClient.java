package br.uern.ges.med.sim2ped.restclient;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;


import br.uern.ges.med.sim2ped.utils.Constant;

/**
 * Created by Rodrigo on 04/09/2014 (10:30).
 *
 */

public class MEDRestClient{
    private static SyncHttpClient client = new SyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        //client.post(getAbsoluteUrl(url), params, responseHandler);
        client.addHeader("Content-Type:", "application/json");
        client.addHeader("Accept", "application/json");
        //client.post(context, getAbsoluteUrl(url), stringEntity, "application/json", responseHandler);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        client.setBasicAuth(Constant.WEBSERVICE_LOGIN, Constant.WEBSERVICE_PASSWORD);
        return Constant.WEBSERVICE_URL + relativeUrl + Constant.WEBSERVICE_RESPONSE_FORMAT;
    }
}
