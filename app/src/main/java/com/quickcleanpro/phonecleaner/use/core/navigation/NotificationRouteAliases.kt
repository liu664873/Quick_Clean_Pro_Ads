package com.quickcleanpro.phonecleaner.use.core.navigation

data class NotificationRouteAlias(
    val rawRoute: String,
    val route: String,
)

object NotificationRouteAliases {
    val mappings: List<NotificationRouteAlias> =
        listOf(
            alias("home", AppRoute.Home.value),
            alias("main", AppRoute.Home.value),
            alias("/main", AppRoute.Home.value),
            alias("junk_files", AppRoute.JunkClean.value),
            alias("clean", AppRoute.JunkClean.value),
            alias("scan", AppRoute.JunkClean.value),
            alias("/scan", AppRoute.JunkClean.value),
            alias("/junkClean", AppRoute.JunkClean.value),
            alias("virus_anti", AppRoute.AntiVirus.value),
            alias("virusAnti", AppRoute.AntiVirus.value),
            alias("/virusAnti", AppRoute.AntiVirus.value),
            alias("app_lock", AppRoute.AppLock.value),
            alias("appLock", AppRoute.AppLock.value),
            alias("/appLock", AppRoute.AppLock.value),
            alias("device_info", AppRoute.DeviceInfo.value),
            alias("deviceInfo", AppRoute.DeviceInfo.value),
            alias("/deviceInfo", AppRoute.DeviceInfo.value),
            alias("battery_info", AppRoute.BatteryInfo.value),
            alias("batteryInfo", AppRoute.BatteryInfo.value),
            alias("/batteryInfo", AppRoute.BatteryInfo.value),
            alias("checkBatteryInfo", AppRoute.BatteryInfo.value),
            alias("/checkBatteryInfo", AppRoute.BatteryInfo.value),
            alias("app_usage", AppRoute.AppUsage.value),
            alias("appUsage", AppRoute.AppUsage.value),
            alias("/appUsage", AppRoute.AppUsage.value),
            alias("network_usage", AppRoute.NetworkUsage.value),
            alias("networkUsage", AppRoute.NetworkUsage.value),
            alias("/networkUsage", AppRoute.NetworkUsage.value),
            alias("network_scan", AppRoute.NetworkScan.value),
            alias("networkScan", AppRoute.NetworkScan.value),
            alias("/networkScan", AppRoute.NetworkScan.value),
            alias("network_speed", AppRoute.NetworkSpeed.value),
            alias("networkSpeed", AppRoute.NetworkSpeed.value),
            alias("/networkSpeed", AppRoute.NetworkSpeed.value),
            alias("whatsapp", AppRoute.WhatsAppCleaner.value),
            alias("/whatsapp", AppRoute.WhatsAppCleaner.value),
            alias("whatsapp_cleaner", AppRoute.WhatsAppCleaner.value),
            alias("notification_bar", AppRoute.NotificationCleaner.value),
            alias("notification_clean", AppRoute.NotificationCleaner.value),
            alias("notificationClean", AppRoute.NotificationCleaner.value),
            alias("/notificationClean", AppRoute.NotificationCleaner.value),
            alias("file_manager", HOME_FILE_MANAGER_ROUTE),
            alias("home_file_manager", HOME_FILE_MANAGER_ROUTE),
            alias("toolbox", HOME_TOOLBOX_ROUTE),
            alias("home_toolbox", HOME_TOOLBOX_ROUTE),
            alias("photos", AppRoute.PhotosManager.value),
            alias("managePhotos", AppRoute.PhotosManager.value),
            alias("/managePhotos", AppRoute.PhotosManager.value),
            alias("similar_photos", AppRoute.SimilarPhotosManager.value),
            alias("manageSimilarPhotos", AppRoute.SimilarPhotosManager.value),
            alias("/manageSimilarPhotos", AppRoute.SimilarPhotosManager.value),
            alias("videos", AppRoute.VideosManager.value),
            alias("manageVideos", AppRoute.VideosManager.value),
            alias("/manageVideos", AppRoute.VideosManager.value),
            alias("large_files", AppRoute.LargeFilesManager.value),
            alias("largeFileManager", AppRoute.LargeFilesManager.value),
            alias("/largeFileManager", AppRoute.LargeFilesManager.value),
            alias("screenshots", AppRoute.ScreenshotsManager.value),
            alias("screenshot_manager", AppRoute.ScreenshotsManager.value),
            alias("/screenshotManager", AppRoute.ScreenshotsManager.value),
        )

    private val aliasByKey: Map<String, String> =
        mappings.associate { it.rawRoute.normalizedKey() to it.route }

    val supportedTargetRoutes: Set<String> =
        mappings.mapTo(mutableSetOf()) { it.route } +
            AppRoute.Home.value +
            HOME_FILE_MANAGER_ROUTE +
            HOME_TOOLBOX_ROUTE

    val knownTargetRoutes: Set<String> = supportedTargetRoutes

    private val supportedRouteByKey: Map<String, String> =
        supportedTargetRoutes.associateBy { it.normalizedKey() }

    fun normalize(rawRoute: String?): String? {
        val key = rawRoute?.normalizedKey()?.takeIf { it.isNotEmpty() } ?: return null
        return aliasByKey[key] ?: supportedRouteByKey[key]
    }

    private fun alias(
        rawRoute: String,
        route: String,
    ): NotificationRouteAlias = NotificationRouteAlias(rawRoute = rawRoute, route = route)

    private fun String.normalizedKey(): String =
        trim()
            .substringBefore("#")
            .substringBefore("?")
            .trim()
            .trim('/')
            .lowercase()

    const val HOME_FILE_MANAGER_ROUTE = "home_file_manager"
    const val HOME_TOOLBOX_ROUTE = "home_toolbox"
}
