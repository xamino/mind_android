package de.uulm.mi.mind_android;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import de.uulm.mi.mind_android.data.WifiInfo;

import java.util.ArrayList;

public class MainActivity extends Activity {

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
        // button to force location
        Button forceB = (Button) findViewById(R.id.forceButton);
        forceB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO implement onClick
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
    private class ResponseReceiver extends BroadcastReceiver
    {
        // Prevents instantiation
        private ResponseReceiver() {
        }
        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        public void onReceive(Context context, Intent intent) {
            ArrayList<WifiInfo> infolist = intent.getParcelableArrayListExtra("info");

            Log.d(TAG, "Activity received WifiInfo Intent");

            StringBuilder sb = new StringBuilder();
            for (WifiInfo w : infolist){
                sb.append(w.SSID);
                sb.append('|');
                sb.append(w.MAC);
                sb.append('|');
                sb.append(w.LEVEL);
                sb.append('\n');
            }
            mainText.setText(sb.toString());
        }
    }
}
