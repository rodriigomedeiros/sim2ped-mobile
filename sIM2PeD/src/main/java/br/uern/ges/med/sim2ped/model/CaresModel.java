package br.uern.ges.med.sim2ped.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.uern.ges.med.sim2ped.beans.Care;
import br.uern.ges.med.sim2ped.model.persistence.db.DataBase;
import br.uern.ges.med.sim2ped.utils.Constant;

public class CaresModel {
	private DataBase db;
	
	public CaresModel(Context context) {
		db = DataBase.getInstance(context);
	}
	
	/**
	 * Insert new care in database
	 * 
	 * @param care
     * @return true if success or false if not
	 */
	public boolean insert(Care care) {

		ContentValues c = new ContentValues();

		c.put(Constant.DB_TABLE_CARES_ROW_ID, care.getID());
		c.put(Constant.DB_TABLE_CARES_ROW_TITLE_CARE, care.getTitleCare());
		c.put(Constant.DB_TABLE_CARES_ROW_CARE_QUESTION, care.getCareQuestion());
		c.put(Constant.DB_TABLE_CARES_ROW_MODIFIED_AT, care.getModifiedAt());
		c.put(Constant.DB_TABLE_CARES_ROW_CREATED_AT, care.getCreatedAt());

		try {
			//db.getDB().insertOrThrow(Constant.DB_TABLE_CARES, null, c);
            db.getDB().insertWithOnConflict(Constant.DB_TABLE_CARES, null, c, SQLiteDatabase.CONFLICT_REPLACE);
			Log.i(Constant.TAG_LOG, "Inserting new care...");
			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

    /**
     * Update care in database
     *
     * @param  care
     * @return true if success or false if not
     */
    public boolean update(Care care) {

        ContentValues c = new ContentValues();

        c.put(Constant.DB_TABLE_CARES_ROW_TITLE_CARE, care.getTitleCare());
        c.put(Constant.DB_TABLE_CARES_ROW_CARE_QUESTION, care.getCareQuestion());
        c.put(Constant.DB_TABLE_CARES_ROW_MODIFIED_AT, care.getModifiedAt());
        c.put(Constant.DB_TABLE_CARES_ROW_CREATED_AT, care.getCreatedAt());

        try {
            db.getDB().update(Constant.DB_TABLE_CARES, c, Constant.DB_TABLE_CARES_ROW_ID + " = ?",
                    new String[] {String.valueOf(care.getID())});
            Log.i(Constant.TAG_LOG, "Updating care...");
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
	 * Delete one Care in DB.
	 * 
	 * @param ID Care
	 * @return Boolean, True for success, False for error.
	 */
	public boolean delete(long ID) {
        int r = 0;

        try {
            r = db.getDB().delete(Constant.DB_TABLE_CARES, Constant.DB_TABLE_CARES_ROW_ID + " = ?",
                    new String[]{Long.toString(ID)});
        } catch (Exception ignored){

        } finally {
            db.close();
        }

        return r > 0;

    }
	
	/**
	 * Get one Care
	 * 
	 * @param ID
	 * @return Care
	 */
	public Care get(long ID) {
        Care care = new Care();

        try {
            Cursor c = db.getDB().query(Constant.DB_TABLE_CARES, null, Constant.DB_TABLE_CARES_ROW_ID + " = ?",
                    new String[]{String.valueOf(ID)}, null, null, null);

            c.moveToFirst();
            care.setID(c.getLong(c.getColumnIndex(Constant.DB_TABLE_CARES_ROW_ID)));
            care.setTitleCare(c.getString(c.getColumnIndex(Constant.DB_TABLE_CARES_ROW_TITLE_CARE)));
            care.setCareQuestion(c.getString(c.getColumnIndex(Constant.DB_TABLE_CARES_ROW_CARE_QUESTION)));
            care.setModifiedAt(c.getLong(c.getColumnIndex(Constant.DB_TABLE_CARES_ROW_MODIFIED_AT)));
            care.setCreatedAt(c.getLong(c.getColumnIndex(Constant.DB_TABLE_CARES_ROW_CREATED_AT)));
            care.setInsertedAt(c.getLong(c.getColumnIndex(Constant.DB_TABLE_CARES_ROW_INSERTED_AT)));
        } catch (Exception ignored){

        } finally {
            db.close();
        }

        return care;
	}

    /**
     * Get all Cares in one Tip in DB
     *
     * @param
     * @return List<Care>
     */
/*	public ArrayList<Care> getAllCaresInTip(long idTip) {

		Cursor c = db.getDB().query(Constant.DB_TABLE_CARES, null, Constant.DB_TABLE_CARES_ROW_TIP_ID + "=?",
				new String[] { String.valueOf(idTip) }, null, null, null);

		ArrayList<Care> cares = new ArrayList<Care>();

		if (c.moveToFirst()) {
			do {

				Care care = new Care();

				care.setID(c.getLong(c.getColumnIndex(Constant.DB_TABLE_CARES_ROW_ID)));
				care.setTipID(c.getLong(c.getColumnIndex(Constant.DB_TABLE_CARES_ROW_TIP_ID)));
				care.setTitleCare(c.getString(c.getColumnIndex(Constant.DB_TABLE_CARES_ROW_TITLE_CARE)));
				care.setCareQuestion(c.getString(c.getColumnIndex(Constant.DB_TABLE_CARES_ROW_TITLE_CARE_IN_QUESTION)));
				care.setModifiedAt(c.getLong(c.getColumnIndex(Constant.DB_TABLE_CARES_ROW_MODIFIED_AT)));
				care.setCreatedAt(c.getLong(c.getColumnIndex(Constant.DB_TABLE_CARES_ROW_CREATED_AT)));
				care.setInsertedAt(c.getLong(c.getColumnIndex(Constant.DB_TABLE_CARES_ROW_INSERTED_AT)));

				cares.add(care);

			}while (c.moveToNext());
		}

		return cares;

	}*/
}
