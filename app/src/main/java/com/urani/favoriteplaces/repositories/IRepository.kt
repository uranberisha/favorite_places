package com.urani.favoriteplaces.repositories

interface IRepository<in T> {
    suspend fun insert(table: T)

    suspend fun update(table: T)

    suspend fun delete(table: T)
}
