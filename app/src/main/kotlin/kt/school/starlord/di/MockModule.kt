package kt.school.starlord.di

import kt.school.starlord.domain.repository.CategoriesRepository
import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.repository.ProductsRepository
import kt.school.starlord.domain.repository.SubcategoriesRepository
import kt.school.starlord.model.repository.mock.MockRepository
import org.koin.dsl.binds
import org.koin.dsl.module

/**
 * Contains mocked dependencies with fake data.
 */
val mockModule = module {
    single { MockRepository() } binds arrayOf(
        SubcategoriesRepository::class,
        CategoriesRepository::class,
        CategoriesWithSubcategoriesRepository::class,
        ProductsRepository::class
    )
}
