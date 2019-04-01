package kt.school.starlord

import com.squareup.leakcanary.LeakCanary

class AppDebug : App() {

    override fun onCreate() {
        super.onCreate()
        initLeakCanary()
    }

    private fun initLeakCanary() {
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }
    }

}