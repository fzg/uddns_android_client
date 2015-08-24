package foo.bar.uddns;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class SettingsActivity extends PreferenceActivity {
    private static final String TAG = "Settings";
    final SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
            SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    Log.w(TAG, "PREFERENCE CHANGED: "+key);
                    Intent intent = new Intent(getApplication(), foo.bar.uddns.ConnexionHandler.class);
                    if (key.equals("enable")) {
                        Log.w(TAG,"TROLOLOLO");
                        if (!sharedPreferences.getBoolean("enable", false)) {
                            stopService(intent);
                        } else {
                            stopService(intent);
                            startService(intent);
                        }
                    }
                }
            };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Preference button;

        addPreferencesFromResource(R.xml.preferences);
        prefs.registerOnSharedPreferenceChangeListener(spChanged);
        button = findPreference("enable");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                handleApply();
                return true;
            }
        });}

    private void handleApply() {
        //TODO: essayer de grab le cert
        // erreur / l'afficher
        final String p = "prout";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String sserver = prefs.getString("server", "");
        String sport = prefs.getString("port", "");


        ///SharedPreferences sharedPreferences = getSharedPreferences("", 0);
        CertMgr mgr = new CertMgr(getApplicationContext(), p);
        try {
            mgr.getContext();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mgr.fetchCertFor(sserver, sport);
        } catch (Exception e) { e.printStackTrace(); }
    }

}
