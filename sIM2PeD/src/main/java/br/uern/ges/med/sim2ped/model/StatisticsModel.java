package br.uern.ges.med.sim2ped.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import java.util.ArrayList;

import br.uern.ges.med.sim2ped.beans.StatisticalReadingTips;
import br.uern.ges.med.sim2ped.beans.StatisticalUsage;
import br.uern.ges.med.sim2ped.beans.StatisticalViewProcedure;
import br.uern.ges.med.sim2ped.model.persistence.db.DataBase;
import br.uern.ges.med.sim2ped.utils.Constant;

/**
 * Created by Rodrigo on 04/10/2014 (19:36).
 *
 *
 * Package: br.uern.ges.med.sim2ped.model
 */
public class StatisticsModel {

    private DataBase db;
    //private Context context;

    public StatisticsModel(Context context){
       // this.context = context;
        db = DataBase.getInstance(context);
    }

    /**
     * Insert new statistical of the usage in database
     *
     * @param usage Object StatisticalUsage
     * @return ID of the statics inserted or -1 if error
    */
    public long insertStaticalUsage(StatisticalUsage usage) {

        ContentValues c = new ContentValues();

        c.put(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_USER_ID, usage.getUserId());
        c.put(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ENTRY_TIME, usage.getEntryTime());
        c.put(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_SENT_SERVER, 0);

        try {
            long id = db.getDB().insertOrThrow(Constant.DB_TABLE_STATISTICAL_USAGE, null, c);

            Log.i(Constant.TAG_LOG, "Inserting new entry in StaticalUsage...");
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            db.close();
        }
    }

