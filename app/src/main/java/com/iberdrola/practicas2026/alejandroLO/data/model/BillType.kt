package com.iberdrola.practicas2026.alejandroLO.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "billType")
data class BillType (
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    @NonNull
    var type: String = ""
)