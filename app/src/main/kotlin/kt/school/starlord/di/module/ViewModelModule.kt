package kt.school.starlord.di.module

import kt.school.starlord.model.system.viewmodel.ErrorViewModelFeature
import kt.school.starlord.model.system.viewmodel.ProgressViewModelFeature
import kt.school.starlord.ui.categories.CategoriesViewModel
import kt.school.starlord.ui.products.ProductsViewModel
import kt.school.starlord.ui.subcategories.SubcategoriesViewModel
import kt.school.starlord.ui.subcategories.entity.UiSubcategory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Depends on NetworkModule and DatabaseModule.
 */
val viewModelModule = module {

    factory { ProgressViewModelFeature() }

    factory { ErrorViewModelFeature() }

    viewModel { CategoriesViewModel(get(), get(), get(), get(), get(), get()) }

    viewModel { (categoryName: String) -> SubcategoriesViewModel(get(), get(), categoryName) }

    viewModel { (subcategory: UiSubcategory) ->
        ProductsViewModel(get(), get(), get(), get(), get(), get(), subcategory)
    }
}
