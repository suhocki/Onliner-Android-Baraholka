package kt.school.starlord.di

import kt.school.starlord.domain.CategoriesRepository
import kt.school.starlord.model.NetworkRepository
import kt.school.starlord.model.PageLoader
import kt.school.starlord.ui.categories.CategoriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val categoriesModule = module {

    factory { PageLoader() }

    single<CategoriesRepository> { NetworkRepository(get()) }

    viewModel { CategoriesViewModel(get()) }
}