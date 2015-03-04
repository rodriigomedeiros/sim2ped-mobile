package br.uern.ges.med.sim2ped.managers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import br.uern.ges.med.sim2ped.MainActivity;
import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.beans.HistoryResponse;
import br.uern.ges.med.sim2ped.beans.StatisticalUsage;
import br.uern.ges.med.sim2ped.model.HistoryResponsesModel;
import br.uern.ges.med.sim2ped.model.StatisticsModel;
import br.uern.ges.med.sim2ped.preferences.Preferences;
import br.uern.ges.med.sim2ped.restclient.MEDRestClient;
import br.uern.ges.med.sim2ped.utils.Constant;

/**
 * Created by Rodrigo on 03/09/2014 (17:53).
 * Package: br.uern.ges.med.sim2ped.managers
 */
public class SendDataTask extends AsyncTask<String, String, Void>{

    private Context context;
    private final SendHistoryTaskListener listener;
    private int nHistoryUpdatedInServer = 0, nStatisticsUpdateInServer = 0;
    private HistoryResponsesModel historyResponsesModel;
    private ArrayList<HistoryResponse> historyResponses;
    private ArrayList<StatisticalUsage> statisticalUsages;
    private StatisticsModel statisticsModel;
   // private MainActivity mainActivity;

    public SendDataTask(MainActivity activity, SendHistoryTaskListener listener, Context context) {
        this.context = context;
        this.listener = listener;
     //   this.mainActivity = activity;
    }

    @Override
    protected void onPreExecute(){
        listener.onSendHistoryStarted();
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            postHistory();
            postStatistics();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onProgressUpdate(String... text){
        listener.progressSendHistoryUpdate(text[0]);
        Log.d(Constant.TAG_LOG, "SendHistory progress is: "+text[0]+".");
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);

        boolean historySent;
        boolean staticalUsageSent;

        historySent = historyResponses.size() == nHistoryUpdatedInServer;

        staticalUsageSent = statisticalUsages.size() == nStatisticsUpdateInServer;

        Log.e(Constant.TAG_LOG, "History Sent: " + String.valueOf(historySent) + "   " + historyResponses.size() + "    " + nHistoryUpdatedInServer);
        Log.e(Constant.TAG_LOG, "Statical Sent: " + String.valueOf(staticalUsageSent) + "   " + statisticalUsages.size() + "    " + nStatisticsUpdateInServer);

        if(historySent && staticalUsageSent && historyResponses.size() > 0 && statisticalUsages.size() > 0){
            listener.onSendHistoryFinished(context.getString(R.string.msg_all_data_sent));
        } else if(historySent && historyResponses.size() > 0 && statisticalUsages.size() == 0) {
            listener.onSendHistoryFinished(context.getString(R.string.msg_all_data_sent));
        } else if(staticalUsageSent && statisticalUsages.size() > 0 && historyResponses.size() == 0){
            listener.onSendHistoryFinished(context.getString(R.string.msg_all_data_sent));
        } else if((historySent && !staticalUsageSent) || (!historySent && staticalUsageSent)){
            listener.onSendHistoryFinished(context.getString(R.string.msg_some_data_no_sent));
        } else if(historyResponses.size() == 0 && statisticalUsages.size() == 0){
            listener.onSendHistoryFinished(context.getString(R.string.msg_you_do_not_have_data_send));
        }


        /*
        if(((historyResponses.size() == nHistoryUpdatedInServer) && historyResponses.size() > 0)
            &&
          ((statisticalUsages.size() == nStatisticsUpdateInServer) && statisticalUsages.size() > 0)){

            listener.onSendHistoryFinished(context.getString(R.string.msg_all_data_sent));

        } else if(((historyResponses.size() != nHistoryUpdatedInServer) && historyResponses.size() > 0)
                ||
                ((statisticalUsages.size() != nStatisticsUpdateInServer) && statisticalUsages.size() > 0)) {

            listener.onSendHistoryFinished(context.getString(R.string.msg_some_data_no_sent));

        } else if() {

        } else if(statisticalUsages.size() == 0 && historyResponses.size() == 0) {
            listener.onSendHistoryFinished(context.getString(R.string.msg_you_do_not_have_data_send));
        } else {
            listener.onSendHistoryFinished(context.getString(R.string.msg_nothing_has_been_done));
        }
        */
    }

