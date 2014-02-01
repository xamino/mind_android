package de.uulm.mi.mind_android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import de.uulm.mi.mind_android.data.WifiInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tamino Hartmann on 1/31/14. Class for continously running the wifi locating in the background.
 */
public class WifiScanner extends AsyncTask<WifiScannerListener, Void, Void> {

    // Refresh time of scan in seconds. Values below 5 sec are too short, the scan takes about 5 secs!
    private final long SLEEP_TIME_SEC = 10;

    private WifiManager wifiManager;
    private final String TAG = "WifiScanner";
    private List<ScanResult> wifiList;
    private ArrayList<WifiInfo> wifiData;
    private WifiScannerListener callback;

    /**
     * Runs once to set up the Wifi stuff and to register required parameters.
     *
     * @param params
     * @return
     */
    @Override
    protected Void doInBackground(WifiScannerListener... params) {
        // On start, add listener
        callback = params[0];
        // Now init the wifi stuff
        wifiManager = (WifiManager) callback.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        callback.getApplicationContext().registerReceiver(new WifiScanProcessor(), new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        // Loop to keep scanning
        while (true) {
            // If Wifi is off, kill
            //TODO make sure that the task can be managed from the activity (pause, resume, etc)
            //TODO currently the task keeps running in the background!!!
            if (!wifiManager.isWifiEnabled()) {
                Log.e(TAG, "Wifi Disabled!");
                break;
            } else {
                wifiManager.startScan();
                // Sleep for some time:
                try {
                    Thread.sleep(SLEEP_TIME_SEC * 1000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "WifiScanner crashed! No more updates until restarted!");
                    break;
                }
            }
        }

        return null;
    }

    /**
     * Private class because the main class already extends AsynTask.
     */
    private class WifiScanProcessor extends BroadcastReceiver {
        /**
         * Method that receives the update on Wifi information and gives it to the listener.
         *
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Received data!");
            // Receive data
            wifiList = wifiManager.getScanResults();
            wifiData = new ArrayList<WifiInfo>();
            // parse to our data object
            for (ScanResult scanResult : wifiList) {
                wifiData.add(new WifiInfo(scanResult.SSID, scanResult.BSSID, scanResult.level));
            }
            // deliver
            callback.receiveWifiUpdate(wifiData);
        }
    }
}
