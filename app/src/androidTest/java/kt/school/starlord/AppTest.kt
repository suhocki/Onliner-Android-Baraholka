package kt.school.starlord

import timber.log.Timber

class AppTest : App() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        val debugTree = Timber.DebugTree()
        Timber.plant(debugTree)
    }
}
