package com.quickcleanpro.phonecleaner.use.brand

import android.content.Context
import com.quickcleanpro.phonecleaner.BuildConfig
import com.quickcleanpro.phonecleaner.R

object AppProfileLoader {
    fun load(context: Context): AppProfile =
        AppProfile(
            variantKey = context.getString(R.string.app_profile_key),
            appName = context.getString(R.string.app_name),
            themeKey = context.getString(R.string.app_theme_key),
            legalProfile =
                LegalProfile(
                    termsOfServiceUrl = context.getString(R.string.terms_of_service_url),
                    privacyPolicyUrl = context.getString(R.string.privacy_policy_url),
                ),
            notificationProfile = quickCleanProNotificationProfile(),
            serviceProfile =
                AppServiceProfile(
                    trustlookApiKey = BuildConfig.TRUSTLOOK_API_KEY,
                ),
        )
}
