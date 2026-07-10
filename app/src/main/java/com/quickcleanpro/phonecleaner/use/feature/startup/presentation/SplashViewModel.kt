package com.quickcleanpro.phonecleaner.use.feature.startup.presentation

import androidx.lifecycle.ViewModel
import com.quickcleanpro.phonecleaner.use.core.source.local.SharedPreferencesUtils

class SplashViewModel : ViewModel() {
    fun shouldShowOnboardingScan(): Boolean =
        !SharedPreferencesUtils.getBoolean(
            key = SharedPreferencesUtils.KEY_ONBOARDING_SCAN_COMPLETED,
            defaultValue = false,
        )
}
