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

public class BackgroundActivity extends Activity {
	public static final String TAG = "BackgroundActivity";

	public void onCreate(Bundle savedInstanceState) {
		registerReceiver(new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
				NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
// see http://developer.android.com/reference/java/net/InetAddress.html#getHostAddress()
				handleCon(context, intent);
				Log.i(TAG, "connectivity changed");
			}}, new IntentFilter("android.net.comm.CONNECTIVITY_CHANGE"));
		}
	private void handleCon(Context context, Intent intent) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String sserver = sharedPref.getString("server", "");
		String suser = sharedPref.getString("user", "");
		String spassword = sharedPref.getString("password", "");
		String shost = sharedPref.getString("host", "");
		HttpResponse response = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			Uri.Builder b = new Uri.Builder();
			b.scheme("https")
				.authority(sserver)
				.appendPath("update4")
				.appendQueryParameter("u", suser)
				.appendQueryParameter("p", spassword)
				.appendQueryParameter("n", shost);
			request.setURI(new URI(b.build().toString()));
			response = client.execute(request);
		} catch (Exception e) {
			Log.getStackTraceString(e);
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			Toast.makeText(this, "Error updating uddns", Toast.LENGTH_LONG).show();
		}
	}
}
