package foo.bar.uddns;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Checks for connectivity changes and runs the updater if necessary
 */
public class BackgroundActivity extends Activity {
    public static final String TAG = "BackgroundActivity";
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                if (prefs.getBoolean("enable", false) && (!noConnectivity)) {
                        Log.i(TAG, "connectivity changed");
                        Intent j = new Intent("foo.bar.uddns.ConnexionHandler");
                        j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startService(j);
                }
            }
        }, new IntentFilter("android.net.comm.CONNECTIVITY_CHANGE"));
    }
}

