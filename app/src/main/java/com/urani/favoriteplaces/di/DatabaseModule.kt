package com.urani.favoriteplaces.di

import android.content.Context
import androidx.room.Room
import com.urani.favoriteplaces.database.FavPlaceDatabase
import com.urani.favoriteplaces.database.dao.PlaceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  DatabaseModule {

    @Provides
    fun provideChannelDao(appDatabase: FavPlaceDatabase): PlaceDao {
        return appDatabase.placeDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): FavPlaceDatabase {
        return Room.databaseBuilder(
            appContext,
            FavPlaceDatabase::class.java,
            "favorite-places-db"
        ).build()
    }
}