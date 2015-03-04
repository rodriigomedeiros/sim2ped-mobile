package br.uern.ges.med.sim2ped.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.uern.ges.med.sim2ped.beans.User;
import br.uern.ges.med.sim2ped.model.persistence.db.DataBase;
import br.uern.ges.med.sim2ped.utils.Constant;

/**
 * Created by Rodrigo on 23/08/2014 (13:26).
 *
 *
 * Package:
 */
public class UserModel {

    private DataBase db;

    public UserModel(Context context){
        db = DataBase.getInstance(context);
    }

    /**
     * Insert new user in database
     *
     * @param user Object user
     * @return true if success or false if not
     */
    public boolean insert(User user) {

        ContentValues c = new ContentValues();

        c.put(Constant.DB_TABLE_USER_ROW_ID, user.getId());
        c.put(Constant.DB_TABLE_USER_ROW_NAME, user.getName());
        c.put(Constant.DB_TABLE_USER_ROW_AVATAR_PATH, user.getAvatarPath());

        try {
            //db.getDB().insertOrThrow(Constant.DB_TABLE_USER, null, c);
            db.getDB().insertWithOnConflict(Constant.DB_TABLE_USER, null, c, SQLiteDatabase.CONFLICT_REPLACE);
            Log.i(Constant.TAG_LOG, "Inserting new user...");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }

    }

    /**
     * Update user in database
     * @param user Object user
     * @return boolean true if success or false if not
     */
    public boolean updateUser(User user) {

        ContentValues c = new ContentValues();

        c.put(Constant.DB_TABLE_USER_ROW_NAME, user.getName());
        c.put(Constant.DB_TABLE_USER_ROW_AVATAR_PATH, user.getAvatarPath());

        try {
            db.getDB().update(Constant.DB_TABLE_USER, c, Constant.DB_TABLE_USER_ROW_ID + " = ?",
                    new String[] {String.valueOf(user.getId())});
            Log.i(Constant.TAG_LOG, "Updating user...");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }

    }

    /**
     * Get user in DB
     * @param userId Id of the user
     * @return User
     */
    public User getUser(long userId){
        User user = new User();

        try {
            Cursor cursor = db.getDB().query(Constant.DB_TABLE_USER, null, Constant.DB_TABLE_USER_ROW_ID + " = ?",
                    new String[]{String.valueOf(userId)}, null, null, null);
            cursor.moveToFirst();

            user.setId(cursor.getLong(cursor.getColumnIndex(Constant.DB_TABLE_USER_ROW_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(Constant.DB_TABLE_USER_ROW_NAME)));
            user.setAvatarPath(cursor.getString(cursor.getColumnIndex(Constant.DB_TABLE_USER_ROW_AVATAR_PATH)));
        } catch (Exception ignored) {

        } finally {
            db.close();
        }

        return user;
    }

}