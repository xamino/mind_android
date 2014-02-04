package de.uulm.mi.mind_android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    /**
     * Database version. Incremented manually when the structure of the DB changes.
     */
    private static int DB_VERSION = 1;

    /**
     * Constructur.
     * @param context Application context.
     */
    public Database(Context context) {
        super(context, "LocationDB", null, DB_VERSION);
    }

    /**
     * Creates, if not already exists, our database structure.
     * @param db The database to work on.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS locations ( posID INTEGER NOT NULL PRIMARY KEY, posX INTEGER, posY INTEGER,"
                + "info VARCHAR, wifiOne INTEGER FOREIGN KEY REFERENCES wifiInfos(wifiID),"
                + "wifiTwo INTEGER FOREIGN KEY REFERENCES wifiInfos(wifiID), "
                + "wifiThree INTEGER FOREIGN KEY REFERENCES wifiInfos(wifiID), "
                + "wifiFour INTEGER FOREIGN KEY REFERENCES wifiInfos(wifiID), "
                + "wifiFive INTEGER FOREIGN KEY REFERENCES wifiInfos(wifiID)");
        db.execSQL("CREATE TABLE IF NOT EXISTS wifiInfos ( wifiID INTEGER NOT NULL PRIMARY KEY, bssid VARCHAR, mac"
                + "VARCHAR, level INTEGER )");
    }

    /**
     * Not yet implemented!
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO implement when necessary
    }
}
