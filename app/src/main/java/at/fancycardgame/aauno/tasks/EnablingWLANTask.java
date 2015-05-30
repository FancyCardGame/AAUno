package at.fancycardgame.aauno.tasks;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.view.View;

import at.fancycardgame.aauno.R;

/**
 * Created by Thomas on 30.05.2015.
 */
public class EnablingWLANTask extends AsyncTask<Void, Void, Void> {
    private Activity a;
    private WifiManager wifiM;
    private ConnectivityManager conM;

    public EnablingWLANTask(Activity activity) {
        this.a = activity;
        this.wifiM = (WifiManager)this.a.getSystemService(Context.WIFI_SERVICE);
        this.conM = (ConnectivityManager)this.a.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void onPreExecute() {
        this.a.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... params) {
        // enable WLAN
        this.wifiM.setWifiEnabled(true);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // mobile network type = 0
                if(conM.getNetworkInfo(0) != null)
                    while(true) {
                        if(conM.getNetworkInfo(0).isConnected() || conM.getNetworkInfo(1).isConnected()) {
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    a.findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
                                }
                            });
                            break;
                        }
                    }
                else
                    // seems like device only has WIFI module (WLAN)
                    while(true) {
                        if(conM.getNetworkInfo(1).isConnected()) {
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    a.findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
                                }
                            });
                            break;
                        }
                    }
            }
        };
        new Thread(r).start();
    }
}
