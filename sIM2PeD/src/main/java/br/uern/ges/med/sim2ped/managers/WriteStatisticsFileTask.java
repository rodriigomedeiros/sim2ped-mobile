package br.uern.ges.med.sim2ped.managers;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;

import br.uern.ges.med.sim2ped.beans.HistoryResponse;
import br.uern.ges.med.sim2ped.beans.StatisticalUsage;
import br.uern.ges.med.sim2ped.model.HistoryResponsesModel;
import br.uern.ges.med.sim2ped.model.StatisticsModel;
import br.uern.ges.med.sim2ped.preferences.Preferences;
import br.uern.ges.med.sim2ped.utils.FileOperations;

/**
 * Created by Rodrigo on 07/10/2014 (08:47).
 *
 *
 * Package: br.uern.ges.med.sim2ped.managers
 */
public class WriteStatisticsFileTask extends AsyncTask<Void, Void, Void>{

    private final WriteStatisticsFileTaskListener writeStatisticsFileTaskListener;
    private final Context context;

    public WriteStatisticsFileTask(WriteStatisticsFileTaskListener writeStatisticsFileTaskListener, Context context){
        this.context = context;
        this.writeStatisticsFileTaskListener = writeStatisticsFileTaskListener;
    }

    @Override
    protected void onPreExecute(){
        writeStatisticsFileTaskListener.onWriteStarted();
    }


    @Override
    protected Void doInBackground(Void... unused) {

        Calendar cal = Calendar.getInstance();

        String filename = "SIM2PeD_" +
                cal.get(Calendar.YEAR) + "-"+
                (cal.get(Calendar.MONTH)+1) + "-" +
                cal.get(Calendar.DAY_OF_MONTH) + "-" +
                cal.get(Calendar.HOUR_OF_DAY) + "-" +
                cal.get(Calendar.MINUTE) + "-" +
                cal.get(Calendar.SECOND) + "-" +
                cal.get(Calendar.MILLISECOND) +
                ".json";


        Preferences preferences = new Preferences(context);

        FileOperations fileOperations = new FileOperations();

        if(fileOperations.checkExternalMedia() == 2) {

            StatisticsModel statisticsModel = new StatisticsModel(context);
            HistoryResponsesModel historyResponsesModel = new HistoryResponsesModel(context);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            ArrayList<HistoryResponse> allHistoryResponses = historyResponsesModel.
                    getAllHistoryResponses(preferences.getUserCodeLogged());

            for (HistoryResponse historyResponse : allHistoryResponses) {
                String historyJson = gson.toJson(historyResponse);
                fileOperations.writeToSDFile(filename, historyJson);
            }

            ArrayList<StatisticalUsage> allStatisticsUsage = statisticsModel.
                    getAllStatisticsUsage(preferences.getUserCodeLogged());

            for (StatisticalUsage statisticalUsage : allStatisticsUsage) {
                String staticalJson = gson.toJson(statisticalUsage);
                fileOperations.writeToSDFile(filename, staticalJson);
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        writeStatisticsFileTaskListener.onWriteFinished();
    }

    public interface WriteStatisticsFileTaskListener {
        void onWriteStarted();

        void onWriteFinished();
    }

}
