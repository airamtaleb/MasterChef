package com.politecnico.masterchef.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version. //reiniciar BBDD mediante onUpgrade
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "votaciones.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Contract.Entry.TABLE_NAME + " (" +
                    Contract.Entry._ID + " INTEGER PRIMARY KEY," +
                    Contract.Entry.NOMBRE_EQUIPO + " TEXT," +
                    Contract.Entry.ID_EVENTO + " TEXT," +
                    Contract.Entry.ID_JUEZ + " TEXT," +
                    Contract.Entry.PRESENTACION + " TEXT," +
                    Contract.Entry.SERVICIO + " TEXT," +
                    Contract.Entry.SABOR + " TEXT," +
                    Contract.Entry.IMAGEN + " TEXT," +
                    Contract.Entry.TRIPTICO + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Contract.Entry.TABLE_NAME;

    public AdminDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {



        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
