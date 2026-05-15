package com.iberdrola.practicas2026.alejandroLO

import android.app.Application
import com.google.firebase.FirebaseApp
import com.iberdrola.practicas2026.alejandroLO.data.AppContainer
import com.iberdrola.practicas2026.alejandroLO.data.AppDataContainer

class IberdrolaApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        container = AppDataContainer(this)
    }
}