package kt.school.starlord.di.module

import kt.school.starlord.domain.repository.category.CategoriesNetworkRepository
import kt.school.starlord.domain.repository.product.ProductsNetworkRepository
import kt.school.starlord.model.repository.network.NetworkRepository
import org.koin.dsl.binds
import org.koin.dsl.module

/**
 * Depends on MapperModule.
 */
val networkModule = module {

    single { NetworkRepository(get()) } binds arrayOf(
        CategoriesNetworkRepository::class,
        ProductsNetworkRepository::class
    )
}
