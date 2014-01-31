package de.uulm.mi.mind_android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    private WifiManager mngr;
    private WifiScanProcessor proc;
    private List<ScanResult> wifiList;
    private StringBuilder sb;
    private TextView mainText;
    private final String TAG = "MIND";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        init();
    }

    private void init() {
        startService(new Intent(this, WifiScannerService.class));

        mngr = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        proc = new WifiScanProcessor();
        mainText = (TextView) findViewById(R.id.textOutput);

        if (!mngr.isWifiEnabled()){
            Log.e(TAG, "Wifi Disabled!");
        }
        else {
            registerReceiver(proc, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            mngr.startScan();
            mainText.setText("\\nStarting Scan...\\n");
        }
    }

    // Broadcast receiver for receiving status updates from the IntentService
    private class ResponseReceiver extends BroadcastReceiver
    {
        // Prevents instantiation
        private ResponseReceiver() {
        }
        // Called when the BroadcastReceiver gets an Intent it's registered to receive

        public void onReceive(Context context, Intent intent) {

        /*
         * Handle Intents here.
         */

        }
    }

    public void refreshScan(View v){
        mngr.startScan();
        mainText.setText("\\nStarting Scan...\\n");
    }

    protected void onPause() {
        unregisterReceiver(proc);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(proc, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }


    private class WifiScanProcessor extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            sb = new StringBuilder();
            wifiList = mngr.getScanResults();
            for(int i = 0; i < wifiList.size(); i++){
                sb.append(new Integer(i+1).toString() + ".");
                sb.append((wifiList.get(i)).toString());
                sb.append("\\n");
            }
            mainText.setText(sb);

        }
    }
}
