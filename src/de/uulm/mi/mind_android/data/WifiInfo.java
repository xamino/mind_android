package de.uulm.mi.mind_android.data;

/**
 * Created by Tamino Hartmann on 1/31/14. Data class for storing collected wifi information.
 */
public class WifiInfo {
    public final String BSSID;
    public final String MAC;
    public final long LEVEL;

    public WifiInfo(String BSSID, String MAC, long LEVEL) {
        this.BSSID = BSSID;
        this.MAC = MAC;
        this.LEVEL = LEVEL;
    }
}
