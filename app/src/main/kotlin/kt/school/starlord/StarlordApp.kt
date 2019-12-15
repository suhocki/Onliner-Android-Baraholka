package kt.school.starlord

import android.annotation.SuppressLint
import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import kt.school.starlord.di.module.appModule
import kt.school.starlord.di.module.databaseModule
import kt.school.starlord.di.module.mapperModule
import kt.school.starlord.di.module.networkModule
import kt.school.starlord.di.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Base class for maintaining global application state.
 */
@SuppressLint("Registered")
open class StarlordApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        startKoin {
            androidLogger()
            androidContext(this@StarlordApp)
            modules(
                listOf(
                    appModule,
                    mapperModule,
                    networkModule,
                    databaseModule,
                    viewModelModule
                )
            )
        }
    }
}
