package de.uulm.mi.mind_android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tamino Hartmann on 2/4/14.
 */
public class Database extends SQLiteOpenHelper {

    private static int DB_VERSION = 1;

    public Database(Context context) {
        super(context, "LocationDB", null, DB_VERSION);
    }

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }
}
