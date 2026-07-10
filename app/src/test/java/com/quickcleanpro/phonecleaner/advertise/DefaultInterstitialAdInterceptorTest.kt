package com.quickcleanpro.phonecleaner.advertise

import com.quickcleanpro.phonecleaner.use.core.ads.AdScene
import com.quickcleanpro.phonecleaner.use.core.ads.DefaultInterstitialAdInterceptor
import com.quickcleanpro.phonecleaner.use.core.common.operation.OperationAction
import com.quickcleanpro.phonecleaner.use.core.feature.FeatureKey
import com.quickcleanpro.phonecleaner.use.core.navigation.AppRoute
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultInterstitialAdInterceptorTest {
    @Test
    fun routeEntryShowsInterstitialBeforeContinue() {
        val recorder = RecordingInterstitials()
        val interceptor = DefaultInterstitialAdInterceptor(recorder::show)
        var continued = false

        interceptor.interceptRouteEntry(
            fromRoute = AppRoute.Home.value,
            targetRoute = AppRoute.JunkClean.value,
        ) {
            continued = true
        }

        assertEquals(listOf(AdScene.EnterFeature(FeatureKey.JUNK_CLEAN, AppRoute.JunkClean.value)), recorder.scenes)
        assertEquals(listOf("route_enter_JUNK_CLEAN_scan"), recorder.requestIds)
        assertTrue(continued)
    }

    @Test
    fun nonEntryRouteContinuesWithoutAd() {
        val recorder = RecordingInterstitials()
        val interceptor = DefaultInterstitialAdInterceptor(recorder::show)
        var continued = false

        interceptor.interceptRouteEntry(
            fromRoute = AppRoute.Home.value,
            targetRoute = AppRoute.Settings.value,
        ) {
            continued = true
        }

        assertTrue(recorder.scenes.isEmpty())
        assertTrue(continued)
    }

    @Test
    fun featureOperationUsesInterstitialWhenSceneExists() {
        val recorder = RecordingInterstitials()
        val interceptor = DefaultInterstitialAdInterceptor(recorder::show)
        var continued = false
        val scene = AdScene.OperationFinished(FeatureKey.JUNK_CLEAN, OperationAction.CLEAN, success = true)

        interceptor.interceptFeatureOperation(
            scene = scene,
            requestId = "operation_finished_JUNK_CLEAN_CLEAN_true",
        ) {
            continued = true
        }

        assertEquals(listOf(scene), recorder.scenes)
        assertTrue(continued)
    }

    @Test
    fun returnHomeFeatureOperationUsesInterstitial() {
        val recorder = RecordingInterstitials()
        val interceptor = DefaultInterstitialAdInterceptor(recorder::show)
        var continued = false
        val scene = AdScene.ReturnHome(FeatureKey.NETWORK_SPEED)

        interceptor.interceptFeatureOperation(
            scene = scene,
            requestId = "return_home_NETWORK_SPEED",
        ) {
            continued = true
        }

        assertEquals(listOf(scene), recorder.scenes)
        assertTrue(continued)
    }

    @Test
    fun routeAdInFlightDropsSecondEntryNavigation() {
        val recorder = RecordingInterstitials(autoContinue = false)
        val interceptor = DefaultInterstitialAdInterceptor(recorder::show)
        var firstContinued = false
        var secondContinued = false

        interceptor.interceptRouteEntry(
            fromRoute = AppRoute.Home.value,
            targetRoute = AppRoute.JunkClean.value,
        ) {
            firstContinued = true
        }
        interceptor.interceptRouteEntry(
            fromRoute = AppRoute.Home.value,
            targetRoute = AppRoute.DeviceInfo.value,
        ) {
            secondContinued = true
        }

        assertEquals(1, recorder.scenes.size)
        assertFalse(firstContinued)
        assertFalse(secondContinued)
    }

    private class RecordingInterstitials(
        private val autoContinue: Boolean = true,
    ) {
        val scenes = mutableListOf<AdScene>()
        val requestIds = mutableListOf<String>()

        fun show(
            scene: AdScene,
            requestId: String,
            onContinue: () -> Unit,
        ) {
            scenes += scene
            requestIds += requestId
            if (autoContinue) onContinue()
        }
    }
}
