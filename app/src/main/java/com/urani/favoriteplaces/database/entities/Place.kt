package com.urani.favoriteplaces.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var imagePath: String = ""
)