package com.quickcleanpro.phonecleaner.use.core.ads

import android.content.Context
import android.util.Log
import com.pdffox.adv.AdvertiseSdk

object AdvertisePreloader {
    private const val TAG = "QuickCleanAdPreload"

    fun preloadStartupAds(context: Context) {
        preload(context, AdvertiseSdk.LOAD_TIME_OPEN_APP, includeOpen = true)
    }

    fun preloadMainPageAds(context: Context) {
        preload(context, AdvertiseSdk.LOAD_TIME_ENTER_FEATURE)
    }

    fun preloadAfterPlayFinish(context: Context) {
        preload(context, AdvertiseSdk.LOAD_TIME_PLAY_FINISH)
    }

    private fun preload(
        context: Context,
        loadTimeKey: String,
        includeOpen: Boolean = false,
    ) {
        runCatching {
            val appContext = context.applicationContext
            if (includeOpen && AdvertiseSdk.canPreloadOpen(loadTimeKey)) {
                AdvertiseSdk.preloadOpen(appContext, loadTimeKey)
            }
            if (AdvertiseSdk.canPreloadInterstitial(loadTimeKey)) {
                AdvertiseSdk.preloadInterstitial(appContext, loadTimeKey)
            }
        }.onFailure { throwable ->
            Log.w(TAG, "preload failed for $loadTimeKey", throwable)
        }
    }
}
