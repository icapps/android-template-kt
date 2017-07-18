package be.icapps.template

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import com.icapps.crashreporter.CompoundCrashReporter
import com.icapps.crashreporter.CrashLog
import crashreporter.crashlytics.CrashlyticsCrashReporter
import crashreporter.googleanalytics.GoogleAnalyticsCrashReporter

class ApplicationProvider : Application(), KodeinAware {

    lateinit override var kodein: Kodein

    private val mTracker: Tracker by lazy {
        val analytics = GoogleAnalytics.getInstance(this)
        analytics.enableAutoActivityReports(this)
        analytics.newTracker(R.xml.analytics)
                .apply {
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

        kodein = Kodein {
            bind<Application>() with singleton { this@ApplicationProvider }
        }


        if (!BuildConfig.DEBUG) {
            val compoundCrashReporter = CompoundCrashReporter()

            compoundCrashReporter.addCrashLogger(CrashlyticsCrashReporter(this))
            compoundCrashReporter.addCrashLogger(GoogleAnalyticsCrashReporter(mTracker))

            CrashLog.initialize(compoundCrashReporter)
            CrashLog.leaveBreadcrumb("App Created")
        }
    }

}