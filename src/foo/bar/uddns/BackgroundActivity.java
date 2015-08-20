package foo.bar.uddns;

import java.net.URI;
import android.content.Context;

import foo.bar.uddns.SettingsActivity;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.app.Activity;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;

import foo.bar.uddns.ConnexionHandler;

public class BackgroundActivity extends Activity {
	SharedPreferences prefs;
	public static final String TAG = "BackgroundActivity";

	public void onCreate(Bundle savedInstanceState) {
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
		registerReceiver(new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
				boolean xtmp = true;
	                        if ( prefs.getBoolean("enable", xtmp) == true) {
					if (noConnectivity == false) {
	                        	        Log.i(TAG, "connectivity changed");
                				Intent j = new Intent("foo.bar.uddns.ConnexionHandler");
					        j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					        context.startService(j);
				}}
			}}, new IntentFilter("android.net.comm.CONNECTIVITY_CHANGE"));
		}
}

