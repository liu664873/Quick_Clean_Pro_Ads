package com.quickcleanpro.phonecleaner.use.core.ads

interface InterstitialAdInterceptor {
    fun interceptRouteEntry(
        fromRoute: String?,
        targetRoute: String?,
        onContinue: () -> Unit,
    )

    fun interceptFeatureOperation(
        scene: AdScene?,
        requestId: String,
        onContinue: () -> Unit,
    )
}

object NoOpInterstitialAdInterceptor : InterstitialAdInterceptor {
    override fun interceptRouteEntry(
        fromRoute: String?,
        targetRoute: String?,
        onContinue: () -> Unit,
    ) {
        onContinue()
    }

    override fun interceptFeatureOperation(
        scene: AdScene?,
        requestId: String,
        onContinue: () -> Unit,
    ) {
        onContinue()
    }
}

class DefaultInterstitialAdInterceptor(
    private val showInterstitial: (AdScene, String, () -> Unit) -> Unit = { _, _, onContinue -> onContinue() },
    private val policy: AdNavigationPolicy = AdNavigationPolicy(),
) : InterstitialAdInterceptor {
    private val routeAdLock = Any()
    private var routeAdInFlight = false

    override fun interceptRouteEntry(
        fromRoute: String?,
        targetRoute: String?,
        onContinue: () -> Unit,
    ) {
        val continueOnce = once(onContinue)
        val decision =
            policy.entryAdDecision(
                fromRoute = fromRoute,
                targetRoute = targetRoute,
            )
        if (decision == null) {
            continueOnce()
            return
        }

        val scene = AdScene.EnterFeature(decision.feature, decision.route)
        if (AdPlacementRegistry.interstitialArea(scene).isNullOrBlank()) {
            continueOnce()
            return
        }
        if (!markRouteAdInFlight()) return

        showInterstitial(scene, "route_enter_${decision.feature.name}_${decision.route}") {
            clearRouteAdInFlight()
            continueOnce()
        }
    }

    override fun interceptFeatureOperation(
        scene: AdScene?,
        requestId: String,
        onContinue: () -> Unit,
    ) {
        val continueOnce = once(onContinue)
        if (scene == null) {
            continueOnce()
            return
        }
        showInterstitial(scene, requestId, continueOnce)
    }

    private fun markRouteAdInFlight(): Boolean =
        synchronized(routeAdLock) {
            if (routeAdInFlight) {
                false
            } else {
                routeAdInFlight = true
                true
            }
        }

    private fun clearRouteAdInFlight() {
        synchronized(routeAdLock) {
            routeAdInFlight = false
        }
    }
}
