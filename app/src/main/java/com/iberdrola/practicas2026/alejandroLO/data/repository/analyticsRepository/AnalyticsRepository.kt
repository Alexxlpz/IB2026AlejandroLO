package com.iberdrola.practicas2026.alejandroLO.data.repository.analyticsRepository

interface AnalyticsRepository {
    fun logScreenView(screenName: String)
    fun logButtonClick(buttonName: String, screenName: String, extraInfo: String = "")
    fun logElectronicBillEmailUpdated(contractType: String, isModification: Boolean)
    fun logVerificationAttempt(contractType: String, attemptNumber: Int)
    fun logChangeMode(isOnline: Boolean)
    fun logSelectDirection(street: String)
    fun logOpenFeedbackSheet(mostrarFeedback: Boolean)
    fun logChangeBillType(type: String)
}