package de.uulm.mi.mind_android;

import android.content.Context;
import de.uulm.mi.mind_android.data.WifiInfo;

import java.util.ArrayList;

/**
 * Created by Tamino Hartmann on 1/31/14.
 */
interface WifiScannerListener {
    public void receiveWifiUpdate(ArrayList<WifiInfo> wifiInfoArrayList);
    public Context getApplicationContext();
}
