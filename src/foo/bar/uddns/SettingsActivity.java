package foo.bar.uddns;

import android.app.PendingIntent;
import android.preference.PreferenceManager;
import foo.bar.uddns.R;
import android.util.Log;
import android.content.SharedPreferences;
import android.view.View.BaseSavedState;
import android.os.Parcelable;
import android.os.Parcel;
import android.app.Activity;
import android.preference.PreferenceActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.preference.EditTextPreference;

import foo.bar.uddns.ConnexionHandler;

public class SettingsActivity extends PreferenceActivity {
	public EditTextPreference server, port, user, password, host;
//	private Button applyButton;
	private static final int REQUEST_CODE = 0;
	private static String TAG = "Settings";

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.preferences);
//		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		prefs.registerOnSharedPreferenceChangeListener(spChanged);
	}
        @Override
        protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Toast.makeText(this, "Applied", Toast.LENGTH_LONG).show();
			}
			else {
				Toast.makeText(this, "Oops", Toast.LENGTH_LONG).show();
			}
		}
	}
	SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
                           SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
   	 public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
               Log.w(TAG, "PREFERENCE CHANGED");
//W/ActivityManager(  157): Unable to start service Intent { cmp=foo.bar.uddns/.ConnexionHandler }: not found
                Intent intent = new Intent(getApplication(), foo.bar.uddns.ConnexionHandler.class);
		if (key == "enable") {
			boolean xtmp = true;
			if ( sharedPreferences.getBoolean("enable", xtmp) == false) {
				stopService(intent);
			}
		} else {
			stopService(intent);
			startService(intent);
		}
         }
	};

	private void handleApply() {
		;
	}

}
