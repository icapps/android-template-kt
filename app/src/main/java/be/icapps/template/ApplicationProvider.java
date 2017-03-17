package be.icapps.template;

import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.icapps.crashreporter.CompoundCrashReporter;
import com.icapps.crashreporter.CrashLog;

import crashreporter.crittercism.CrittercismCrashReporter;
import crashreporter.googleanalytics.GoogleAnalyticsCrashReporter;

public class ApplicationProvider extends Application {

	private Tracker mTracker;

	@Override
	public void onCreate() {
		super.onCreate();

		if (!BuildConfig.DEBUG) {
			final CompoundCrashReporter compoundCrashReporter = new CompoundCrashReporter();

			final CrittercismCrashReporter.Config critConfig = new CrittercismCrashReporter.Config();
			critConfig.includeVersionCode = true;
			critConfig.logNetworkCalls = false;

			compoundCrashReporter.addCrashLogger(new CrittercismCrashReporter(this, getString(R.string.crittercism_id), critConfig));
			compoundCrashReporter.addCrashLogger(new GoogleAnalyticsCrashReporter(getTracker()));

			CrashLog.initialize(compoundCrashReporter);
			CrashLog.leaveBreadcrumb("App Started");
		}
	}

	public Tracker getTracker() {
		if (mTracker == null) {
			final GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			analytics.enableAutoActivityReports(this);
			mTracker = analytics.newTracker(R.xml.analytics);
			mTracker.enableAdvertisingIdCollection(true);
			mTracker.enableAutoActivityTracking(true);
			try {
				mTracker.setAppVersion(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
			} catch (PackageManager.NameNotFoundException e) {
				Log.d("Analytics Tracker", e.getMessage());
			}
			mTracker.setAppName(getString(R.string.app_name));
			mTracker.enableExceptionReporting(true);
		}
		return mTracker;
	}
}