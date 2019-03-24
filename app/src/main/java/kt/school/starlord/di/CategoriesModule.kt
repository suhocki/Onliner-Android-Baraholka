package kt.school.starlord.di

import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.model.MockRepository
import kt.school.starlord.ui.categories.CategoriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val categoriesModule = module {

    single<CategoriesRepository> { MockRepository() }

    viewModel { CategoriesViewModel(get()) }
}