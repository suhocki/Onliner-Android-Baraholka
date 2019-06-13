package kt.school.starlord

import android.annotation.SuppressLint
import android.app.Application
import kt.school.starlord.di.module.databaseModule
import kt.school.starlord.di.module.networkModule
import kt.school.starlord.di.module.mapperModule
import kt.school.starlord.di.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

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