package foo.bar.uddns;

import foo.bar.uddns.R;

import android.app.Activity;
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

public class SettingsActivity extends Activity {
	private EditText server, port, user, password, host;
	private Button applyButton;
	private static final int REQUEST_CODE = 0;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		applyButton = (Button) findViewById(R.id.apply);
		applyButton.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				handleApply();
			}
		});
		server = (EditText) findViewById(R.id.server);
		port = (EditText) findViewById(R.id.port);
		user = (EditText) findViewById(R.id.user);
		password = (EditText) findViewById(R.id.password);
		host =  (EditText) findViewById(R.id.host);
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
