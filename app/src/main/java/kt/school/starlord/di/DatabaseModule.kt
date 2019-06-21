package kt.school.starlord.di

import androidx.room.Room
import kt.school.starlord.BuildConfig
import kt.school.starlord.domain.SubcategoriesRepository
import kt.school.starlord.model.room.AppDatabase
import kt.school.starlord.model.room.DaoManager
import kt.school.starlord.model.room.RoomRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
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

    single { RoomRepository(get(), get()) } bind SubcategoriesRepository::class
}
