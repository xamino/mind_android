package de.uulm.mi.mind_android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import de.uulm.mi.mind_android.data.WifiInfo;
import de.uulm.mi.mind_android.database.Database;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private TextView mainText;
    private final String TAG = "MIND";
    private ArrayList<WifiInfo> wifiList;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
        // button to force location
        Button forceB = (Button) findViewById(R.id.forceButton);
        forceB.setOnClickListener(new View.OnClickListener() {

            private SQLiteDatabase database;

            @Override
            public void onClick(View v) {
                Database helper = new Database(getApplicationContext());
                database = helper.getWritableDatabase();

                for (WifiInfo chunck : wifiList) {
                    database.execSQL("INSERT INTO wifiInfos (ssid, mac, level) VALUES ('" + chunck.SSID + "', '" + chunck.MAC + "', '" + chunck.LEVEL + "')");
                }

                Cursor cur = database.query("wifiInfos", null, null, null, null, null, null);

                // TODO only for debugging, values also need to be deleted sometimes :P
                while (cur.moveToNext()) {
                    Log.d(TAG+"|DEBUG", "" + cur.getString(1));
                }
            }
        });
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
