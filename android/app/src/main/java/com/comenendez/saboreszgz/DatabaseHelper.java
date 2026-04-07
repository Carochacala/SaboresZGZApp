package com.comenendez.saboreszgz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sabores.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_FAVORITES = "favorites";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_FAVORITES +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "restaurant_id TEXT, " +
                "name TEXT, " +
                "address TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    // Insertar favorito
    public void addFavorite(String restaurantId, String name, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("restaurant_id", restaurantId);
        values.put("name", name);
        values.put("address", address);
        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    // Leer favoritos
    public Cursor getFavorites() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_FAVORITES, null, null, null, null, null, null);
    }
}
