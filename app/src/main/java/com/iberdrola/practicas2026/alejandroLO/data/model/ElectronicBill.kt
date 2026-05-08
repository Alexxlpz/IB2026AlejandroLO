package com.iberdrola.practicas2026.alejandroLO.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "electronicBills",
    foreignKeys = [
        ForeignKey(
            entity = Direction::class,
            parentColumns = ["id"],
            childColumns = ["directionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["directionId"], unique = true)
    ]
)
data class ElectronicBill (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "directionId")
    val directionId: Int = 0,
    val gasBillEmail: String? = null,
    val electricityBillEmail: String? = null
)