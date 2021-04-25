package com.urani.favoriteplaces.repositories.place

import androidx.lifecycle.LiveData
import com.urani.favoriteplaces.database.entities.Place
import com.urani.favoriteplaces.repositories.IRepository

interface IPlaceRepository : IRepository<Place> {
    fun getAllPlaces(): LiveData<List<Place>>
    suspend fun deleteAll()
}