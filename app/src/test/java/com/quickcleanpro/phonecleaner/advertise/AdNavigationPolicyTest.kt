package com.quickcleanpro.phonecleaner.advertise

import com.quickcleanpro.phonecleaner.use.core.ads.AdNavigationPolicy
import com.quickcleanpro.phonecleaner.use.core.feature.FeatureKey
import com.quickcleanpro.phonecleaner.use.core.navigation.AppRoute
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AdNavigationPolicyTest {
    @Test
    fun blacklistedTargetWinsOverWhitelist() {
        val policy =
            AdNavigationPolicy(
                routeBlacklist = setOf(AppRoute.Home.value),
                sourceBlacklist = emptySet(),
                entryWhitelist = setOf(AppRoute.Home.value),
            )

        assertNull(policy.entryAdDecision(AppRoute.Settings.value, AppRoute.Home.value))
    }

    @Test
    fun nonWhitelistedTargetDoesNotShowEntryAd() {
        val decision = AdNavigationPolicy().entryAdDecision(AppRoute.Home.value, AppRoute.Settings.value)

        assertNull(decision)
    }

    @Test
    fun sameFeatureInternalRouteDoesNotShowEntryAd() {
        val policy =
            AdNavigationPolicy(
                routeBlacklist = emptySet(),
                sourceBlacklist = emptySet(),
                entryWhitelist = setOf(AppRoute.VirusQuickScan.value),
            )

        assertNull(policy.entryAdDecision(AppRoute.AntiVirus.value, AppRoute.VirusQuickScan.value))
    }

    @Test
    fun legacyNotificationRouteIsNotWhitelistedByDefault() {
        val decision = AdNavigationPolicy().entryAdDecision(AppRoute.Home.value, "notification_bar")

        assertNull(decision)
    }

    @Test
    fun whitelistedFeatureRouteReturnsDecision() {
        val decision = AdNavigationPolicy().entryAdDecision(AppRoute.Home.value, AppRoute.JunkClean.value)

        assertEquals(FeatureKey.JUNK_CLEAN, decision?.feature)
        assertEquals(AppRoute.JunkClean.value, decision?.route)
    }
}
