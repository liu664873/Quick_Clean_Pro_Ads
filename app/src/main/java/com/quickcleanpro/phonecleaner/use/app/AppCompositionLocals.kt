package com.quickcleanpro.phonecleaner.use.app

import androidx.compose.runtime.staticCompositionLocalOf
import com.quickcleanpro.phonecleaner.use.core.ads.InterstitialAdInterceptor
import com.quickcleanpro.phonecleaner.use.core.ads.NoOpInterstitialAdInterceptor
import com.quickcleanpro.phonecleaner.use.core.common.operation.FeatureOperationTracker
import com.quickcleanpro.phonecleaner.use.core.common.operation.NoOpFeatureOperationTracker
import com.quickcleanpro.phonecleaner.use.core.common.platform.ExternalActivityLaunchHandler

val LocalExternalActivityLaunchHandler =
    staticCompositionLocalOf { ExternalActivityLaunchHandler() }

val LocalFeatureOperationTracker =
    staticCompositionLocalOf<FeatureOperationTracker> {
        NoOpFeatureOperationTracker
    }

val LocalInterstitialAdInterceptor =
    staticCompositionLocalOf<InterstitialAdInterceptor> {
        NoOpInterstitialAdInterceptor
    }
