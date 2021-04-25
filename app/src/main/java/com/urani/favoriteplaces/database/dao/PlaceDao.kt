package com.urani.favoriteplaces.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.urani.favoriteplaces.database.entities.Place

@Dao
interface PlaceDao : BaseDao<Place> {

    @Query("SELECT * FROM Place WHERE id=:id")
    suspend fun getById(id: Int): Place

    @Query("SELECT * FROM Place")
    suspend fun getAll(): List<Place>

    @Query("DELETE FROM Place")
    suspend fun deleteAll()

    @Query("select * from Place")
    fun getAllPlaces(): LiveData<List<Place>>
}