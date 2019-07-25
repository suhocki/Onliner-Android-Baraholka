package kt.school.starlord.di

import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.model.repository.network.NetworkRepository
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Depends on MapperModule.
 */
val networkModule = module {

    single { NetworkRepository(get(), get()) } bind CategoriesWithSubcategoriesRepository::class
}
