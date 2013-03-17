package com.yumm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FavoriteRecipesDbHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "yumm";
	private static final int DB_VERSION = 1;
	public static final String TABLE_NAME = "favoriteRecipes";
	public static final String COL_NAME = "rName";
	public static final String COL_THUMB_URL = "rThumbUrl";
	public static final String COL_PUBLISHER = "rSource";
	public static final String COL_URL = "rUrl";
	public static final String COL_DATE = "rDate";
	public static final String[] COLUMNS = { COL_NAME, COL_THUMB_URL, COL_PUBLISHER, COL_URL,
			COL_DATE };
	private static final String STRING_CREATE = "CREATE TABLE " + TABLE_NAME
			+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME + " TEXT, " + COL_THUMB_URL
			+ " TEXT, " + COL_PUBLISHER + " TEXT, " + COL_URL + " TEXT, " + COL_DATE + " DATE);";

	public FavoriteRecipesDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	/**
	 * Returns one Recipe associated with the WHERE clause from the favorites
	 * database. passed.
	 * 
	 * @param db
	 *            Favorites database.
	 * @param where
	 *            SQLite WHERE clause. ie. "COL_NAME = Potatoes"
	 * @return One Recipe matching WHERE clause. If no Recipe exists with that
	 *         specification, returns null.
	 */
	public Recipe getOneFavorite(SQLiteDatabase db, String where) {
		Cursor cursor = db.query(FavoriteRecipesDbHelper.TABLE_NAME, COLUMNS, where, null, null,
				null, null, "1");
		cursor.moveToFirst();
		if (cursor.getCount() != 1) {
			return null;
		}
		Recipe r = new Recipe();
		r.setName(cursor.getString(cursor.getColumnIndex(FavoriteRecipesDbHelper.COL_NAME)));
		r.setThumbUrl(cursor.getString(cursor.getColumnIndex(FavoriteRecipesDbHelper.COL_THUMB_URL)));
		r.setPublisher(cursor.getString(cursor
				.getColumnIndex(FavoriteRecipesDbHelper.COL_PUBLISHER)));
		r.setUrl(cursor.getString(cursor.getColumnIndex(FavoriteRecipesDbHelper.COL_URL)));
		return r;
	}

	/**
	 * Returns a list of every Recipe in the favorites database.
	 * 
	 * @param db
	 *            Favorites database.
	 */
	public List<Recipe> getFavorites(SQLiteDatabase db) {
		List<Recipe> result = new ArrayList<Recipe>();
		Cursor cursor = db.query(FavoriteRecipesDbHelper.TABLE_NAME, null, null, null, null, null,
				null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Recipe r = new Recipe();
			r.setName(cursor.getString(cursor.getColumnIndex(FavoriteRecipesDbHelper.COL_NAME)));
			r.setThumbUrl(cursor.getString(cursor
					.getColumnIndex(FavoriteRecipesDbHelper.COL_THUMB_URL)));
			r.setPublisher(cursor.getString(cursor
					.getColumnIndex(FavoriteRecipesDbHelper.COL_PUBLISHER)));
			r.setUrl(cursor.getString(cursor.getColumnIndex(FavoriteRecipesDbHelper.COL_URL)));
			result.add(r);
		}
		return result;
	}

	/**
	 * Adds a Recipe to the favorites database.
	 * 
	 * @param db
	 *            Favorites database.
	 * @param r
	 *            Recipe to add to favorites.
	 */
	public void addToFavorites(SQLiteDatabase db, Recipe r) {
		// Check if recipe already exists
		Recipe oldRecipe = getOneFavorite(db, COL_NAME + " = '" + r.getName() + "'");
		if (oldRecipe != null) {
			return;
		}
		// Add new recipe to db
		ContentValues cv = new ContentValues();
		cv.put(FavoriteRecipesDbHelper.COL_NAME, r.getName());
		cv.put(FavoriteRecipesDbHelper.COL_THUMB_URL, r.getThumbUrl());
		cv.put(FavoriteRecipesDbHelper.COL_PUBLISHER, r.getPublisher());
		cv.put(FavoriteRecipesDbHelper.COL_URL, r.getUrl());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		cv.put(FavoriteRecipesDbHelper.COL_DATE, dateFormat.format(new Date()));
		db.insert(FavoriteRecipesDbHelper.TABLE_NAME, null, cv);
		Log.v("FavoriteRecipeDbHelper", "Added recipe to favorites.");
	}

	/**
	 * Called when database doesn't exist and needs to be created.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create the database table
		db.execSQL(STRING_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// For now, clear the database and re-create
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}