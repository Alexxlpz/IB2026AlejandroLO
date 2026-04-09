package com.iberdrola.practicas2026.alejandroLO.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Direction")
class Direction {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var street: String = "calle Hermes 10/ Vikingos la mejor serie"
    var city: String = "Madrid"
}