// http://nuvemandroid.wordpress.com/2013/12/15/utilizando-sqlite/
// http://cmanios.wordpress.com/2012/05/17/extend-sqliteopenhelper-as-a-singleton-class-in-android/
// http://douglasalipio.wordpress.com/2011/09/05/aplicando-mvccamadas-em-um-projeto-android-%E2%80%93-parte-final-2/

package br.uern.ges.med.sim2ped.model;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.uern.ges.med.sim2ped.beans.Tip;
import br.uern.ges.med.sim2ped.model.persistence.db.DataBase;
import br.uern.ges.med.sim2ped.utils.Constant;

public class TipsModel {

	private DataBase db;

	public TipsModel(Context context) {
		db = DataBase.getInstance(context);
	}

	/**
	 * Insert new tip in database
	 * 
	 * @param tip Object tip
     * @return True if sucess, false for fail
	 */
	public boolean insert(Tip tip) {

		ContentValues c = new ContentValues();
		
		c.put(Constant.DB_TABLE_TIPS_ROW_ID, tip.getID());
        c.put(Constant.DB_TABLE_TIPS_ROW_CARE_ID, tip.getCareId());
		c.put(Constant.DB_TABLE_TIPS_ROW_TEXT, tip.getText());
		c.put(Constant.DB_TABLE_TIPS_ROW_PRIORITY, tip.getPriority());
		c.put(Constant.DB_TABLE_TIPS_ROW_CREATED_AT, tip.getCreatedAt());
		c.put(Constant.DB_TABLE_TIPS_ROW_MODIFIED_AT, tip.getModifiedAt());

		try {
			//db.getDB().insertOrThrow(Constant.DB_TABLE_TIPS, null, c);
            db.getDB().insertWithOnConflict(Constant.DB_TABLE_TIPS, null, c, SQLiteDatabase.CONFLICT_REPLACE);
			Log.i(Constant.TAG_LOG, "Inserting new tip...");
			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
            db.close();
        }

	}

    /**
     * Update tip in database
     *
     * @param tip Object tip
     * @return True if sucess, false for fail
     */
    public boolean update(Tip tip) {

        ContentValues c = new ContentValues();

        c.put(Constant.DB_TABLE_TIPS_ROW_CARE_ID, tip.getCareId());
        c.put(Constant.DB_TABLE_TIPS_ROW_TEXT, tip.getText());
        c.put(Constant.DB_TABLE_TIPS_ROW_PRIORITY, tip.getPriority());
        c.put(Constant.DB_TABLE_TIPS_ROW_CREATED_AT, tip.getCreatedAt());
        c.put(Constant.DB_TABLE_TIPS_ROW_MODIFIED_AT, tip.getModifiedAt());

        try {
            int numRows = db.getDB().update(Constant.DB_TABLE_TIPS, c, Constant.DB_TABLE_TIPS_ROW_ID + " = ?",
                    new String[] {String.valueOf(tip.getID())});
            Log.i(Constant.TAG_LOG, "Updating tip...");

            return numRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }

    }

	/**
	 * Delete one Tip in DB.
	 * 
	 * @param ID Id of the tip
	 * @return Boolean, True for success, False for error.
	 */
	public boolean delete(long ID) {
        try {
            int r = db.getDB().delete(Constant.DB_TABLE_TIPS, Constant.DB_TABLE_TIPS_ROW_ID + " = ?",
                    new String[]{Long.toString(ID)});

            return r > 0;
        } catch (Exception e){
            Log.e(Constant.TAG_LOG, "Error - TipsModel[delete()]");
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }

	}

