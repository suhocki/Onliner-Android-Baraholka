package kt.school.starlord

import timber.log.Timber

class StarlordAppDebug : StarlordApp() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        val debugTree = Timber.DebugTree()
        Timber.plant(debugTree)
    }
}
