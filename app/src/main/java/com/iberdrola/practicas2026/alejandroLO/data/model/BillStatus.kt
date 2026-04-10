package com.iberdrola.practicas2026.alejandroLO.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "billStatus")
data class BillStatus(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    @NonNull
    var status: String = ""
)