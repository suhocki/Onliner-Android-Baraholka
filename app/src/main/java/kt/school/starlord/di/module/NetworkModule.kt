package kt.school.starlord.di.module

import kt.school.starlord.di.Qualifier
import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.model.network.NetworkRepository
import kt.school.starlord.model.httpurlconnection.PageLoader
import kt.school.starlord.model.parser.PageParser
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Depends on MapperModule
 */
val networkModule = module {

    single { PageLoader() }

    single { PageParser(get()) }

    single<CategoriesRepository>(named(Qualifier.REMOTE)) {
        NetworkRepository(
            get(),
            get()
        )
    }
}