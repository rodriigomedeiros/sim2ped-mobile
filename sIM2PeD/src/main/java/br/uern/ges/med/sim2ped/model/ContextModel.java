package br.uern.ges.med.sim2ped.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import br.uern.ges.med.sim2ped.beans.CaresInContext;
import br.uern.ges.med.sim2ped.model.persistence.db.DataBase;
import br.uern.ges.med.sim2ped.utils.Constant;

/**
 * Created by Rodrigo on 23/08/2014 (09:27).
 *
 *
 */
public class ContextModel {

    private DataBase db;

    public ContextModel(Context context){
        db = DataBase.getInstance(context);
    }


    /**
     * Insert new context in database
     *
     * @param context Context object
     * @return true if success or false if not
     */
    public boolean insert(br.uern.ges.med.sim2ped.beans.Context context) {
        ContentValues c = new ContentValues();

        c.put(Constant.DB_TABLE_CONTEXTS_ROW_ID, context.getId());
        c.put(Constant.DB_TABLE_CONTEXTS_ROW_NAME, context.getName());

        try {
//            db.getDB().insertOrThrow(Constant.DB_TABLE_CONTEXTS, null, c);
            db.getDB().insertWithOnConflict(Constant.DB_TABLE_CONTEXTS, null, c, SQLiteDatabase.CONFLICT_REPLACE);
            Log.i(Constant.TAG_LOG, "Inserting new entry in context...");

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    /**
     * Update context in database
     * @param context Context object
     * @return boolean true if success or false if not
     */
    public synchronized boolean update(br.uern.ges.med.sim2ped.beans.Context context) {

        ContentValues c = new ContentValues();

        c.put(Constant.DB_TABLE_CONTEXTS_ROW_NAME, context.getName());

        try {
            int numRowsAffected = db.getDB().update(Constant.DB_TABLE_CONTEXTS, c,
                    Constant.DB_TABLE_CONTEXTS_ROW_ID + " = ?",
                    new String[] {String.valueOf(context.getId())});
            Log.i(Constant.TAG_LOG, "Updating Context...");

            return numRowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {

            db.close();
        }
    }

    /**
     * Get context in DB
     *
     * @param contextId Id of the context;
     * @return Context
     */
    public br.uern.ges.med.sim2ped.beans.Context getContext(long contextId){
        br.uern.ges.med.sim2ped.beans.Context context = new br.uern.ges.med.sim2ped.beans.Context();

        try {
            Cursor cursor = db.getDB().query(Constant.DB_TABLE_CONTEXTS, null, Constant.DB_TABLE_CONTEXTS_ROW_ID + " = ?",
                    new String[]{String.valueOf(contextId)}, null, null, null);

            if (cursor.moveToFirst()) {
                context.setId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_CONTEXTS_ROW_ID)));
                context.setName(cursor.getString(cursor.getColumnIndex(Constant.DB_TABLE_CONTEXTS_ROW_NAME)));
            }
        } catch (Exception ignored){
        } finally {
            db.close();
        }

        return context;
    }

    /**
     * Get all index of care in one determinate context
     *
     * @param contextId Id of the context;
     * @return ArrayList<Long>
     */
    public ArrayList<Long> getAllCaresIdInContext(long contextId){

        ArrayList<Long> ids = new ArrayList<Long>();

        try {
            Cursor cursor = db.getDB().query(Constant.DB_TABLE_CARES_IN_CONTEXT, null, Constant.DB_TABLE_CARES_IN_CONTEXT_ROW_CONTEXT_ID + "=?",
                    new String[]{String.valueOf(contextId)}, null, null, null);


            if (cursor.moveToFirst()) {
                do {
                    ids.add(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_CARES_IN_CONTEXT_ROW_CARE_ID)));
                } while (cursor.moveToNext());
            }
        } catch (Exception ignored){

        } finally {
            db.close();
        }

        return ids;
    }

    /**
     * Get aleatory index of care in one determinate context
     *
     * @param   contextId   Id of the context;
     * @return  Random care
     */
    public long getRandomCareIdInContext(long contextId){

        try {
            Cursor cursor = db.getDB().query(Constant.DB_TABLE_CARES_IN_CONTEXT, null,
                    Constant.DB_TABLE_CARES_IN_CONTEXT_ROW_CONTEXT_ID + " = ?",
                    new String[]{String.valueOf(contextId)}, null, null, "RANDOM()",
                    String.valueOf(1));

            if (cursor.moveToFirst()) {
                return cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_CARES_IN_CONTEXT_ROW_CARE_ID));
            }
        } catch (Exception ignored){

        } finally {
            db.close();
        }

        return 0;
    }

    /**
     * Delete one Context in DB.
     *
     * @param ID Id of the context
     * @return Boolean, True for success, False for error.
     */
    public boolean delete(long ID) {
        try {
            int r = db.getDB().delete(Constant.DB_TABLE_CONTEXTS, Constant.DB_TABLE_CONTEXTS_ROW_ID + " = ?",
                    new String[]{Long.toString(ID)});

            return r > 0;

        } catch (Exception e){
            Log.e(Constant.TAG_LOG, "Error - ContextModel[delete()]");
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    /**
     * Insert relation of Care in Context
     *
     * @param caresInContext    CaresInContext object
     * @return true if success or false if not
     */

    public boolean inserCaresInContext(CaresInContext caresInContext){

        ContentValues c = new ContentValues();

        c.put(Constant.DB_TABLE_CARES_IN_CONTEXT_ROW_CONTEXT_ID, caresInContext.getContextId());
        c.put(Constant.DB_TABLE_CARES_IN_CONTEXT_ROW_CARE_ID, caresInContext.getCareId());

        try {
            db.getDB().insertOrThrow(Constant.DB_TABLE_CARES_IN_CONTEXT, null, c);
            Log.i(Constant.TAG_LOG, "Inserting new cares in context...");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }

    }

    /**
     * Delete relation of Care in Context
     *
     * @param ID CareId
     * @return Boolean, True for success, False for error.
     */
    public boolean deleteCaresInContext_CARE(long ID) {
        try {
            int r = db.getDB().delete(Constant.DB_TABLE_CARES_IN_CONTEXT,
                    Constant.DB_TABLE_CARES_IN_CONTEXT_ROW_CARE_ID + " = ?",
                    new String[]{Long.toString(ID)});

            return r > 0;

        } catch (Exception e){
            Log.e(Constant.TAG_LOG, "Error - ContextModel[deleteCaresInContext_CARE()]");
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    /**
     * Delete relation of Care in Context
     *
     * @param ID CareId
     * @return Boolean, True for success, False for error.
     */
    public boolean deleteCaresInContext_CONTEXT(long ID) {
        try {
            int r = db.getDB().delete(Constant.DB_TABLE_CARES_IN_CONTEXT,
                    Constant.DB_TABLE_CARES_IN_CONTEXT_ROW_CONTEXT_ID + " = ?",
                    new String[]{Long.toString(ID)});

            return r > 0;

        } catch (Exception e){
            Log.e(Constant.TAG_LOG, "Error - ContextModel[deleteCaresInContext_CONTEXT()]");
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

}
