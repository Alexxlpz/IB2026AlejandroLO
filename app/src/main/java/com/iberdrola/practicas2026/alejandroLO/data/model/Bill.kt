package com.iberdrola.practicas2026.alejandroLO.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "bills",
    foreignKeys = [
        ForeignKey(
            entity = BillType::class,
            parentColumns = ["id"],
            childColumns = ["typeId"]
        ),
        ForeignKey(
            entity = BillStatus::class,
            parentColumns = ["id"],
            childColumns = ["statusId"]
        ),
        ForeignKey(
            entity = Direction::class,
            parentColumns = ["id"],
            childColumns = ["directionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["typeId"]),
        Index(value = ["statusId"]),
        Index(value = ["directionId"])
    ]
)
data class Bill(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "typeId")
    val typeId: Int = 0,
    val price: Double = 0.0,
    @ColumnInfo(name = "statusId")
    val statusId: Int = 0,
    @ColumnInfo(name = "directionId")
    val directionId: Int = 0,
    val emissionDate: Date = Date(),
    val startDate: Date = Date(),
    val endDate: Date = Date()
)