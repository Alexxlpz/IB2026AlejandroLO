package com.iberdrola.practicas2026.alejandroLO.data

import android.content.Context
import com.iberdrola.practicas2026.alejandroLO.data.repository.Bill.BillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.Bill.OfflineBillsRepository

interface AppContainer {
    val billsRepository: BillsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val billsRepository: BillsRepository by lazy {
        OfflineBillsRepository(BillDatabase.getDatabase(context).billDao())
    }
}