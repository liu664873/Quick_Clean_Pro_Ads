package com.quickcleanpro.phonecleaner.use.app

import com.quickcleanpro.phonecleaner.use.core.navigation.Screen
import com.quickcleanpro.phonecleaner.use.core.startup.AppLaunchRequest
import com.quickcleanpro.phonecleaner.use.core.startup.NotificationLaunchSource
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppNotificationTargetRoutingTest {
    @Test
    fun initialNotificationTargetOnSplashIsConsumedBySplash() {
        val request =
            AppLaunchRequest.NotificationTarget(
                route = Screen.NotificationCleaner.route,
                source = NotificationLaunchSource.InitialIntent,
            )

        assertTrue(shouldLetSplashConsumeNotificationTarget(Screen.Splash.route, request))
        assertFalse(shouldConsumeNotificationTargetImmediately(Screen.Splash.route, request))
    }

    @Test
    fun newIntentNotificationTargetOnSplashIsConsumedImmediately() {
        val request =
            AppLaunchRequest.NotificationTarget(
                route = Screen.NotificationCleaner.route,
                source = NotificationLaunchSource.NewIntent,
            )

        assertFalse(shouldLetSplashConsumeNotificationTarget(Screen.Splash.route, request))
        assertTrue(shouldConsumeNotificationTargetImmediately(Screen.Splash.route, request))
    }
}
