package com.urani.favoriteplaces.repositories.place

import androidx.lifecycle.LiveData
import com.urani.favoriteplaces.database.dao.PlaceDao
import com.urani.favoriteplaces.database.entities.Place
import javax.inject.Inject

class PlaceRepository@Inject constructor(private val placeDao: PlaceDao) : IPlaceRepository {

    override fun getAllPlaces(): LiveData<List<Place>> {
        return placeDao.getAllPlaces()
    }

    override suspend fun deleteAll() {
        placeDao.deleteAll()
    }

    override suspend fun insert(table: Place) {
        placeDao.insert(table)
    }

    override suspend fun update(table: Place) {
        placeDao.update(table)
    }

    override suspend fun delete(table: Place) {
        placeDao.delete(table)
    }
}