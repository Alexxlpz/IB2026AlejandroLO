package com.iberdrola.practicas2026.alejandroLO.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import androidx.room.ForeignKey

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
        )
    ])
data class Bill(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "typeId")
    val typeId: Int = 0,
    @NonNull
    val price: Double = 0.0,
    @ColumnInfo(name = "statusId")
    val statusId: Int = 0,
    @NonNull
    val date: Date = Date(),
    @NonNull
    val dueDate: Date = Date()
)