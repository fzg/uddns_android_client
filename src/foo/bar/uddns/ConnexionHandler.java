package foo.bar.uddns;
import android.util.Log;

import android.os.Handler;
import android.os.IBinder;
import android.app.Service;
import java.security.KeyStore;
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
import org.apache.http.Header;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;

import com.loopj.android.http.*;


public class ConnexionHandler extends Service {
	Handler handler;
	SharedPreferences prefs;
	final static private String TAG = "ConnexionHandler";

	@Override
	public void onCreate() {
		handler = new Handler();
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		super.onCreate();
	}
	private void runOnUiThread(Runnable runnable) {
		handler.post(runnable);
	}
	@Override
	public void onDestroy() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.w(TAG, "BIND CALLED");
		return null;
	}

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
                Log.w(TAG, "STARTCMD CALLED");

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        	String sserver = prefs.getString("server", "");
        	String suser = prefs.getString("user", "");
        	String spassword = prefs.getString("password", "");
        	String shost = prefs.getString("host", "");

        	AsyncHttpClient c = new AsyncHttpClient();


        	Uri.Builder b = new Uri.Builder();
        	b.scheme("https")
                	.authority(sserver)
                	.appendPath("update4")
                	.appendQueryParameter("u", suser)
                	.appendQueryParameter("p", spassword)
                	.appendQueryParameter("n", shost);
        	Log.i(TAG, b.build().toString());
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null,null);
			MySSLSocketFactory sF = new MySSLSocketFactory(trustStore);
			sF.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                	c.setSSLSocketFactory(sF);
		} catch (Exception e) {
			;
		}
		c.get(b.build().toString(), new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
	                        		Toast.makeText(ConnexionHandler.this, "Updating uddns...", Toast.LENGTH_LONG).show();
					}
				});
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        	runOnUiThread(new Runnable() {
                        		@Override
                        		public void run() {
                        			Toast.makeText(ConnexionHandler.this, "uddns updated.", Toast.LENGTH_LONG).show();
                                	}
                        	});
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] Response, Throwable e) {
                        	runOnUiThread(new Runnable() {
                        		@Override
                        		public void run() {
						Toast.makeText(ConnexionHandler.this, "Error updating uddns.", Toast.LENGTH_LONG).show();
                                	}
                        	});
			}
		});
		return(START_STICKY);
		}
	}
