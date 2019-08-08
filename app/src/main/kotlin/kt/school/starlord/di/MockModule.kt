package kt.school.starlord.di

import kt.school.starlord.domain.repository.CategoriesCacheRepository
import kt.school.starlord.domain.repository.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.repository.product.ProductsRepository
import kt.school.starlord.domain.repository.product.ProductsCacheRepository
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
        CategoriesCacheRepository::class,
        CategoriesWithSubcategoriesRepository::class,
        ProductsRepository::class,
        ProductsCacheRepository::class
    )
}
