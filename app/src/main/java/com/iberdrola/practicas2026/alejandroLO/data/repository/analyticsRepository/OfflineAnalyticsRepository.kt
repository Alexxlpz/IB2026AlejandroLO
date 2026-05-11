package com.iberdrola.practicas2026.alejandroLO.data.repository.analyticsRepository

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent

class OfflineAnalyticsRepository(context: Context) : AnalyticsRepository {

    private val analytics = FirebaseAnalytics.getInstance(context)

    override fun logScreenView(screenName: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
    }

    override fun logButtonClick(buttonName: String, screenName: String, extraInfo: String) {
        analytics.logEvent("button_click") {
            param("button_name", buttonName)
            param("screen_name", screenName)
            if (extraInfo.isNotEmpty()) param("extra_info", extraInfo)
        }
    }

    override fun logElectronicBillEmailUpdated(contractType: String, isModification: Boolean) {
        analytics.logEvent("electronic_bill_email_updated") {
            param("contract_type", contractType)
            param("action_type", if (isModification) "modification" else "new_registration")
        }
    }

    override fun logVerificationAttempt(contractType: String, attemptNumber: Int) {
        analytics.logEvent("verification_attempt") {
            param("contract_type", contractType)
            param("attempt_number", attemptNumber.toLong())
        }
    }

    override fun logChangeMode(isOnline: Boolean) {
        val changeMode = if (isOnline) "online" else "offline"
        analytics.logEvent("change_mode") {
            param("is_online", changeMode)
        }
    }

    override fun logSelectDirection(street: String) {
        analytics.logEvent("select_direction") {
            param("street", street)
        }
    }

    override fun logOpenFeedbackSheet(mostrarFeedback: Boolean) {
        analytics.logEvent("open_feedback_sheet") {
            param("mostrar_feedback", "$mostrarFeedback")
        }
    }

    override fun logChangeBillType(type: String) {
        analytics.logEvent("change_bill_type") {
            param("type", type)
        }
    }
}