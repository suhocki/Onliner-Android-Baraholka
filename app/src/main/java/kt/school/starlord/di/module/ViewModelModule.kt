package kt.school.starlord.di.module

import kt.school.starlord.di.Qualifier
import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.domain.SubcategoriesRepository
import kt.school.starlord.ui.categories.CategoriesViewModel
import kt.school.starlord.ui.subcategories.SubcategoriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Depends on NetworkModule and DatabaseModule
 */
val viewModelModule = module {

    viewModel {
        val remoteCategoriesRepository = get<CategoriesRepository>(named(Qualifier.REMOTE))
        val localCategoriesRepository = get<CategoriesRepository>(named(Qualifier.LOCAL))
        val localSubcategoriesRepository = get<SubcategoriesRepository>()

        CategoriesViewModel(remoteCategoriesRepository, localCategoriesRepository, localSubcategoriesRepository)
    }

    viewModel { SubcategoriesViewModel(get()) }
}