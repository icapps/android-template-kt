package be.icapps.template

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log

import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import com.icapps.crashreporter.CompoundCrashReporter
import com.icapps.crashreporter.CrashLog

import crashreporter.crittercism.CrittercismCrashReporter
import crashreporter.googleanalytics.GoogleAnalyticsCrashReporter

class ApplicationProvider : Application() {

    private val mTracker: Tracker by lazy {
        val analytics = GoogleAnalytics.getInstance(this)
        analytics.enableAutoActivityReports(this)
        analytics.newTracker(R.xml.analytics)
                .apply {
                    enableAdvertisingIdCollection(true)
                    enableAutoActivityTracking(true)
                    setAppName(getString(R.string.app_name))
                    enableExceptionReporting(true)

                    try {
                        setAppVersion(packageManager.getPackageInfo(packageName, 0).versionName)
                    } catch (e: PackageManager.NameNotFoundException) {
                        Log.d("Analytics Tracker", e.message)
                    }
                }
    }

    override fun onCreate() {
        super.onCreate()

        if (!BuildConfig.DEBUG) {
            val compoundCrashReporter = CompoundCrashReporter()

            val critConfig = CrittercismCrashReporter.Config()
            critConfig.includeVersionCode = true
            critConfig.logNetworkCalls = false

            compoundCrashReporter.addCrashLogger(CrittercismCrashReporter(this, getString(R.string.crittercism_id), critConfig))
            compoundCrashReporter.addCrashLogger(GoogleAnalyticsCrashReporter(mTracker))

            CrashLog.initialize(compoundCrashReporter)
            CrashLog.leaveBreadcrumb("App Started")
        }
    }

}