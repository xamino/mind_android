package de.uulm.mi.mind_android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import de.uulm.mi.mind_android.data.WifiInfo;
import de.uulm.mi.mind_android.database.Database;
import de.uulm.mi.mind_android.img.ScaleImageView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private TextView mainText;
    private final String TAG = "MIND";
    private ArrayList<WifiInfo> wifiList;
    private ScaleImageView siv;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();

        //IMAGE
        siv = (ScaleImageView) findViewById(R.id.img);
        siv.setBackgroundColor(Color.WHITE);
    }

    /**
     * Called on Save Location Button Click
     *
     * @param v
     */
    public void saveLocation(View v) {

        Log.d("MIND|HELP!", "x: " + siv.getCoordX() + " y: " + siv.getCoordY());

        SQLiteDatabase database;

        Database helper = new Database(getApplicationContext());
        database = helper.getWritableDatabase();

        // Add wifi access point levels
        for (WifiInfo chunck : wifiList) {
            database.execSQL("INSERT INTO wifiInfos (posX, posY, ssid, mac, level) " +
                    "VALUES ('" + siv.getCoordX() + "', '" + siv.getCoordY() + "', '" + chunck.SSID + "', '" + chunck.MAC + "', '" + chunck.LEVEL + "')");
        }

        Cursor cur = database.query("wifiInfos", null, null, null, null, null, null);

        // TODO only for debugging, values also need to be deleted sometimes :P
        while (cur.moveToNext()) {
            Log.d(TAG + "|DEBUG", "" + cur.getString(1) + "; " + cur.getString(2) + "; " + cur.getString(3) + "; " + cur.getString(4) + "; " + cur.getString(5));
        }

    }

    private void init() {
        mainText = (TextView) findViewById(R.id.textOutput);
        ResponseReceiver receiver = new ResponseReceiver();
        registerReceiver(receiver, new IntentFilter("testMePlease"));
        startService(new Intent(this, WifiScannerService.class));
    }

    // Broadcast receiver for receiving status updates from the IntentService
    private class ResponseReceiver extends BroadcastReceiver {

        // Prevents instantiation
        private ResponseReceiver() {
        }

        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        public void onReceive(Context context, Intent intent) {
            ArrayList<WifiInfo> infolist = intent.getParcelableArrayListExtra("info");

            Log.d(TAG, "Activity received WifiInfo Intent");

            wifiList = infolist;
        }
    }
}
