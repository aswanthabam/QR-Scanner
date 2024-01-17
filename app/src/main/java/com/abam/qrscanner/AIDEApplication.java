package com.abam.qrscanner;
import android.app.*;
import java.io.*;

import android.content.Context;
import android.content.Intent;
/**import com.google.android.gms.ads.MobileAds;**/

public class AIDEApplication extends Application
{
	private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

	@Override
	public void onCreate() {
		this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

		Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
			Intent intent = new Intent(getApplicationContext(), DebugActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

			intent.putExtra("error", getStackTrace(ex));

			PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 11111, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);


			AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, pendingIntent);

			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(2);

			uncaughtExceptionHandler.uncaughtException(thread, ex);
		});
		super.onCreate();

	}


	private String getStackTrace(Throwable th){
		StringWriter result = new StringWriter();
		PrintWriter printWriter = new PrintWriter(result);
		while(th != null){
			th.printStackTrace(printWriter);
			th = th.getCause();
		}

		return result.toString();
	}
}