package kt.school.starlord.di.module

import kt.school.starlord.model.httpurlconnection.PageLoader
import kt.school.starlord.model.network.NetworkRepository
import kt.school.starlord.model.parser.PageParser
import org.koin.dsl.module

/**
 * Depends on MapperModule
 */
val networkModule = module {

    single { PageLoader() }

    single { PageParser(get()) }

    single { NetworkRepository(get(), get()) }
}