    private synchronized void postHistory() throws UnsupportedEncodingException {

        Preferences preferences = new Preferences(context);

        historyResponsesModel = new HistoryResponsesModel(context);
        historyResponses = historyResponsesModel.
                getAllHistoryResponsesNotSentToServer(preferences.getUserCodeLogged());

        for(int i = 0; i < historyResponses.size(); i++) {
            publishProgress(context.getString(R.string.msg_sending_history_response_n_to_n, i+1,
                    historyResponses.size()));

            String jsonHistory = new Gson().toJson(historyResponses.get(i));

            RequestParams params = new RequestParams();

            params.put(Constant.JSON_OBJECT_TAG_HISTORY, jsonHistory);

            MEDRestClient.post(context, Constant.WEBSERVICE_RELATIVE_URL_POST_HISTORY, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    Log.d(Constant.TAG_LOG, "onSuccess");

                    try {
                        if(response.getString(Constant.JSON_MESSAGE_STATUS).equalsIgnoreCase("OK")){
                            //historyResponsesModel.delete(response.
                            //        getLong(Constant.JSON_MESSAGE_ID_HISTORY_DELETE_RECEIVED));
                            historyResponsesModel.setSubmittedServer(response.
                                            getLong(Constant.JSON_MESSAGE_ID_HISTORY_DELETE_RECEIVED));
                            nHistoryUpdatedInServer++;
                        }
                    } catch (JSONException ignored) { }
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONArray errorResponse) {
                    Log.e(Constant.TAG_LOG, "" + throwable.toString());
                    publishProgress(context.getString(R.string.error_no_synchronized_data));
                    Log.e(Constant.TAG_LOG, "ResponsString: "+errorResponse.toString());
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers,
                                      java.lang.Throwable throwable, JSONObject errorResponse) {
                    Log.e(Constant.TAG_LOG, throwable.toString());
                    publishProgress(
                            context.getString(R.string.error_no_synchronized_data_verify_connection));

                    Log.e(Constant.TAG_LOG, "ResponsString: "+errorResponse.toString());

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    // When Http response code is '404'
                    if (statusCode == 404) {
                        publishProgress(context.getString(R.string.error_404));
                        Log.e(Constant.TAG_LOG, context.getString(R.string.error_404));
                    } else if (statusCode == 500) {
                        publishProgress(context.getString(R.string.error_500));
                        Log.e(Constant.TAG_LOG, context.getString(R.string.error_500));
                    } else {
                        publishProgress(context.getString(R.string.error_unexpected_error));
                        Log.e(Constant.TAG_LOG, context.getString(R.string.error_unexpected_error));
                        throwable.printStackTrace();
                    }

                    Log.e(Constant.TAG_LOG, "ResponsString: "+responseString);

//                    Log.e(Constant.TAG_LOG, "RESPONSE: " + responseString);
                }

                @Override
                public void onFinish() {
                    Log.d(Constant.TAG_LOG, "onFinish");
                }
            });
        }
    }

    private synchronized void postStatistics() throws UnsupportedEncodingException {

        Preferences preferences = new Preferences(context);

        statisticsModel = new StatisticsModel(context);
        statisticalUsages = statisticsModel.
                getStatisticsUsageNotSentToServer(preferences.getUserCodeLogged());

        for(int i = 0; i < statisticalUsages.size(); i++) {
            publishProgress(context.getString(R.string.msg_sending_statistics_usage_n_to_n, i+1,
                    statisticalUsages.size()));

            String jsonStatistics = new Gson().toJson(statisticalUsages.get(i));

            RequestParams params = new RequestParams();

            params.put(Constant.JSON_OBJECT_TAG_STATISTICS, jsonStatistics);

            MEDRestClient.post(context, Constant.WEBSERVICE_RELATIVE_URL_POST_STATISTICS, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    Log.d(Constant.TAG_LOG, "onSuccess");

                    try {
                        if(response.getString(Constant.JSON_MESSAGE_STATUS).equalsIgnoreCase("OK")){
                            //historyResponsesModel.delete(response.
                            //        getLong(Constant.JSON_MESSAGE_ID_HISTORY_DELETE_RECEIVED));
                           statisticsModel.setSubmittedServer(response.
                                    getLong(Constant.JSON_MESSAGE_ID_STATISTICAL_USAGE_UPDATE_RECEIVED));

                            nStatisticsUpdateInServer++;
                        }
                    } catch (JSONException ignored) { }
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable, org.json.JSONArray errorResponse) {
                    Log.e(Constant.TAG_LOG, "" + throwable.toString());
                    publishProgress(context.getString(R.string.error_no_synchronized_data));
                    Log.e(Constant.TAG_LOG, "ResponsString: "+errorResponse.toString());
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers,
                                      java.lang.Throwable throwable, JSONObject errorResponse) {
                    Log.e(Constant.TAG_LOG, throwable.toString());
                    publishProgress(
                            context.getString(R.string.error_no_synchronized_data_verify_connection));

                    Log.e(Constant.TAG_LOG, "ResponsString: "+errorResponse.toString());

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    // When Http response code is '404'
                    if (statusCode == 404) {
                        publishProgress(context.getString(R.string.error_404));
                        Log.e(Constant.TAG_LOG, context.getString(R.string.error_404));
                    } else if (statusCode == 500) {
                        publishProgress(context.getString(R.string.error_500));
                        Log.e(Constant.TAG_LOG, context.getString(R.string.error_500));
                    } else {
                        publishProgress(context.getString(R.string.error_unexpected_error));
                        Log.e(Constant.TAG_LOG, context.getString(R.string.error_unexpected_error));
                        throwable.printStackTrace();
                    }

                    Log.e(Constant.TAG_LOG, "ResponsString: "+responseString);
                }

                @Override
                public void onFinish() {
                    Log.d(Constant.TAG_LOG, "onFinish");
                }
            });
        }
    }

    public interface SendHistoryTaskListener{
        void onSendHistoryStarted();
        void onSendHistoryFinished(String result);
        void progressSendHistoryUpdate(String progress);
    }
}