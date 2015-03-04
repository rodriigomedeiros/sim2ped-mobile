package br.uern.ges.med.sim2ped.managers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.alarm.Alarm;
import br.uern.ges.med.sim2ped.beans.Care;
import br.uern.ges.med.sim2ped.beans.CaresInContext;
import br.uern.ges.med.sim2ped.beans.Routine;
import br.uern.ges.med.sim2ped.beans.Tip;
import br.uern.ges.med.sim2ped.beans.User;
import br.uern.ges.med.sim2ped.model.CaresModel;
import br.uern.ges.med.sim2ped.model.ContextModel;
import br.uern.ges.med.sim2ped.model.RoutineModel;
import br.uern.ges.med.sim2ped.model.TipsModel;
import br.uern.ges.med.sim2ped.model.UserModel;
import br.uern.ges.med.sim2ped.restclient.MEDRestClient;
import br.uern.ges.med.sim2ped.utils.Constant;
import br.uern.ges.med.sim2ped.utils.JSONMessageObject;

/**
 * Created by Rodrigo on 29/09/2014 (16:25).
 * <p/>
 * Package: br.uern.ges.med.sim2ped.managers
 */
public class DownloaderDataTask extends AsyncTask<String, String, Void> {

    //private ProgressDialog progressDialog;
    private Context context;
    private final DownloaderListener listener;
    private long userCode;

    public DownloaderDataTask(long userCode, DownloaderListener listener, Context context) {
        this.userCode = userCode;
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute(){
        listener.onDownloaderStarted();
        publishProgress(context.getString(R.string.msg_downloading_datas));
    }

    @Override
    protected Void doInBackground(String... strings) {

        publishProgress(context.getString(R.string.msg_updating_cares));
        getDatas(Constant.JSON_OBJECT_TAG_CARE);

        publishProgress(context.getString(R.string.msg_updating_tips));
        getDatas(Constant.JSON_OBJECT_TAG_TIP);

        publishProgress(context.getString(R.string.msg_updating_contexts));
        getDatas(Constant.JSON_OBJECT_TAG_CONTEXT);
        getDatas(Constant.JSON_OBJECT_TAG_CARES_IN_CONTEXT);

        publishProgress(context.getString(R.string.msg_updating_data_user));
        getDatas(Constant.JSON_OBJECT_TAG_USER);

        publishProgress(context.getString(R.string.msg_updating_routines));
        getDatas(Constant.JSON_OBJECT_TAG_ROUTINE);

        Alarm.cancelAlarm(context); // Cancel existing alarms.
        Alarm.setAlarmCycleStart(context); // Configure alarm cycle based in new routines.

        return null;
    }

    protected void onProgressUpdate(String... text){
        listener.progressDownloaderUpdate(text[0]);
        Log.d(Constant.TAG_LOG, "Downloader progress is: " + text[0] + ".");
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        listener.onDownloaderFinished("");
    }

    private synchronized void getDatas(String dataType){
        RequestParams params = new RequestParams();

        if(dataType.equals(Constant.JSON_OBJECT_TAG_CARE)){
            params.add("data", Constant.JSON_OBJECT_TAG_CARE);
        } else if(dataType.equals(Constant.JSON_OBJECT_TAG_TIP)){
            params.add("data", Constant.JSON_OBJECT_TAG_TIP);
        } else if(dataType.equals(Constant.JSON_OBJECT_TAG_CONTEXT)){
            params.add("data", Constant.JSON_OBJECT_TAG_CONTEXT);
        } else if(dataType.equals(Constant.JSON_OBJECT_TAG_CARES_IN_CONTEXT)){
            params.add("data", Constant.JSON_OBJECT_TAG_CARES_IN_CONTEXT);
        } else if(dataType.equals(Constant.JSON_OBJECT_TAG_ROUTINE)){
            params.add("data", Constant.JSON_OBJECT_TAG_ROUTINE);
        } else if(dataType.equals(Constant.JSON_OBJECT_TAG_USER)) {
            params.add("data", Constant.JSON_OBJECT_TAG_USER);
        } else {
            params.add("data", "");
        }

        params.add(Constant.PARAM_WS_USER_CODE, String.valueOf(userCode)); // user code

        MEDRestClient.get(Constant.WEBSERVICE_RELATIVE_URL_DOWNLOAD_DATES, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                JSONMessageObject jsonMessageObject = new Gson()
                        .fromJson(response.toString(), JSONMessageObject.class);
                updateDataInDB(jsonMessageObject);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONMessageObject jsonMessageObject = new Gson()
                                .fromJson(response.getJSONObject(i).toString(), JSONMessageObject.class);
                        updateDataInDB(jsonMessageObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                publishProgress(context.getString(R.string.msg_synchronized_data));

            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers,
                                  java.lang.Throwable throwable, org.json.JSONArray errorResponse) {
                Log.d(Constant.TAG_LOG, "" + throwable.toString());
                publishProgress(context.getString(R.string.error_no_synchronized_data));
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers,
                                  java.lang.Throwable throwable, JSONObject errorResponse) {
                Log.d(Constant.TAG_LOG, throwable.toString());
                publishProgress(context.getString(R.string.error_no_synchronized_data_verify_connection));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                // When Http response code is '404'
                if (statusCode == 404) {
                    publishProgress(context.getString(R.string.error_404));
                    Log.d(Constant.TAG_LOG, context.getString(R.string.error_404));
                } else if (statusCode == 500) {
                    publishProgress(context.getString(R.string.error_500));
                    Log.d(Constant.TAG_LOG, context.getString(R.string.error_500));
                } else {
                    publishProgress(context.getString(R.string.error_unexpected_error));
                    Log.d(Constant.TAG_LOG, context.getString(R.string.error_unexpected_error));
                }
            }
        });
    }

