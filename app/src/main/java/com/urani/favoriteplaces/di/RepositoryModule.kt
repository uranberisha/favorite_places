package com.urani.favoriteplaces.di

import com.urani.favoriteplaces.database.dao.PlaceDao
import com.urani.favoriteplaces.repositories.place.PlaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAuthRepository(placeDao: PlaceDao) = PlaceRepository(placeDao)

}