package kt.school.starlord.di

import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.repository.ProductsListRepository
import kt.school.starlord.model.repository.network.NetworkRepository
import org.koin.dsl.binds
import org.koin.dsl.module

/**
 * Depends on MapperModule.
 */
val networkModule = module {

    single { NetworkRepository(get(), get()) } binds arrayOf(
        CategoriesWithSubcategoriesRepository::class,
        ProductsListRepository::class
    )
}
