package de.uulm.mi.mind_android.data;

/**
 * Created by Tamino Hartmann on 1/31/14. Data class for storing collected wifi information.
 */
public class WifiInfo {
    public final String SSID;
    public final String MAC;
    public final int LEVEL;

    public WifiInfo(String SSID, String MAC, int LEVEL) {
        this.SSID = SSID;
        this.MAC = MAC;
        this.LEVEL = LEVEL;
    }

    public String toString() {
        return "SSID: " + SSID + " BSSID: " + MAC + " Level: " + LEVEL;
    }
}
