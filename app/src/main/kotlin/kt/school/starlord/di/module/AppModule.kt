package kt.school.starlord.di.module

import android.app.Activity
import android.content.res.Configuration
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kt.school.starlord.di.Qualifier
import kt.school.starlord.domain.HtmlParser
import kt.school.starlord.domain.system.coroutine.CoroutineContextProvider
import kt.school.starlord.domain.system.view.ErrorSnackbar
import kt.school.starlord.domain.system.view.ProgressSnackbar
import kt.school.starlord.model.data.android.ResourceManager
import kt.school.starlord.model.data.jsoup.JsoupParser
import kt.school.starlord.model.system.coroutine.AppCoroutineContextProvider
import kt.school.starlord.model.system.view.ErrorSnackbarFeature
import kt.school.starlord.model.system.view.ProgressSnackbarFeature
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import java.util.Locale

/**
 * Provides instructions on how to maintain basic application dependencies.
 */
val appModule = module {

    single { AppCoroutineContextProvider() } bind CoroutineContextProvider::class

    single { ResourceManager(androidContext().resources) }

    single(Qualifier.LOCALIZED) { (locale: Locale) ->
        val configuration = Configuration(androidContext().resources.configuration).apply { setLocale(locale) }
        val context = androidContext().createConfigurationContext(configuration)
        ResourceManager(context.resources)
    }

    single<HtmlParser> { JsoupParser(get(), get()) }

    factory { (activity: Activity) -> ProgressSnackbarFeature(activity) } bind ProgressSnackbar::class

    factory { (activity: Activity) -> ErrorSnackbarFeature(activity) } bind ErrorSnackbar::class

    single { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }
}
