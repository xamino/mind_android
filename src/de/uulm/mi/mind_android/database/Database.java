package de.uulm.mi.mind_android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

    private final String TAG = "MIND|Database";

    /**
     * Database version. Incremented manually when the structure of the DB changes.
     */
    private static int DB_VERSION = 1;

    /**
     * Constructor.
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
        db.execSQL("CREATE TABLE IF NOT EXISTS wifiInfos ( wifiID integer primary key autoincrement, posX INTEGER, posY INTEGER, ssid text, mac "
                + "text, level INTEGER )");
    }

    /**
     * Deletes old database and creates new, empty structure. All data is lost!
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database, this will destroy all previous data!");
        db.execSQL("DROP TABLE IF EXISTS wifiInfos");
        onCreate(db);
    }
}