    /**
     * Insert new statistical of the reading tips in database
     *
     * @param readingTips Object StatisticalReadingTips
     * @return true if success or false if not
     */
    public boolean insertStaticalReadingTips(StatisticalReadingTips readingTips) {

        ContentValues c = new ContentValues();

        c.put(Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_STATISTICAL_ID, readingTips.getStatisticalId());
        c.put(Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_HOUR_READING, readingTips.getHourReading());
        c.put(Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_TIP_ID, readingTips.getTipId());
        c.put(Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_USER_ID, readingTips.getUserId());

        try {
            db.getDB().insertOrThrow(Constant.DB_TABLE_STATISTICAL_READING_TIPS, null, c);
            Log.i(Constant.TAG_LOG, "Inserting new entry in StaticalReadingTips...");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    /**
     * Insert new statistical of the view procedure in database
     *
     * @param statisticalViewProcedure Object StatisticalViewProcedure
     * @return true if success or false if not
     */
    public boolean insertStaticalViewProcedure(StatisticalViewProcedure statisticalViewProcedure) {

        ContentValues c = new ContentValues();

        c.put(Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_STATISTICAL_ID, statisticalViewProcedure.getStatisticalId());
        c.put(Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_HOUR_VIEW, statisticalViewProcedure.getHourView());
        c.put(Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_USER_ID, statisticalViewProcedure.getUserId());

        try {
            db.getDB().insertOrThrow(Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE, null, c);
            Log.i(Constant.TAG_LOG, "Inserting new entry in StaticalViewProcedure...");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }


    /**
     * Get StatisticUsage has not yet been sent to the server
     *
     * @param userId Id of the user
     * @return {Routine}

    public StatisticalUsage getStatisticUsageNotSentToServer(long userId){
        String QUERY = "SELECT * FROM " + Constant.DB_TABLE_STATISTICAL_USAGE + " WHERE " +
                Constant.DB_TABLE_STATISTICAL_USAGE_ROW_USER_ID + " = ? AND "+
                Constant.DB_TABLE_STATISTICAL_USAGE_ROW_SENT_SERVER + " = 0";

        String[] statement = new String[]{String.valueOf(userId)};

        StatisticalUsage statisticalUsage = new StatisticalUsage();

        try{
            Cursor cursor = db.getDB().rawQuery(QUERY, statement);

            if (cursor.moveToFirst()) {
                statisticalUsage.setId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ID)));
                statisticalUsage.setEntryTime(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ENTRY_TIME)));
                statisticalUsage.setUserId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_USER_ID)));
                statisticalUsage.setSentServer(cursor.getInt(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ENTRY_TIME)));
                statisticalUsage.setStatisticalReadingTips(getStatisticsReadingTips(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ID))));

            }

        } catch (Exception ignored){

        } finally {
            db.close();
        }

        return statisticalUsage;
    }*/

    /**
     * Get StatisticsUsage has not yet been sent to the server
     *
     * @param userId Id of the user
     * @return ArrayList<StaticalUsage>
     */
    public ArrayList<StatisticalUsage> getStatisticsUsageNotSentToServer(long userId){
        String QUERY = "SELECT * FROM " + Constant.DB_TABLE_STATISTICAL_USAGE + " WHERE " +
                Constant.DB_TABLE_STATISTICAL_USAGE_ROW_USER_ID + " = ? AND "+
                Constant.DB_TABLE_STATISTICAL_USAGE_ROW_SENT_SERVER + " = 0";

        String[] statement = new String[]{String.valueOf(userId)};

        ArrayList<StatisticalUsage> statisticalUsages = new ArrayList<StatisticalUsage>();

        Cursor cursor = db.getDB().rawQuery(QUERY, statement);

        try{
            if (cursor.moveToFirst()) do {
                StatisticalUsage statisticalUsage = new StatisticalUsage();
                statisticalUsage.setId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ID)));
                statisticalUsage.setEntryTime(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ENTRY_TIME)));
                statisticalUsage.setUserId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_USER_ID)));
                statisticalUsage.setSentServer(cursor.getInt(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_SENT_SERVER)));
                statisticalUsage.setStatisticalReadingTips(getStatisticsReadingTips(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ID))));
                statisticalUsage.setStatisticalViewProcedures(getStatisticsViewProcedures(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ID))));

                statisticalUsages.add(statisticalUsage);
            } while (cursor.moveToNext());

        } catch (Exception ignored){

        } finally {
            db.close();
        }

        return statisticalUsages;
    }

    /**
     * Get All StatisticsUsage
     *
     * @param userId Id of the user
     * @return ArrayList<StatisticalUsage>
     */
    public ArrayList<StatisticalUsage> getAllStatisticsUsage(long userId){
        String QUERY = "SELECT * FROM " + Constant.DB_TABLE_STATISTICAL_USAGE + " WHERE " +
                Constant.DB_TABLE_STATISTICAL_USAGE_ROW_USER_ID + " = ?";

        String[] statement = new String[]{String.valueOf(userId)};

        ArrayList<StatisticalUsage> statisticalUsages = new ArrayList<StatisticalUsage>();

        Cursor cursor = db.getDB().rawQuery(QUERY, statement);

        try{
            if (cursor.moveToFirst()) do {
                StatisticalUsage statisticalUsage = new StatisticalUsage();
                statisticalUsage.setId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ID)));
                statisticalUsage.setEntryTime(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ENTRY_TIME)));
                statisticalUsage.setUserId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_USER_ID)));
                statisticalUsage.setSentServer(cursor.getInt(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_SENT_SERVER)));
                statisticalUsage.setStatisticalReadingTips(getStatisticsReadingTips(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ID))));
                statisticalUsage.setStatisticalViewProcedures(getStatisticsViewProcedures(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ID))));

                statisticalUsages.add(statisticalUsage);
            } while (cursor.moveToNext());

        } catch (Exception ignored){

        } finally {
            db.close();
        }

        return statisticalUsages;
    }

    /**
     * Get all statistics of the one StatisticUsage Id
     *
     * @param idStatisticUsage Id of the Statistic Usage
     *
     * @return ArrayList<StatisticalReadingTips>
     */
    private ArrayList<StatisticalReadingTips> getStatisticsReadingTips(long idStatisticUsage) {

        ArrayList<StatisticalReadingTips> statisticalReadingTips = new ArrayList<StatisticalReadingTips>();

        String QUERY = "SELECT * FROM " + Constant.DB_TABLE_STATISTICAL_READING_TIPS +
                " WHERE " + Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_STATISTICAL_ID + " = ?" +
                " ORDER BY " + Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_HOUR_READING;

        Cursor cursor = db.getDB().rawQuery(QUERY, new String[]{String.valueOf(idStatisticUsage)});

        try {
            if (cursor.moveToFirst()) do {
                StatisticalReadingTips statisticReadingTips = new StatisticalReadingTips();

                statisticReadingTips.setStatisticalId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_STATISTICAL_ID)));
                statisticReadingTips.setHourReading(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_HOUR_READING)));
                statisticReadingTips.setTipId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_TIP_ID)));
                statisticReadingTips.setId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_ID)));
                statisticReadingTips.setUserId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_READING_TIPS_ROW_USER_ID)));

                statisticalReadingTips.add(statisticReadingTips);
            } while (cursor.moveToNext());
        } catch (Exception ignored) {

        } finally {
            db.close();
        }

        return statisticalReadingTips;
    }

    /**
     * Get all statistics of the one StatisticUsage Id
     *
     * @param idStatisticUsage Id of the Statistic Usage
     *
     * @return ArrayList<StatisticalViewProcedure>
     */
    private ArrayList<StatisticalViewProcedure> getStatisticsViewProcedures(long idStatisticUsage) {

        ArrayList<StatisticalViewProcedure> statisticalViewProcedures = new ArrayList<StatisticalViewProcedure>();

        String QUERY = "SELECT * FROM " + Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE +
                " WHERE " + Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_STATISTICAL_ID + " = ?" +
                " ORDER BY " + Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_HOUR_VIEW;

        Cursor cursor = db.getDB().rawQuery(QUERY, new String[]{String.valueOf(idStatisticUsage)});

        try {
            if (cursor.moveToFirst()) do {
                StatisticalViewProcedure statisticalViewProcedure = new StatisticalViewProcedure();

                statisticalViewProcedure.setStatisticalId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_STATISTICAL_ID)));
                statisticalViewProcedure.setHourView(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_HOUR_VIEW)));
                statisticalViewProcedure.setId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_ID)));
                statisticalViewProcedure.setUserId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_STATISTICAL_VIEW_PROCEDURE_ROW_USER_ID)));

                statisticalViewProcedures.add(statisticalViewProcedure);
            } while (cursor.moveToNext());
        } catch (Exception ignored) {
        } finally {
            db.close();
        }

        return statisticalViewProcedures;
    }

    /**
     * Set statistical has submitted to the server
     * @param idStatistic id of the history for update
     * @return boolean true if success or false if not
    */
    public boolean setSubmittedServer(long idStatistic) {

        ContentValues c = new ContentValues();

        c.put(Constant.DB_TABLE_STATISTICAL_USAGE_ROW_SENT_SERVER, 1);

        try {
            db.getDB().update(Constant.DB_TABLE_STATISTICAL_USAGE, c,
                    Constant.DB_TABLE_STATISTICAL_USAGE_ROW_ID + " = ?",
                    new String[] {String.valueOf(idStatistic)});

            Log.i(Constant.TAG_LOG, "Updating statisticalUsage...");

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }
}
