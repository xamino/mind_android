package de.uulm.mi.mind_android;

import android.content.Context;
import de.uulm.mi.mind_android.data.WifiInfo;

import java.util.ArrayList;

/**
 * Created by Tamino Hartmann on 1/31/14.
 */
interface WifiScannerListener {
    /**
     * Method that is called when a new scan result is in.
     *
     * @param wifiInfoArrayList The ArrayList containing the wifi information.
     */
    public void receiveWifiUpdate(ArrayList<WifiInfo> wifiInfoArrayList);

    /**
     * Allow access to the application context from the AsynTask.
     *
     * @return Application context.
     */
    public Context getApplicationContext();
}
