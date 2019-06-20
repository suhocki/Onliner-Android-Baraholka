package kt.school.starlord

import android.annotation.SuppressLint
import android.app.Application
import kt.school.starlord.di.databaseModule
import kt.school.starlord.di.mapperModule
import kt.school.starlord.di.networkModule
import kt.school.starlord.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Base class for maintaining global application state.
 */
@SuppressLint("Registered")
open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                mapperModule,
                networkModule,
                databaseModule,
                viewModelModule
            )
        }
    }
}
