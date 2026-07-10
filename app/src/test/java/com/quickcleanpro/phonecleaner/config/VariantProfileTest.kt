package com.quickcleanpro.phonecleaner.config

import com.quickcleanpro.phonecleaner.use.brand.AppProfile
import com.quickcleanpro.phonecleaner.use.brand.AppServiceProfile
import com.quickcleanpro.phonecleaner.use.brand.LegalProfile
import com.quickcleanpro.phonecleaner.use.brand.quickCleanProNotificationProfile
import com.quickcleanpro.phonecleaner.use.core.feature.FeatureCatalog
import com.quickcleanpro.phonecleaner.use.core.feature.FeatureKey
import com.quickcleanpro.phonecleaner.use.core.navigation.AppRoute
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VariantProfileTest {
    @Test
    fun featureCatalogMapsNestedRoutesToOwningFeature() {
        assertEquals(FeatureKey.ANTI_VIRUS, FeatureCatalog.featureForRoute(AppRoute.VirusQuickScan.value))
        assertEquals(FeatureKey.NETWORK_SCAN, FeatureCatalog.featureForRoute(AppRoute.NetworkScanDevices.value))
    }

    @Test
    fun featureCatalogDoesNotCarryAdPlacementState() {
        FeatureCatalog.specs.forEach { spec ->
            assertFalse(spec.toString().contains("Ad", ignoreCase = true))
        }
    }

    @Test
    fun appRouteEncodesQueryArgs() {
        val route = AppRoute("detail").withArgs(mapOf("name" to "a b&c", "path" to "/a/b"))

        assertEquals("detail?name=a+b%26c&path=%2Fa%2Fb", route)
    }

    @Test
    fun variantProfileKeepsOnlyNonUiVariantConfiguration() {
        val profile = testProfile()

        assertEquals("quickcleanpro", profile.variantKey)
        assertEquals("quickclean_pro", profile.themeKey)
        assertEquals("https://terms.example", profile.termsOfServiceUrl)
        assertEquals("https://privacy.example", profile.privacyPolicyUrl)
        assertEquals("trustlook-key", profile.trustlookApiKey)
        assertTrue(profile.notificationProfile.enabledToolRoutes.contains(AppRoute.BatteryInfo.value))
    }

    private fun testProfile(): AppProfile =
        AppProfile(
            variantKey = "quickcleanpro",
            appName = "Quick Clean PRO",
            themeKey = "quickclean_pro",
            legalProfile =
                LegalProfile(
                    termsOfServiceUrl = "https://terms.example",
                    privacyPolicyUrl = "https://privacy.example",
                ),
            notificationProfile = quickCleanProNotificationProfile(),
            serviceProfile = AppServiceProfile(trustlookApiKey = "trustlook-key"),
        )
}
