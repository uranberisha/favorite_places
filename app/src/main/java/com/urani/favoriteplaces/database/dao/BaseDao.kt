package com.urani.favoriteplaces.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<in T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(table: T)

    @Update
    suspend fun update(table: T)

    @Delete
    suspend fun delete(table: T)
}