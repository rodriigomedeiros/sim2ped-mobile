package br.uern.ges.med.sim2ped.model.persistence.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.uern.ges.med.sim2ped.utils.Constant;

public class SQLiteHelper extends SQLiteOpenHelper {

	private String[] sqlCreate;
	private String[] sqlInsert = {};

	SQLiteHelper(Context c, String dataBaseName, int version, String[] createSQL) {
		super(c, dataBaseName, null, version);
		Log.i(Constant.TAG_LOG, "SQLiteHelper()");
		
		this.sqlCreate = createSQL;
	}
	
	SQLiteHelper(Context c, String dataBaseName, int version, String[] createSQL, String[] insertSQL) {
		super(c, dataBaseName, null, version);
		this.sqlCreate = createSQL;
		this.sqlInsert = insertSQL;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		Log.i(Constant.TAG_LOG, "Creating DataBase...");
		
		for (String script : sqlCreate) {
			try { 
				db.execSQL(script);
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		for (String insert : sqlInsert) {
			try {
				db.execSQL(insert);
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		Log.i(Constant.TAG_LOG, "DataBase created! :)");
	}
	 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}
	 
	
}