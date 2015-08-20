package foo.bar.uddns;

import java.net.URI;
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
	public static final String TAG = "BackgroundActivity";

	public void onCreate(Bundle savedInstanceState) {
		registerReceiver(new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
				NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
// see http://developer.android.com/reference/java/net/InetAddress.html#getHostAddress()
				ConnexionHandler.handleCon(context, intent);
				Log.i(TAG, "connectivity changed");
			}}, new IntentFilter("android.net.comm.CONNECTIVITY_CHANGE"));
		}
}

