package de.uulm.mi.mind_android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import de.uulm.mi.mind_android.data.WifiInfo;

import java.util.ArrayList;

public class MainActivity extends Activity implements WifiScannerListener {

    private final String TAG = "MIND";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Start Wifiscanner:
        new WifiScanner().execute(this);
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    public void receiveWifiUpdate(ArrayList<WifiInfo> wifiInfoArrayList) {
        TextView text = (TextView) findViewById(R.id.textOutput);
        String out = "";
        for (WifiInfo chunk : wifiInfoArrayList)
            out += chunk.toString() + "\n";
        text.setText(out);
    }
}
