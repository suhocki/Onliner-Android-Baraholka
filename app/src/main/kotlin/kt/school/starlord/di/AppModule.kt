package kt.school.starlord.di

import android.app.Activity
import kt.school.starlord.domain.system.coroutine.CoroutineContextProvider
import kt.school.starlord.domain.system.view.ErrorSnackbar
import kt.school.starlord.domain.system.view.ProgressSnackbar
import kt.school.starlord.model.system.coroutine.AppCoroutineContextProvider
import kt.school.starlord.model.system.view.ErrorSnackbarFeature
import kt.school.starlord.model.system.view.ProgressSnackbarFeature
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Provides instructions on how to maintain database dependencies.
 * Depends on MapperModule.
 */
val appModule = module {
    single { AppCoroutineContextProvider() } bind CoroutineContextProvider::class

    factory { (activity: Activity) -> ProgressSnackbarFeature(activity) } bind ProgressSnackbar::class

    factory { (activity: Activity) -> ErrorSnackbarFeature(activity) } bind ErrorSnackbar::class
}
