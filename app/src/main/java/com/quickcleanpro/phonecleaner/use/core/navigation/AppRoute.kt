package com.quickcleanpro.phonecleaner.use.core.navigation

import java.net.URLEncoder

const val DETAIL_INITIAL_INDEX_ARG = "initialIndex"

@JvmInline
value class AppRoute(val value: String) {
    init {
        require(value.isNotBlank()) { "Route value must not be blank." }
    }

    fun withArgs(args: Map<String, String> = emptyMap()): String {
        if (args.isEmpty()) return value
        val query =
            args.entries.joinToString("&") { (key, rawValue) ->
                "${key.encodeQueryPart()}=${rawValue.encodeQueryPart()}"
            }
        return "$value?$query"
    }

    fun withPathArg(arg: Int): String = "$value/$arg"

    fun withDetailInitialIndex(index: Int): String = "$value/${index.coerceAtLeast(0)}"

    fun detailPattern(): String = "$value/{$DETAIL_INITIAL_INDEX_ARG}"

    override fun toString(): String = value

    companion object {
        val Splash = AppRoute("splash")
        val OnboardingScan = AppRoute("onboarding_scan")
        val Home = AppRoute("home")
        val Settings = AppRoute("settings")
        val ManagePermissions = AppRoute("manage_permissions")
        val JunkClean = AppRoute("scan")
        val AntiVirus = AppRoute("anti_virus")
        val VirusQuickScan = AppRoute("virus_quick_scan")
        val VirusDeepScan = AppRoute("virus_deep_scan")
        val VirusResult = AppRoute("virus_result")
        val NoVirusResult = AppRoute("no_virus_result")
        val AppLock = AppRoute("app_lock")
        val DeviceInfo = AppRoute("device_info")
        val BatteryInfo = AppRoute("battery_info")
        val AppUsage = AppRoute("app_usage")
        val NetworkUsage = AppRoute("network_usage")
        val NetworkScan = AppRoute("network_scan")
        val NetworkScanDevices = AppRoute("network_scan_devices")
        val NetworkSpeed = AppRoute("network_speed")
        val WhatsAppCleaner = AppRoute("whatsapp_cleaner")
        val NotificationCleaner = AppRoute("notification_cleaner")
        val PhotosManager = AppRoute("photos_manager")
        val SimilarPhotosManager = AppRoute("similar_photos_manager")
        val PhotoPrivacyManager = AppRoute("photo_privacy_manager")
        val ScreenshotsManager = AppRoute("screenshots_manager")
        val VideosManager = AppRoute("videos_manager")
        val AudiosManager = AppRoute("audios_manager")
        val LargeFilesManager = AppRoute("large_files_manager")
        val DuplicateFilesManager = AppRoute("duplicate_files_manager")
        val DocumentsManager = AppRoute("documents_manager")
        val PhotosDetail = AppRoute("photos_detail")
        val SimilarPhotosDetail = AppRoute("similar_photos_detail")
        val ScreenshotsDetail = AppRoute("screenshots_detail")
        val VideosDetail = AppRoute("videos_detail")
        val AudiosDetail = AppRoute("audios_detail")
        val LargeFilesDetail = AppRoute("large_files_detail")
        val DocumentsDetail = AppRoute("documents_detail")
    }
}

private fun String.encodeQueryPart(): String =
    URLEncoder.encode(this, Charsets.UTF_8.name())
