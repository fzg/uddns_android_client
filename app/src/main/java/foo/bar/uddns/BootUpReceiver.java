package foo.bar.uddns;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootUpReceiver extends BroadcastReceiver {
    SharedPreferences prefs;
//TODO: vérifier que c'est bien exécuté au boot.
    @Override
    public void onReceive(Context context, Intent intent) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs.getBoolean("enable", false)) {
            Intent j = new Intent("foo.bar.uddns.ConnexionHandler");
            context.startService(j);
        }
    }

}
