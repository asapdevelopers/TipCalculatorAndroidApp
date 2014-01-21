package com.asap.tipcalculator.dataaccess;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TipsDataSource {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allTipColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_TIP };
	private String[] allTaxColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_TAX };

	public TipsDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Tip createTip(String tip) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TIP, tip);
		long insertId = database
				.insert(MySQLiteHelper.TABLE_TIPS, null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TIPS,
				allTipColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		Tip newTip = cursorToTip(cursor);
		cursor.close();
		return newTip;
	}

	public Tip updateTip(String tip) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TIP, tip);
		long updateID = database.update(MySQLiteHelper.TABLE_TIPS, values,
				MySQLiteHelper.COLUMN_ID + "=1", null);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TIPS,
				allTipColumns, MySQLiteHelper.COLUMN_ID + " = " + updateID,
				null, null, null, null);
		cursor.moveToFirst();
		Tip newTip = cursorToTip(cursor);
		cursor.close();
		return newTip;
	}

	public void deleteTip(Tip tip) {
		long id = tip.getId();
		System.out.println("Tip deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_TIPS, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public long getTipsCount() {
		return DatabaseUtils.queryNumEntries(database,
				MySQLiteHelper.TABLE_TIPS);
	}

	public Tip getLastTip() {
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TIPS,
				allTipColumns, null, null, null, null, null);
		if (cursor.getCount() != 0) {
			cursor.moveToLast();
			Tip tip = cursorToTip(cursor);
			cursor.close();
			return tip;
		}
		return null;
	}

	private Tip cursorToTip(Cursor cursor) {
		Tip tip = new Tip();
		tip.setId(cursor.getLong(0));
		tip.setTip(cursor.getString(1));
		return tip;
	}
	
	//Tax Methods

	public Tax createTax(String tax) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TAX, tax);
		long insertId = database
				.insert(MySQLiteHelper.TABLE_TAX, null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TAX,
				allTaxColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		Tax newTax = cursorToTax(cursor);
		cursor.close();
		return newTax;
	}
	
	public Tax updateTax(String tax) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TAX, tax);
		long updateID = database.update(MySQLiteHelper.TABLE_TAX, values,
				MySQLiteHelper.COLUMN_ID + "=1", null);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TAX,
				allTaxColumns, MySQLiteHelper.COLUMN_ID + " = " + updateID,
				null, null, null, null);
		cursor.moveToFirst();
		Tax newTax = cursorToTax(cursor);
		cursor.close();
		return newTax;
	}
	
	public void deleteTax(Tax tax) {
		long id = tax.getId();
		System.out.println("Tax deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_TAX, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public long getTaxCount() {
		return DatabaseUtils.queryNumEntries(database,
				MySQLiteHelper.TABLE_TAX);
	}

	public Tax getLastTax() {
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TAX,
				allTaxColumns, null, null, null, null, null);
		if (cursor.getCount() != 0) {
			cursor.moveToLast();
			Tax tax = cursorToTax(cursor);
			cursor.close();
			return tax;
		}
		return null;
	}

	private Tax cursorToTax(Cursor cursor) {
		Tax tax = new Tax();
		tax.setId(cursor.getLong(0));
		tax.setTax(cursor.getString(1));
		return tax;
	}
}
