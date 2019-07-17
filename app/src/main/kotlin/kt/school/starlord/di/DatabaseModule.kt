package kt.school.starlord.di

import androidx.room.Room
import kt.school.starlord.BuildConfig
import kt.school.starlord.domain.repository.CategoriesRepository
import kt.school.starlord.domain.repository.ProductsRepository
import kt.school.starlord.domain.repository.SubcategoriesRepository
import kt.school.starlord.model.data.room.AppDatabase
import kt.school.starlord.model.data.room.DaoManager
import kt.school.starlord.model.repository.database.DatabaseRepository
import kt.school.starlord.model.repository.mock.MockRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

/**
 * Provides instructions on how to maintain database dependencies.
 * Depends on MapperModule.
 */
val databaseModule = module {

    factory {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, BuildConfig.DATABASE_FILE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    factory { get<AppDatabase>().categoryDao() }

    factory { get<AppDatabase>().subcategoryDao() }

    factory { DaoManager(get(), get()) }

    single { DatabaseRepository(get(), get()) } binds arrayOf(
        SubcategoriesRepository::class,
        CategoriesRepository::class
    )

    single { MockRepository() } bind ProductsRepository::class
}
