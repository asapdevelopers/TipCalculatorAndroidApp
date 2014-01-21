package com.asap.tipcalculator.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_TIPS = "tips";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TIP = "tip";

	public static final String TABLE_TAX = "taxes";
	public static final String COLUMN_TAX = "tax";

	private static final String DATABASE_NAME = "TipCalculatorDB.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String CREATE_TABLE_TIPS = "create table " + TABLE_TIPS
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_TIP + " text not null);" ;
	
	private static final String CREATE_TABLE_TAXES = "create table " + TABLE_TAX
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_TAX + " text not null);";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE_TIPS);
		database.execSQL(CREATE_TABLE_TAXES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAX);
		onCreate(db);
	}

}
