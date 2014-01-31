package de.uulm.mi.mind_android.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Tamino Hartmann on 1/31/14. Data class for storing collected wifi information.
 */
public class WifiInfo implements Parcelable {
    public final String SSID;
    public final String MAC;
    public final long LEVEL;

    public WifiInfo(String SSID, String MAC, long LEVEL) {
        this.SSID = SSID;
        this.MAC = MAC;
        this.LEVEL = LEVEL;
    }

    public WifiInfo(Parcel in) {
        Log.d("MIND", "Init WifiInfo from Parcel");
        this.SSID = in.readString();
        this.MAC = in.readString();
        this.LEVEL = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d("MIND", "Write WifiInfo to Parcel");
        dest.writeString (SSID);
        dest.writeString (MAC);
        dest.writeLong(LEVEL);
    }

    public static final Parcelable.Creator<WifiInfo> CREATOR
            = new Parcelable.Creator<WifiInfo>()
    {
        public WifiInfo createFromParcel(Parcel in)
        {
            Log.d("MIND", "Parcel.Creator create WifiInfo from Parcel");
            return new WifiInfo(in);
        }

        public WifiInfo[] newArray (int size)
        {
            Log.d("MIND", "Parcel.Creator create WifiInfo[]");
            return new WifiInfo[size];
        }
    };
}
