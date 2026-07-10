package com.quickcleanpro.phonecleaner.use.core.startup

import android.content.Intent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppLaunchCoordinatorTest {
    @Test
    fun notificationTargetUsesNewIntentSource() {
        val coordinator =
            coordinator(
                targetRouteResolver = { NOTIFICATION_ROUTE },
            )

        coordinator.onNewIntent(Intent())

        assertEquals(
            AppLaunchRequest.NotificationTarget(
                route = NOTIFICATION_ROUTE,
                source = NotificationLaunchSource.NewIntent,
            ),
            coordinator.pendingRequest.value,
        )
    }

    @Test
    fun consumeRequestIfCurrentDoesNotConsumeNewerRequest() {
        val coordinator =
            coordinator(
                targetRouteResolver = { NOTIFICATION_ROUTE },
            )

        val staleRequest = AppLaunchRequest.Normal
        coordinator.onNewIntent(Intent())

        val consumed = coordinator.consumeRequestIfCurrent(staleRequest)

        assertTrue(!consumed)
        assertEquals(
            AppLaunchRequest.NotificationTarget(
                route = NOTIFICATION_ROUTE,
                source = NotificationLaunchSource.NewIntent,
            ),
            coordinator.pendingRequest.value,
        )
    }

    @Test
    fun initialNotificationTargetUsesInitialIntentSource() {
        val coordinator =
            coordinator(
                targetRouteResolver = { NOTIFICATION_ROUTE },
            )

        coordinator.onCreate(Intent())

        assertEquals(
            AppLaunchRequest.NotificationTarget(
                route = NOTIFICATION_ROUTE,
                source = NotificationLaunchSource.InitialIntent,
            ),
            coordinator.pendingRequest.value,
        )
    }

    private fun coordinator(targetRouteResolver: (Intent?) -> String? = { null }): AppLaunchCoordinator =
        AppLaunchCoordinator(
            targetRouteResolver = targetRouteResolver,
        )

    private companion object {
        const val NOTIFICATION_ROUTE = "notification_cleaner"
    }
}
