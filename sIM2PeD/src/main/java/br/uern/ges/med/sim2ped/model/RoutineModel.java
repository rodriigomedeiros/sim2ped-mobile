package br.uern.ges.med.sim2ped.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import br.uern.ges.med.sim2ped.beans.Routine;
import br.uern.ges.med.sim2ped.model.persistence.db.DataBase;
import br.uern.ges.med.sim2ped.preferences.Preferences;
import br.uern.ges.med.sim2ped.utils.Constant;

/**
 * Created by Rodrigo on 22/08/2014 (13:20).
 *
 *
 * Package:
 */
public class RoutineModel {
    private DataBase db;
    private Context context;

    public RoutineModel(Context context){
        this.context = context;
        db = DataBase.getInstance(context);
    }

    /**
     * Insert new routine in database
     *
     * @param routine Object routine
     * @return true if success or false if not
     */
    public boolean insert(Routine routine) {

        ContentValues c = new ContentValues();

        c.put(Constant.DB_TABLE_ROUTINE_ROW_CONTEXT_ID, routine.getContextId());
        c.put(Constant.DB_TABLE_ROUTINE_ROW_TIME, routine.getTime());
        c.put(Constant.DB_TABLE_ROUTINE_ROW_ID, routine.getId());
        c.put(Constant.DB_TABLE_ROUTINE_ROW_USER_ID, routine.getUserId());
       // c.put(Constant.DB_TABLE_ROUTINE_ROW_WEEK_DAY, routine.getWeekDay());

        try {
//            db.getDB().insertOrThrow(Constant.DB_TABLE_ROUTINE, null, c);
            db.getDB().insertWithOnConflict(Constant.DB_TABLE_ROUTINE, null, c, SQLiteDatabase.CONFLICT_REPLACE);
            Log.i(Constant.TAG_LOG, "Inserting new entry in routine...");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    /**
     * Update routine in database
     * @param routine Object routine
     * @return boolean true if success or false if not
     */
    public synchronized boolean updateRoutine(Routine routine) {

        ContentValues c = new ContentValues();

        c.put(Constant.DB_TABLE_ROUTINE_ROW_CONTEXT_ID, routine.getContextId());
        c.put(Constant.DB_TABLE_ROUTINE_ROW_TIME, routine.getTime());
        c.put(Constant.DB_TABLE_ROUTINE_ROW_USER_ID, routine.getUserId());
        //c.put(Constant.DB_TABLE_ROUTINE_ROW_WEEK_DAY, routine.getWeekDay());

        try {
            int numRowsAffected = db.getDB().update(Constant.DB_TABLE_ROUTINE, c, Constant.DB_TABLE_ROUTINE_ROW_ID + " = ?", new String[] {String.valueOf(routine.getId())});
            Log.i(Constant.TAG_LOG, "Updating routine...");

            return numRowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {

            db.close();
        }
    }

    /**
     * Get lost routines. Probably because the phone was turned off and the routine was not updated.
     *
     * @param userId - Id of user
     *
     * @return ArrayList<Routine>
     */

    public ArrayList<Routine> getLostRoutines(long userId){

        ArrayList<Routine> routines = new ArrayList<Routine>();
        Preferences pref = new Preferences(context);

        StringBuilder QUERY = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        long timeToCompare;

        // Add early minutes
        calendar.add(Calendar.MINUTE,
            ((Integer.parseInt(pref.getPrefEarlyMinutesForAlarm())+Constant.TIME_ALARM_LOST)*-1));
        timeToCompare = calendar.getTimeInMillis();

        QUERY.append("SELECT * FROM ").append(Constant.DB_TABLE_ROUTINE);
        QUERY.append(" WHERE " + Constant.DB_TABLE_ROUTINE_ROW_USER_ID + " = ").append(userId);
        QUERY.append(" AND " + Constant.DB_TABLE_ROUTINE_ROW_TIME + " <= ").append(timeToCompare);
        QUERY.append(" ORDER BY " + Constant.DB_TABLE_ROUTINE_ROW_TIME);


        Cursor cursor = db.getDB().rawQuery(QUERY.toString(), null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Routine routine = new Routine();

                    routine.setId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_ID)));
                    routine.setContextId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_CONTEXT_ID)));
                    routine.setTime(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_TIME)));
                    routine.setUserId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_USER_ID)));
//                routine.setWeekDay(c.getInt(c.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_WEEK_DAY)));

                    routines.add(routine);
                } while (cursor.moveToNext());
            }
        } catch (Exception ignored) {

        } finally {
            db.close();
        }

        return routines;
    }

    /**
     * Get all routines
     *
     * @param userId - Id of user
     *
     * @return ArrayList<Routine>
     */

    public ArrayList<Routine> getAll(long userId){

        ArrayList<Routine> routines = new ArrayList<Routine>();

        String QUERY = "SELECT * FROM " + Constant.DB_TABLE_ROUTINE +
                " WHERE " + Constant.DB_TABLE_ROUTINE_ROW_USER_ID + " = ?" +
                " ORDER BY " + Constant.DB_TABLE_ROUTINE_ROW_TIME;

        Cursor cursor = db.getDB().rawQuery(QUERY, new String[]{String.valueOf(userId)});

        try {
            if (cursor.moveToFirst()) do {
                Routine routine = new Routine();

                routine.setId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_ID)));
                routine.setContextId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_CONTEXT_ID)));
                routine.setTime(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_TIME)));
                routine.setUserId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_USER_ID)));
