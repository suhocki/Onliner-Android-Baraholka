package kt.school.starlord

import com.squareup.leakcanary.LeakCanary
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
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }
    }
}