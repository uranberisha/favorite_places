package com.urani.favoriteplaces.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.urani.favoriteplaces.database.FavPlaceDatabase.Companion.VERSION
import com.urani.favoriteplaces.database.dao.PlaceDao
import com.urani.favoriteplaces.database.entities.Place

@Database(
    entities = [
        (Place::class)],
    version = VERSION
)

abstract class FavPlaceDatabase : RoomDatabase() {
    companion object {
        const val VERSION = 1
    }

    abstract fun placeDao(): PlaceDao
}