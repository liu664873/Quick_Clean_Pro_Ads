package com.quickcleanpro.phonecleaner.use.core.ads

import android.app.Activity
import android.util.Log
import com.pdffox.adv.AdvertiseSdk

data class InterstitialRequest(
    val scene: AdScene,
    val requestId: String,
)

class InterstitialAdManager(
    private val activityProvider: () -> Activity?,
    private val stateProvider: () -> AdRuntimeState = { AdRuntimeState() },
    private val onInFlightChanged: (Boolean) -> Unit = {},
) {
    private val policyGate = AdPolicyGate(stateProvider)
    private val runningRequestIds = linkedSetOf<String>()

    fun show(
        scene: AdScene,
        requestId: String,
        onContinue: () -> Unit,
    ) {
        run(
            request =
                InterstitialRequest(
                    scene = scene,
                    requestId = requestId,
                ),
            onContinue = onContinue,
        )
    }

    fun run(request: InterstitialRequest, onContinue: () -> Unit) {
        if (!markRunning(request.requestId)) {
            Log.w(TAG, "skip interstitial: duplicate requestId=${request.requestId} scene=${request.scene}")
            return
        }
        onInFlightChanged(true)

        val finish =
            once {
                val hasRunningRequests = clearRunning(request.requestId)
                onInFlightChanged(hasRunningRequests)
                onContinue()
            }

        // Interstitial scenes are converted to SDK area keys only at the display adapter boundary.
        val areaKey =
            AdPlacementRegistry.interstitialArea(request.scene)
                ?: run {
                    Log.w(TAG, "skip interstitial: no areaKey scene=${request.scene} requestId=${request.requestId}")
                    return finish()
                }
        val activity =
            activityProvider()
                ?: run {
                    Log.w(TAG, "skip interstitial: activity is null scene=${request.scene} areaKey=$areaKey requestId=${request.requestId}")
                    return finish()
                }

        val state = stateProvider()
        val activityUnavailable = activity.isUnavailable()
        val canShow = !activityUnavailable && policyGate.canShowFullScreen(request.scene, areaKey)
        Log.d(
            TAG,
            "request interstitial scene=${request.scene} areaKey=$areaKey requestId=${request.requestId} activityUnavailable=$activityUnavailable state=$state canShow=$canShow",
        )
        if (!canShow) {
            Log.w(
                TAG,
                "skip interstitial scene=${request.scene} areaKey=$areaKey requestId=${request.requestId} activityUnavailable=$activityUnavailable state=$state",
            )
            finish()
            return
        }

        runCatching {
            AdvertiseSdk.showInterstitialAd(activity, areaKey) {
                Log.d(TAG, "interstitial closed scene=${request.scene} areaKey=$areaKey requestId=${request.requestId}")
                if (!activity.isUnavailable()) {
                    // The next business/route interstitial is warmed after the current full-screen ad closes.
                    AdvertisePreloader.preloadAfterPlayFinish(activity)
                }
                finish()
            }
        }.onFailure { throwable ->
            Log.w(TAG, "show interstitial failed for $areaKey", throwable)
            finish()
        }
    }

    private fun markRunning(requestId: String): Boolean =
        synchronized(runningRequestIds) {
            if (requestId in runningRequestIds) {
                false
            } else {
                runningRequestIds.add(requestId)
                true
            }
        }

    private fun clearRunning(requestId: String): Boolean =
        synchronized(runningRequestIds) {
            runningRequestIds.remove(requestId)
            runningRequestIds.isNotEmpty()
        }

    private fun Activity.isUnavailable(): Boolean = isFinishing || isDestroyed

    private companion object {
        const val TAG = "QuickCleanInterAd"
    }
}
