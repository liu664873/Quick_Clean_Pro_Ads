package com.quickcleanpro.phonecleaner.notification

import com.quickcleanpro.phonecleaner.use.core.navigation.AppRoute
import com.quickcleanpro.phonecleaner.use.core.navigation.NotificationRouteAliases
import com.quickcleanpro.phonecleaner.use.service.notification.ToolNotificationIntentFactory
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NotificationRouteMappingTest {
    @Test
    fun localNotificationContentRoutesResolveToSupportedTargets() {
        val routes = notificationRoutesFrom("app/src/main/res/raw/notification_content.json")

        assertTrue(routes.isNotEmpty())
        assertAllRoutesResolve(routes)
    }

    @Test
    fun remoteNotificationContentRoutesResolveToSupportedTargets() {
        val routes = notificationRoutesFrom("docs/remote_config_quick_clean_pro.json")

        assertTrue(routes.isNotEmpty())
        assertAllRoutesResolve(routes)
    }

    @Test
    fun invalidLegacyRouteDoesNotBlockValidSdkRoute() {
        val route =
            ToolNotificationIntentFactory.resolveTargetRouteCandidates(
                listOf("missing_route", "/junkClean"),
            )

        assertEquals(AppRoute.JunkClean.value, route)
    }

    @Test
    fun routeVariantsNormalizeBeforeResolving() {
        assertEquals(
            AppRoute.JunkClean.value,
            ToolNotificationIntentFactory.resolveTargetRouteCandidates(
                listOf("/junkClean?from=push"),
            ),
        )
        assertEquals(
            AppRoute.NetworkScan.value,
            ToolNotificationIntentFactory.resolveTargetRouteCandidates(
                listOf("/networkScan/"),
            ),
        )
        assertEquals(
            AppRoute.NotificationCleaner.value,
            ToolNotificationIntentFactory.resolveTargetRouteCandidates(
                listOf("notification_bar"),
            ),
        )
        assertEquals(
            AppRoute.NotificationCleaner.value,
            ToolNotificationIntentFactory.resolveTargetRouteCandidates(
                listOf("/notificationClean"),
            ),
        )
    }

    @Test
    fun unknownNotificationRouteFallsBackToHome() {
        assertEquals(
            AppRoute.Home.value,
            ToolNotificationIntentFactory.resolveTargetRouteCandidates(
                listOf("/unknownRoute"),
            ),
        )
    }

    @Test
    fun emptyNotificationRouteCandidatesReturnNull() {
        assertNull(ToolNotificationIntentFactory.resolveTargetRouteCandidates(emptyList()))
    }

    private fun assertAllRoutesResolve(routes: Set<String>) {
        val unresolved =
            routes.filter { route ->
                NotificationRouteAliases.normalize(route) == null ||
                    !ToolNotificationIntentFactory.isValidRoute(route)
            }

        assertTrue("Unresolved notification routes: $unresolved", unresolved.isEmpty())
    }

    private fun notificationRoutesFrom(relativePath: String): Set<String> =
        ROUTE_REGEX
            .findAll(sourceFile(relativePath).readText())
            .map { it.groupValues[1] }
            .toSet()

    private fun sourceFile(relativePath: String): File {
        val startDir = File(requireNotNull(System.getProperty("user.dir"))).absoluteFile
        generateSequence(startDir) { current -> current.parentFile }
            .forEach { dir ->
                val direct = File(dir, relativePath)
                if (direct.isFile) return direct
            }
        error("Cannot find $relativePath from $startDir")
    }

    private companion object {
        val ROUTE_REGEX = Regex("\"Route\"\\s*:\\s*\"([^\"]+)\"")
    }
}