//                routine.setWeekDay(c.getInt(c.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_WEEK_DAY)));

                routines.add(routine);
            } while (cursor.moveToNext());
        } catch (Exception ignored) {

        } finally {
            db.close();
        }

        return routines;
    }



    /**
     *  Get next alarm (routine) of user.
     *
     * @param userId Id of user
     * @return Routine
     */
    public Routine getNextRoutine(long userId){
        return getNextRoutine(userId, -1,  -1);
    }

    /**
     *  Get next alarm (routine) of user.
     *
     * @param userId Id of user
     * @param actualRoutineTime Time of actual routine in millis
     * @return Routine
     */
    public Routine getNextRoutine(long userId, long actualRoutineTime){
        return getNextRoutine(userId, actualRoutineTime,  -1);
    }

    /**
     *  Get next alarm (routine) of user.
     *
     * @param userId Id of user
     * @param actualRoutineTime Actual routine time
     * @param actualRoutineId Actual routine id
     * @return Routine
     */
    private synchronized Routine getNextRoutine(long userId, long actualRoutineTime, long actualRoutineId) {

        StringBuilder QUERY = new StringBuilder();

        QUERY.append("SELECT * FROM " + Constant.DB_TABLE_ROUTINE);
        QUERY.append(" WHERE " + Constant.DB_TABLE_ROUTINE_ROW_USER_ID + " = ").append(userId);

        if(actualRoutineTime > 0)
            QUERY.append(" AND " + Constant.DB_TABLE_ROUTINE_ROW_TIME + " > ").append(actualRoutineTime);

        if(actualRoutineId > 0)
            QUERY.append(" AND " + Constant.DB_TABLE_ROUTINE_ROW_ID + " != ").append(actualRoutineId);

        QUERY.append(" ORDER BY " + Constant.DB_TABLE_ROUTINE_ROW_TIME);



        Routine routine = new Routine();


        try {
            Cursor cursor = db.getDB().rawQuery(QUERY.toString(), null);

            if (cursor.moveToFirst()) {
                routine.setId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_ID)));
                routine.setContextId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_CONTEXT_ID)));
                routine.setTime(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_TIME)));
                routine.setUserId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_USER_ID)));
//          routine.setWeekDay(c.getInt(c.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_WEEK_DAY)));

            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return routine;
    }

    /**
     *  Get actual alarm (routine) of user.
     *
     * @param userId Id of user
     * @return Routine
     */
    public synchronized Routine getActualRoutine(long userId) {

        StringBuilder QUERY = new StringBuilder();

        QUERY.append("SELECT * FROM " + Constant.DB_TABLE_ROUTINE);
        QUERY.append(" WHERE " + Constant.DB_TABLE_ROUTINE_ROW_USER_ID + " = ").append(userId);
        QUERY.append(" ORDER BY " + Constant.DB_TABLE_ROUTINE_ROW_TIME);

        Routine routine = new Routine();


        try {
            Cursor cursor = db.getDB().rawQuery(QUERY.toString(), null);

            if (cursor.moveToLast()) {
                routine.setId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_ID)));
                routine.setContextId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_CONTEXT_ID)));
                routine.setTime(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_TIME)));
                routine.setUserId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_USER_ID)));
//          routine.setWeekDay(c.getInt(c.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_WEEK_DAY)));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return routine;
    }

    /**
     * Get routine
     *
     * @param routineId Id of the routine
     * @return {Routine}
     */
    public Routine getRoutine(long routineId){
        String QUERY = "SELECT * FROM " + Constant.DB_TABLE_ROUTINE + " WHERE " +
                Constant.DB_TABLE_ROUTINE_ROW_ID + " = ? ";

        String[] statement = new String[]{String.valueOf(routineId)};

        Routine routine = new Routine();

        try{
            Cursor cursor = db.getDB().rawQuery(QUERY, statement);

            if (cursor.moveToFirst()) {
                routine.setId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_ID)));
                routine.setContextId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_CONTEXT_ID)));
                routine.setTime(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_TIME)));
                routine.setUserId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_USER_ID)));
//          routine.setWeekDay(c.getInt(c.getColumnIndex(Constant.DB_TABLE_ROUTINE_ROW_WEEK_DAY)));

            }

        } catch (Exception ignored){

        } finally {
            db.close();
        }

        return routine;
    }

    /**
     * Delete one Routine in DB.
     *
     * @param ID Id of the routine
     * @return Boolean, True for success, False for error.
     */
    public boolean delete(long ID) {
        try {
            int r = db.getDB().delete(Constant.DB_TABLE_ROUTINE, Constant.DB_TABLE_ROUTINE_ROW_ID + " = ?",
                    new String[]{Long.toString(ID)});

            return r > 0;
        } catch (Exception e){
            Log.e(Constant.TAG_LOG, "Error - RoutineModel[delete()]");
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }

    }

}
