package kt.school.starlord.di

import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.domain.CategoriesWithSubcategoriesRepository
import kt.school.starlord.domain.SubcategoriesRepository
import kt.school.starlord.model.mock.MockRepository
import org.koin.dsl.binds
import org.koin.dsl.module

/**
 * Contains mocked dependencies with fake data.
 */
val mockModule = module {
    single { MockRepository() } binds arrayOf(
        SubcategoriesRepository::class,
        CategoriesRepository::class,
        CategoriesWithSubcategoriesRepository::class
    )
}
