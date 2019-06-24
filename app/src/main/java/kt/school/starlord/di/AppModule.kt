package kt.school.starlord.di

import kt.school.starlord.model.system.SystemMessageNotifier
import kt.school.starlord.model.system.SystemMessageReceiver
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Provides instructions on how to maintain database dependencies.
 * Depends on MapperModule.
 */
val appModule = module {
    single { SystemMessageNotifier() } bind SystemMessageReceiver::class
}
