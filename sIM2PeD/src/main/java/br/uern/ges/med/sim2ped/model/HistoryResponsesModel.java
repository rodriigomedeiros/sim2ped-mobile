package br.uern.ges.med.sim2ped.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import java.util.ArrayList;

import br.uern.ges.med.sim2ped.beans.HistoryResponse;
import br.uern.ges.med.sim2ped.model.persistence.db.DataBase;
import br.uern.ges.med.sim2ped.utils.Constant;

/**
 * Created by Rodrigo on 22/08/2014 (12:53).
 *
 *
 * Package:
 */
public class HistoryResponsesModel {

    private DataBase db;

    public HistoryResponsesModel(Context context){
        db = DataBase.getInstance(context);
    }

    /**
     * Insert new response in database
     *
     * @param history   History object
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insert(HistoryResponse history) {

        ContentValues c = new ContentValues();

        c.put(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_CARE_ID, history.getCareId());
        c.put(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_CONTEXT_ID, history.getContextId());
        c.put(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_RESPONSE, history.getResponse());
        c.put(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_RESPONSE_HOUR, history.getResponseHour());
        c.put(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ROUTINE_ID, history.getRoutineId());
        c.put(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_SENT_SERVER, 0);

        try {
            long id = db.getDB().insertOrThrow(Constant.DB_TABLE_HISTORY_RESPONSES, null, c);
            Log.i(Constant.TAG_LOG, "Inserting new response in history...");

            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            db.close();
        }
    }

    /**
     * Update history in database
     * @param history   History object
     * @return boolean true if success or false if not
     */
    public synchronized boolean update(HistoryResponse history) {

        ContentValues c = new ContentValues();

        c.put(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_CARE_ID, history.getCareId());
        c.put(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_CONTEXT_ID, history.getContextId());
        c.put(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_RESPONSE, history.getResponse());
        c.put(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_RESPONSE_HOUR, history.getResponseHour());
        c.put(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ROUTINE_ID, history.getRoutineId());

        try {
            int numRowsAffected = db.getDB().update(Constant.DB_TABLE_HISTORY_RESPONSES, c,
                    Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ID + " = ?",
                    new String[] {String.valueOf(history.getId())});
            Log.i(Constant.TAG_LOG, "Updating history...");

            return numRowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }


    /**
     * Set history as submitted to the server
     * @param idHistory id of the history for update
     * @return boolean true if success or false if not
     */
    public boolean setSubmittedServer(long idHistory) {

        ContentValues c = new ContentValues();

        c.put(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_SENT_SERVER, 1);

        try {
            db.getDB().update(Constant.DB_TABLE_HISTORY_RESPONSES, c,
                    Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ID + " = ?",
                    new String[] {String.valueOf(idHistory)});
            Log.i(Constant.TAG_LOG, "Updating history...");

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    /**
     * Get all responses in history of User in DB
     *
     * @param userId Id of the User
     * @param timeMillis Time in millis to compare
     * @return List<HistoryResponse>
     */
    public ArrayList<HistoryResponse> getHistoryResponsesAfter(long userId, long timeMillis) {

        String QUERY = "SELECT * FROM " + Constant.DB_TABLE_ROUTINE +
                        " INNER JOIN " + Constant.DB_TABLE_HISTORY_RESPONSES + " ON " +
                        Constant.DB_TABLE_HISTORY_RESPONSES + "." + Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ROUTINE_ID +
                        " = " + Constant.DB_TABLE_ROUTINE + "." + Constant.DB_TABLE_ROUTINE_ROW_ID +
                        " WHERE " + Constant.DB_TABLE_ROUTINE_ROW_USER_ID +" = ? " +
                        "AND strftime('%Y-%m-%d %H:%M', " + Constant.DB_TABLE_HISTORY_RESPONSES_ROW_RESPONSE_HOUR +"/1000, 'unixepoch') >= strftime('%Y-%m-%d %H:%M', ?/1000, 'unixepoch') ORDER BY " +
                        Constant.DB_TABLE_HISTORY_RESPONSES_ROW_RESPONSE_HOUR + " DESC";

        ArrayList<HistoryResponse> historyResponses = new ArrayList<HistoryResponse>();

        try {
            Cursor cursor = db.getDB().rawQuery(QUERY, new String[]{String.valueOf(userId), String.valueOf(timeMillis)});

            if (cursor.moveToFirst()) {
                do {
                    HistoryResponse history = new HistoryResponse();

                    history.setCareId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_CARE_ID)));
                    history.setContextId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_CONTEXT_ID)));
                    history.setId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ID)));
                    history.setResponse(cursor.getString(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_RESPONSE)));
                    history.setResponseHour(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_RESPONSE_HOUR)));
                    history.setRoutineId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ROUTINE_ID)));

                    historyResponses.add(history);

                } while (cursor.moveToNext());
            }
        } catch (Exception ignored) {

        } finally {
            db.close();
        }

        return historyResponses;
    }

    /**
     * Get all responses in history of User in DB
     *
     * @param userId Id of the User
     * @return List<HistoryResponse>
     */
	public ArrayList<HistoryResponse> getAllHistoryResponses(long userId) {

        String QUERY = "SELECT * FROM " + Constant.DB_TABLE_ROUTINE + ", " +
                Constant.DB_TABLE_HISTORY_RESPONSES +
                " WHERE " + Constant.DB_TABLE_ROUTINE + "." + Constant.DB_TABLE_ROUTINE_ROW_ID + " = " +
                Constant.DB_TABLE_HISTORY_RESPONSES + "."+Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ROUTINE_ID +
                " AND "+ Constant.DB_TABLE_ROUTINE_ROW_USER_ID +" = ?";

        ArrayList<HistoryResponse> historyResponses = new ArrayList<HistoryResponse>();

        try {
            Cursor cursor = db.getDB().rawQuery(QUERY, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                do {
                    HistoryResponse history = new HistoryResponse();

                    history.setCareId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_CARE_ID)));
                    history.setContextId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_CONTEXT_ID)));
                    history.setId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ID)));
                    history.setResponse(cursor.getString(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_RESPONSE)));
                    history.setResponseHour(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_RESPONSE_HOUR)));
                    history.setRoutineId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ROUTINE_ID)));

                    historyResponses.add(history);

                } while (cursor.moveToNext());
            }
        } catch (Exception ignored) {

        } finally {
            db.close();
        }

        return historyResponses;
	}

    /**
     * Get all responses in history of User in DB
     *
     * @param userId Id of the User
     * @return List<HistoryResponse>
     */
    public ArrayList<HistoryResponse> getAllHistoryResponsesNotSentToServer(long userId) {

        String QUERY = "SELECT * FROM " + Constant.DB_TABLE_ROUTINE + ", " +
                Constant.DB_TABLE_HISTORY_RESPONSES +
                " WHERE " + Constant.DB_TABLE_ROUTINE + "." + Constant.DB_TABLE_ROUTINE_ROW_ID + " = " +
                Constant.DB_TABLE_HISTORY_RESPONSES + "."+Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ROUTINE_ID +
                " AND "+ Constant.DB_TABLE_ROUTINE_ROW_USER_ID +" = ?" +
                " AND " + Constant.DB_TABLE_HISTORY_RESPONSES_ROW_SENT_SERVER + " = 0";

        ArrayList<HistoryResponse> historyResponses = new ArrayList<HistoryResponse>();

        try {
            Cursor cursor = db.getDB().rawQuery(QUERY, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                do {
                    HistoryResponse history = new HistoryResponse();

                    history.setCareId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_CARE_ID)));
                    history.setContextId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_CONTEXT_ID)));
                    history.setId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ID)));
                    history.setResponse(cursor.getString(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_RESPONSE)));
                    history.setResponseHour(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_RESPONSE_HOUR)));
                    history.setRoutineId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ROUTINE_ID)));

                    historyResponses.add(history);

                } while (cursor.moveToNext());
            }
        } catch (Exception ignored) {

        } finally {
            db.close();
        }

        return historyResponses;
    }

    /**
     * Get number of entries in history of User in DB
     *
     * @param userId    Id of the User
     * @return number of entries the history of User in DB, 0 for empty and -1 for error.
     */
    public int getCountAllHistory(long userId) {

        String QUERY = "SELECT * FROM " + Constant.DB_TABLE_ROUTINE + ", " +
                Constant.DB_TABLE_HISTORY_RESPONSES + " WHERE " + Constant.DB_TABLE_ROUTINE + "." +
                Constant.DB_TABLE_ROUTINE_ROW_ID + " = " + Constant.DB_TABLE_HISTORY_RESPONSES +
                "."+Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ROUTINE_ID +
                " AND "+ Constant.DB_TABLE_ROUTINE_ROW_USER_ID +" = ?";

        try {
            Cursor cursor = db.getDB().rawQuery(QUERY, new String[]{String.valueOf(userId)});
            return cursor.getCount();
        } catch (Exception ignored) {
        } finally {
            db.close();
        }

        return -1;

    }

    /**
     * Delete one Response in DB.
     *
     * @param ID Id of the HistoryResponse
     * @return Boolean, True for success, False for error.
     */
    public boolean delete(long ID) {
        try {
            int r = db.getDB().delete(Constant.DB_TABLE_HISTORY_RESPONSES,
                    Constant.DB_TABLE_HISTORY_RESPONSES_ROW_ID + " = ?",
                    new String[]{Long.toString(ID)});

            return r > 0;
        } catch (Exception e){
            Log.e(Constant.TAG_LOG, "Error - HistoryResponsesModel[delete()]");
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }

    }

}