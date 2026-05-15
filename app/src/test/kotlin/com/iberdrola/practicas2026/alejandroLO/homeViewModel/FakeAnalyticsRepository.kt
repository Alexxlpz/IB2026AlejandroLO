package com.iberdrola.practicas2026.alejandroLO.homeViewModel

import com.iberdrola.practicas2026.alejandroLO.data.repository.analyticsRepository.AnalyticsRepository

class FakeAnalyticsRepository : AnalyticsRepository {
    override fun logScreenView(screenName: String) = Unit
    override fun logButtonClick(buttonName: String, screenName: String, extraInfo: String) = Unit
    override fun logElectronicBillEmailUpdated(contractType: String, isModification: Boolean) = Unit
    override fun logVerificationAttempt(contractType: String, attemptNumber: Int) = Unit
    override fun logChangeMode(isOnline: Boolean) = Unit
    override fun logSelectDirection(street: String) = Unit
    override fun logOpenFeedbackSheet(mostrarFeedback: Boolean) = Unit
    override fun logChangeBillType(type: String) = Unit
}
