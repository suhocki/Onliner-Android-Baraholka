package kt.school.starlord.di

import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.model.data.network.OnlinerApi
import kt.school.starlord.model.data.parser.PageParser
import kt.school.starlord.model.repository.network.NetworkRepository
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Depends on MapperModule.
 */
val networkModule = module {

    single { OnlinerApi() }

    single { PageParser(get()) }

    single { NetworkRepository(get(), get(), get()) } bind CategoriesWithSubcategoriesRepository::class
}
