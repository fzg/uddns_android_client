package foo.bar.uddns;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
//import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.Arrays;

public class ConnexionHandler extends Service {
    public static final int mId = 4242;
    //static final String keystore = "uddns.store";
    static final String keystorepass = "FIXME";
    final static private String TAG = "foo.bar.uddns.CnxHndlr";
    Handler handler;
    CertMgr certMgr;
    //SharedPreferences prefs;

    @Override
    public void onCreate() {
        handler = new Handler();
        certMgr = new CertMgr(getApplicationContext(), keystorepass);
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

    @SuppressWarnings("deprecation")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "STARTCMD CALLED");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String sserver = prefs.getString("server", "");
        String suser = prefs.getString("user", "");
        String spassword = prefs.getString("password", "");
        String shost = prefs.getString("host", "");
        String sport = prefs.getString("port", "");

        AsyncHttpClient c = new AsyncHttpClient();

        Uri.Builder b = Uri.parse("https://" + sserver + ":" + sport).buildUpon()
                .appendPath("update4")
                .appendQueryParameter("u", suser)
                .appendQueryParameter("p", spassword)
                .appendQueryParameter("n", shost);
        Log.i(TAG, b.build().toString());
        // try {
            //FIXME: c'est probablement caca v_v
            // SSLContext sC = certMgr.getContext();
            // MyOwnSSLSocketFactory sF = new MyOwnSSLSocketFactory(sC);
            // sF.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            // c.setSSLSocketFactory(sF);
        // } catch (Exception e) {Log.w("HTTPAsync", e.toString());return -1;}
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
                Log.i(TAG, String.valueOf(statusCode)+" "+Arrays.toString(response));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ConnexionHandler.this, "uddns updated.", Toast.LENGTH_LONG).show();
                    }
                });
                bNot(getApplication());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] Response, Throwable e) {
                Log.e(TAG, String.valueOf(statusCode) + " " + Arrays.toString(Response));
                final int sCode = statusCode;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ConnexionHandler.this, "Error "+String.valueOf(sCode), Toast.LENGTH_LONG).show();
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor ed = prefs.edit();
                        ed.putBoolean("enable", false);
                        ed.commit();
                    }
                });
            }
        });
        return (START_STICKY);
    }

    public void bNot(Context c) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(c)
                        .setSmallIcon(R.drawable.ud)
                        .setContentTitle("UDDNS")
                        .setContentText("UDDNS is running");
        Intent resultIntent = new Intent(c, foo.bar.uddns.SettingsActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(c);
        stackBuilder.addParentStack(foo.bar.uddns.SettingsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mId, mBuilder.build());
    }


}
