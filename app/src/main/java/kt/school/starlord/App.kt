package kt.school.starlord

import android.annotation.SuppressLint
import android.app.Application
import kt.school.starlord.di.categoriesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@SuppressLint("Registered")
open class App : Application() {
//    val pageLoader = PageLoader by inject()

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                    categoriesModule
            )
        }
    }
}