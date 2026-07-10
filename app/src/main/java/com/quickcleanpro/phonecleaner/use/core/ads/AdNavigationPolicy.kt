package com.quickcleanpro.phonecleaner.use.core.ads

import com.quickcleanpro.phonecleaner.use.core.feature.FeatureCatalog
import com.quickcleanpro.phonecleaner.use.core.feature.FeatureKey
import com.quickcleanpro.phonecleaner.use.core.navigation.AppRoute

data class RouteEntryAdDecision(
    val feature: FeatureKey,
    val route: String,
)

class AdNavigationPolicy(
    private val routeBlacklist: Set<String> = defaultRouteBlacklist,
    private val sourceBlacklist: Set<String> = defaultSourceBlacklist,
    private val entryWhitelist: Set<String> = defaultEntryWhitelist,
) {
    fun entryAdDecision(
        fromRoute: String?,
        targetRoute: String?,
    ): RouteEntryAdDecision? {
        val normalizedTarget = targetRoute.normalizedRoute() ?: return null
        val normalizedSource = fromRoute.normalizedRoute()

        if (normalizedTarget in routeBlacklist) return null
        if (normalizedSource != null && normalizedSource in sourceBlacklist) return null
        if (normalizedTarget !in entryWhitelist) return null

        val targetFeature = FeatureCatalog.featureForRoute(normalizedTarget) ?: return null
        val sourceFeature = normalizedSource?.let { route -> FeatureCatalog.featureForRoute(route) }
        if (sourceFeature == targetFeature) return null

        return RouteEntryAdDecision(
            feature = targetFeature,
            route = normalizedTarget,
        )
    }

    private fun String?.normalizedRoute(): String? =
        this
            ?.substringBefore("?")
            ?.takeIf(String::isNotBlank)

    companion object {
        private const val ROUTE_HOME_FILE_MANAGER = "home_file_manager"
        private const val ROUTE_HOME_TOOLBOX = "home_toolbox"

        val defaultRouteBlacklist: Set<String> =
            setOf(
                AppRoute.Splash.value,
                AppRoute.OnboardingScan.value,
                AppRoute.Home.value,
                ROUTE_HOME_FILE_MANAGER,
                ROUTE_HOME_TOOLBOX,
                AppRoute.Settings.value,
                AppRoute.ManagePermissions.value,
            )

        val defaultSourceBlacklist: Set<String> =
            setOf(
                AppRoute.Splash.value,
                AppRoute.OnboardingScan.value,
                AppRoute.ManagePermissions.value,
            )

        val defaultEntryWhitelist: Set<String> =
            FeatureCatalog.specs
                .mapTo(linkedSetOf()) { spec -> spec.route.value }
    }
}
