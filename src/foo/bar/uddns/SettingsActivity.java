package foo.bar.uddns;

import foo.bar.uddns.R;

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

public class SettingsActivity extends PreferenceActivity {
	public EditTextPreference server, port, user, password, host;
//	private Button applyButton;
	private static final int REQUEST_CODE = 0;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.preferences);
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
	private void handleApply() {
		;
	}

}
