package com.drnkgn.juicejuicejuice.modules

import android.content.Context
import androidx.room.Room
import com.drnkgn.juicejuicejuice.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabaseFactory(
        @ApplicationContext context: Context
    ): @JvmSuppressWildcards () -> AppDatabase {
        return {
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_database"
            ).build()
        }
    }
}