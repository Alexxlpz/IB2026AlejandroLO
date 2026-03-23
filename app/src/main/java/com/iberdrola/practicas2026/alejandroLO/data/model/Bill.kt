package com.iberdrola.practicas2026.alejandroLO.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.BillStatus
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.BillType
import java.sql.Struct
import java.util.Date

// TODO: HACER UNA BASE DE DATOS OCN LOS TIPOS DE FACTURAS Y
//  LOS ESTADOS POSIBLES PARA EVITAR ERROR POR STRING

@Entity(tableName = "bills")
data class Bill(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @NonNull
    val type: String = BillType.LUZ.title,
    @NonNull
    val price: Double = 0.0,
    @NonNull
    val status: String = BillStatus.PENDIENTE.title,
    @NonNull
    val date: Date = Date(),
    @NonNull
    val dueDate: Date = Date()
)