package kt.school.starlord.di

import kt.school.starlord.BuildConfig
import kt.school.starlord.domain.CategoriesWithSubcategoriesRepository
import kt.school.starlord.model.urlconnection.PageLoader
import kt.school.starlord.model.network.NetworkRepository
import kt.school.starlord.model.parser.PageParser
import org.koin.dsl.bind
import org.koin.dsl.module
import java.net.URL

/**
 * Depends on MapperModule.
 */
val networkModule = module {

    single {
        val url = URL(BuildConfig.BARAHOLKA_ONLINER_URL)
        PageLoader(url)
    }

    single { PageParser(get()) }

    single { NetworkRepository(get(), get()) } bind CategoriesWithSubcategoriesRepository::class
}
