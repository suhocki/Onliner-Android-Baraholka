package kt.school.starlord.di

import kt.school.starlord.domain.system.coroutine.CoroutineContextProvider
import kt.school.starlord.model.system.coroutine.AppCoroutineContextProvider
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Provides instructions on how to maintain database dependencies.
 * Depends on MapperModule.
 */
val appModule = module {
    single { AppCoroutineContextProvider() } bind CoroutineContextProvider::class
}
