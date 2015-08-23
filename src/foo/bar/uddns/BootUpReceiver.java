package foo.bar.uddns;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import foo.bar.uddns.R;
import foo.bar.uddns.SettingsActivity;
import foo.bar.uddns.ConnexionHandler;

public class BootUpReceiver extends BroadcastReceiver {
public static final int mId = 4242;
SharedPreferences prefs;
@Override
public void onReceive(Context context, Intent intent) {
	prefs = PreferenceManager.getDefaultSharedPreferences(context);
	boolean xtmp = true;
	if ( prefs.getBoolean("enable", xtmp) == true) {
		Intent j = new Intent("foo.bar.uddns.ConnexionHandler");
		context.startService(j);
	}
	// dirty but else we got no way to access em afterwards
        Intent i = new Intent(context, SettingsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

//	ConnexionHandler.handleCon(context, intent);
//	bNot(context, intent);
}

private void bNot(Context c, Intent i){
	NotificationCompat.Builder mBuilder =
        new NotificationCompat.Builder(c)
        .setSmallIcon(R.drawable.ud)
        .setContentTitle("UDDNS")
        .setContentText("UDDNS is running");
// Creates an explicit intent for an Activity in your app
Intent resultIntent = new Intent(c, foo.bar.uddns.SettingsActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
TaskStackBuilder stackBuilder = TaskStackBuilder.create(c);
// Adds the back stack for the Intent (but not the Intent itself)
stackBuilder.addParentStack(foo.bar.uddns.SettingsActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
stackBuilder.addNextIntent(resultIntent);
PendingIntent resultPendingIntent =
        stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT
        );
mBuilder.setContentIntent(resultPendingIntent);
NotificationManager mNotificationManager =
    (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
mNotificationManager.notify(mId, mBuilder.build());
}
}
