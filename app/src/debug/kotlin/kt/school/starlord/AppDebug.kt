package kt.school.starlord

import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.internal.LeakCanaryInternals
import timber.log.Timber

class AppDebug : App() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initLeakCanary()
    }

    private fun initTimber() {
        val debugTree = Timber.DebugTree()
        Timber.plant(debugTree)
    }

    private fun initLeakCanary() {
        if (!LeakCanary.isInAnalyzerProcess(this) &&
            LeakCanaryInternals.installedRefWatcher == null
        ) {
            LeakCanary.install(this)
        }
    }
}
