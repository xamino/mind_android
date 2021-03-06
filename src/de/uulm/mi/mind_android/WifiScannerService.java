package de.uulm.mi.mind_android;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import de.uulm.mi.mind_android.data.WifiInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cassio on 31.01.14.
 */
public class WifiScannerService extends IntentService {
    private WifiManager mngr;
    private final String TAG = "MIND";
    private WifiScanProcessor proc;
    private boolean waiting = true;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public WifiScannerService() {
        super("WifiScannerService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mngr = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        proc = new WifiScanProcessor();
        registerReceiver(proc, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!mngr.isWifiEnabled()){
            Log.w(TAG, "Wifi Disabled!");
        }
        else {
            while (true) // forever and ever!!!
            {
                waiting = true;
                Log.d("MIND", "Start scanning");
                mngr.startScan();
                while (waiting)  {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // Log.d("MIND","Waiting for scan results..");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(proc);
        super.onDestroy();
    }

    private class WifiScanProcessor extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MIND", "Wifi scan complete");

            StringBuilder sb = new StringBuilder();
            List<ScanResult> wifiList = mngr.getScanResults();
            ArrayList<WifiInfo> wifiInfoList = new ArrayList<WifiInfo>();

            for(ScanResult r : wifiList){
                wifiInfoList.add(new WifiInfo(r.SSID,r.BSSID,r.level));
            }

            Log.d("MIND", "Create and send Intent with WifiInfo");
            Intent wifiInfoListIntent = new Intent("testMePlease");
            wifiInfoListIntent.putParcelableArrayListExtra("info",wifiInfoList);
            sendBroadcast(wifiInfoListIntent);

            waiting = false;
        }
    }
}