    private void updateDataInDB(JSONMessageObject jsonMessageObject){
        if (jsonMessageObject.getType().equals(Constant.JSON_OBJECT_TAG_TIP)) {
            Log.i(Constant.TAG_LOG, "TIP: " + jsonMessageObject.getObject());

            TipsModel tipsModel = new TipsModel(context);
            Tip tip = new Gson().fromJson(jsonMessageObject.getObject(), Tip.class);

            tipsModel.insert(tip);

        } else if (jsonMessageObject.getType().equals(Constant.JSON_OBJECT_TAG_CARE)) {
            Log.i(Constant.TAG_LOG, "CARE: " + jsonMessageObject.getObject());

            CaresModel caresModel = new CaresModel(context);
            Care care = new Gson().fromJson(jsonMessageObject.getObject(), Care.class);

            caresModel.insert(care);

        } else if (jsonMessageObject.getType().equals(Constant.JSON_OBJECT_TAG_ROUTINE)) {
            Log.i(Constant.TAG_LOG, "ROUTINE: " + jsonMessageObject.getObject());

            RoutineModel routineModel = new RoutineModel(context);
            Routine routine = new Gson().fromJson(jsonMessageObject.getObject(), Routine.class);

            routineModel.insert(routine);

        } else if (jsonMessageObject.getType().equals(Constant.JSON_OBJECT_TAG_CONTEXT)) {
            Log.i(Constant.TAG_LOG, "CONTEXT: " + jsonMessageObject.getObject());

            ContextModel contextModel = new ContextModel(context);
            br.uern.ges.med.sim2ped.beans.Context context1 = new Gson().
                    fromJson(jsonMessageObject.getObject(),
                            br.uern.ges.med.sim2ped.beans.Context.class);

            contextModel.insert(context1);

        } else if (jsonMessageObject.getType().equals(Constant.JSON_OBJECT_TAG_CARES_IN_CONTEXT)) {
            Log.i(Constant.TAG_LOG, "CONTEXT IN CARE: " + jsonMessageObject.getObject());

            ContextModel contextModel = new ContextModel(context);
            CaresInContext caresInContext = new Gson().
                    fromJson(jsonMessageObject.getObject(), CaresInContext.class);

            contextModel.inserCaresInContext(caresInContext);

        } else if (jsonMessageObject.getType().equals(Constant.JSON_OBJECT_TAG_USER)) {
            Log.i(Constant.TAG_LOG, "USER: " + jsonMessageObject.getObject());

            UserModel userModel = new UserModel(context);
            User user = new Gson().fromJson(jsonMessageObject.getObject(), User.class);

            userModel.insert(user);
        }
    }

    public interface DownloaderListener{
        void onDownloaderStarted();
        void onDownloaderFinished(String result);
        void progressDownloaderUpdate(String progress);
    }
}