	/**
	 * Get one Tip
	 * 
	 * @param ID Id of the tip
	 * @return Tip
	 */
	public Tip get(long ID) {
        Tip tip = new Tip();

        try {
            Cursor cursor = db.getDB().query(Constant.DB_TABLE_TIPS, null, Constant.DB_TABLE_TIPS_ROW_ID + " = ?",
                    new String[]{String.valueOf(ID)}, null, null, null);

            cursor.moveToFirst();

            tip.setID(cursor.getInt(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_ID)));
            tip.setCareId(cursor.getInt(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_CARE_ID)));
            tip.setText(cursor.getString(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_TEXT)));
            tip.setPriority(cursor.getInt(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_PRIORITY)));
            tip.setCreatedAt(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_CREATED_AT)));
            tip.setModifiedAt(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_MODIFIED_AT)));
            tip.setInsertedAt(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_INSERTED_AT)));
        } catch (Exception e){
            Log.e(Constant.TAG_LOG, "Error - TipsModel[get() - Tip]");
            e.printStackTrace();
        } finally {
            db.close();
        }

        return tip;

    }

    /**
     * Get all tips in DB
     *
     * @return List<Tip>
     */
    public ArrayList<Tip> get() {

        ArrayList<Tip> tips = new ArrayList<Tip>();

        try {
            Cursor cursor = db.getDB().query(Constant.DB_TABLE_TIPS, null, null, null, null, null, Constant.DB_TABLE_TIPS_ROW_ID);

            if (cursor.moveToFirst()) {
                do {

                    Tip tip = new Tip();

                    tip.setID(cursor.getInt(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_ID)));
                    tip.setCareId(cursor.getInt(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_CARE_ID)));
                    tip.setText(cursor.getString(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_TEXT)));
                    tip.setPriority(cursor.getInt(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_PRIORITY)));
                    tip.setCreatedAt(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_CREATED_AT)));
                    tip.setModifiedAt(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_MODIFIED_AT)));
                    tip.setInsertedAt(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_INSERTED_AT)));

                    tips.add(tip);

                }
                while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.e(Constant.TAG_LOG, "Error - TipsModel[get() - ArrayList]");
            e.printStackTrace();
        } finally {
            db.close();
        }

		return tips;

	}

	/**
	 * Get random tips in DB
	 * 
	 * @param limit Limit of tips for returns
	 * @return List<Tip>
	 */
	public ArrayList<Tip> getRandom(int limit){
		ArrayList<Tip> tips = new ArrayList<Tip>();

		try{
			String orderBy = "RANDOM()";

            Cursor cursor = db.getDB().query(Constant.DB_TABLE_TIPS, null, null, null, null, null, orderBy,
					Integer.toString(limit));
	
			if (cursor.moveToFirst()) {
	
				do {
					Tip tip = new Tip();
	
					tip.setID(cursor.getInt(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_ID)));
                    tip.setCareId(cursor.getInt(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_CARE_ID)));
					tip.setText(cursor.getString(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_TEXT)));
					tip.setPriority(cursor.getInt(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_PRIORITY)));
					tip.setCreatedAt(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_CREATED_AT)));
					tip.setModifiedAt(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_MODIFIED_AT)));
					tip.setInsertedAt(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_INSERTED_AT)));
	
					tips.add(tip);
				}while (cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.e(Constant.TAG_LOG, "Error - TipsModel[getRandom()]");
			e.printStackTrace();
		} finally {
            db.close();
        }

		return tips;

	}

    /**
     * Get tip in DB with base in Care
     *
     * @param careId Id of care
     * @return {Tip} Tip
     */
    public Tip getBasedInCare(long careId){
        Tip tip = new Tip();

        try{
            String QUERY = "SELECT * FROM " + Constant.DB_TABLE_TIPS + " WHERE " +
                    Constant.DB_TABLE_TIPS_ROW_CARE_ID + " = ? ORDER BY RANDOM() LIMIT 1";

            Cursor cursor = db.getDB().rawQuery(QUERY, new String[] {String.valueOf(careId)});

            if (cursor.moveToFirst()) {
                tip.setID(cursor.getInt(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_ID)));
                tip.setCareId(cursor.getInt(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_CARE_ID)));
                tip.setText(cursor.getString(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_TEXT)));
                tip.setPriority(cursor.getInt(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_PRIORITY)));
                tip.setCreatedAt(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_CREATED_AT)));
                tip.setModifiedAt(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_MODIFIED_AT)));
                tip.setInsertedAt(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_TIPS_ROW_INSERTED_AT)));
            }
        } catch (Exception e) {
            Log.e(Constant.TAG_LOG, "Error - TipsModel[getBasedInCare()]");
            e.printStackTrace();
        } finally {
            db.close();
        }

        return tip;
    }
}
