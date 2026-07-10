package com.quickcleanpro.phonecleaner.use.core.feature

import com.quickcleanpro.phonecleaner.use.core.navigation.AppRoute

enum class FeatureGroup {
    HOME,
    FILES,
    TOOLBOX,
}

data class FeatureSpec(
    val key: FeatureKey,
    val route: AppRoute,
    val group: FeatureGroup,
)

object FeatureCatalog {
    val specs: List<FeatureSpec> =
        listOf(
            FeatureSpec(FeatureKey.JUNK_CLEAN, AppRoute.JunkClean, FeatureGroup.HOME),
            FeatureSpec(FeatureKey.ANTI_VIRUS, AppRoute.AntiVirus, FeatureGroup.HOME),
            FeatureSpec(FeatureKey.APP_LOCK, AppRoute.AppLock, FeatureGroup.HOME),
            FeatureSpec(FeatureKey.DEVICE_INFO, AppRoute.DeviceInfo, FeatureGroup.TOOLBOX),
            FeatureSpec(FeatureKey.BATTERY_INFO, AppRoute.BatteryInfo, FeatureGroup.TOOLBOX),
            FeatureSpec(FeatureKey.APP_USAGE, AppRoute.AppUsage, FeatureGroup.TOOLBOX),
            FeatureSpec(FeatureKey.NOTIFICATION_CLEANER, AppRoute.NotificationCleaner, FeatureGroup.TOOLBOX),
            FeatureSpec(FeatureKey.WHATSAPP_CLEANER, AppRoute.WhatsAppCleaner, FeatureGroup.TOOLBOX),
            FeatureSpec(FeatureKey.NETWORK_USAGE, AppRoute.NetworkUsage, FeatureGroup.TOOLBOX),
            FeatureSpec(FeatureKey.NETWORK_SCAN, AppRoute.NetworkScan, FeatureGroup.TOOLBOX),
            FeatureSpec(FeatureKey.NETWORK_SPEED, AppRoute.NetworkSpeed, FeatureGroup.TOOLBOX),
            FeatureSpec(FeatureKey.PHOTOS, AppRoute.PhotosManager, FeatureGroup.FILES),
            FeatureSpec(FeatureKey.SIMILAR_PHOTOS, AppRoute.SimilarPhotosManager, FeatureGroup.FILES),
            FeatureSpec(FeatureKey.PHOTO_PRIVACY, AppRoute.PhotoPrivacyManager, FeatureGroup.FILES),
            FeatureSpec(FeatureKey.SCREENSHOTS, AppRoute.ScreenshotsManager, FeatureGroup.FILES),
            FeatureSpec(FeatureKey.VIDEOS, AppRoute.VideosManager, FeatureGroup.FILES),
            FeatureSpec(FeatureKey.AUDIOS, AppRoute.AudiosManager, FeatureGroup.FILES),
            FeatureSpec(FeatureKey.LARGE_FILES, AppRoute.LargeFilesManager, FeatureGroup.FILES),
            FeatureSpec(FeatureKey.DUPLICATE_FILES, AppRoute.DuplicateFilesManager, FeatureGroup.FILES),
            FeatureSpec(FeatureKey.DOCUMENTS, AppRoute.DocumentsManager, FeatureGroup.FILES),
        )

    val byKey: Map<FeatureKey, FeatureSpec> = specs.associateBy(FeatureSpec::key)

    val byRoute: Map<String, FeatureSpec> =
        specs.associateBy { it.route.value } +
            mapOf(
                AppRoute.VirusQuickScan.value to spec(FeatureKey.ANTI_VIRUS),
                AppRoute.VirusDeepScan.value to spec(FeatureKey.ANTI_VIRUS),
                AppRoute.VirusResult.value to spec(FeatureKey.ANTI_VIRUS),
                AppRoute.NoVirusResult.value to spec(FeatureKey.ANTI_VIRUS),
                AppRoute.NetworkScanDevices.value to spec(FeatureKey.NETWORK_SCAN),
            )

    fun spec(key: FeatureKey): FeatureSpec = byKey.getValue(key)

    fun routeFor(feature: FeatureKey): AppRoute? = byKey[feature]?.route

    fun featureForRoute(route: String): FeatureKey? = byRoute[route]?.key

    fun groupFeatures(group: FeatureGroup): Set<FeatureKey> =
        specs.filter { it.group == group }.mapTo(linkedSetOf(), FeatureSpec::key)
}

val fileFeatures: Set<FeatureKey> = FeatureCatalog.groupFeatures(FeatureGroup.FILES)

val toolboxFeatures: Set<FeatureKey> = FeatureCatalog.groupFeatures(FeatureGroup.TOOLBOX)

fun featureForRoute(route: String): FeatureKey? = FeatureCatalog.featureForRoute(route)
