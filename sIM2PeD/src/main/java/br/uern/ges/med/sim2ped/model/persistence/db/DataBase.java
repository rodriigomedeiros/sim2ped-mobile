package br.uern.ges.med.sim2ped.model.persistence.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.ContactsContract;

import br.uern.ges.med.sim2ped.utils.Constant;

public class DataBase {

	private SQLiteDatabase db;
	private SQLiteHelper dbHelper;
	private static DataBase singleton;
    private Context context;

	private DataBase(Context context) {
		dbHelper = new SQLiteHelper(context, Constant.DB_NAME, Constant.DB_VERSION, Constant.CREATE_SQL);
		this.db = dbHelper.getWritableDatabase();
        this.context = context;
	}

	public static synchronized DataBase getInstance(Context contex) {
		if (singleton == null)
			singleton = new DataBase(contex);
		
		return singleton;
	}
	
	public SQLiteDatabase getDB() {
        if(db.isOpen())
            return db;
        else {
            DataBase db = DataBase.getInstance(context);
            return db.getDB();
        }
	}

	public synchronized void close() {
		singleton = null;
		try {
			db.close();
		} catch (SQLiteException e) { 
			e.printStackTrace(); 
		}
	}

}