package com.quickcleanpro.phonecleaner.use.core.navigation

sealed interface AppNavigationEvent {
    data object Back : AppNavigationEvent

    data object Home : AppNavigationEvent

    /** Navigate to a route with optional args (appended as query params). */
    data class Destination(
        val route: String,
        val args: Map<String, String> = emptyMap(),
    ) : AppNavigationEvent

    /** Clear the back stack, then navigate to [route]. */
    data class ReplaceStack(
        val route: String,
    ) : AppNavigationEvent

    /** Remove the current destination from the stack, then navigate to [route]. */
    data class ReplaceCurrent(
        val route: String,
    ) : AppNavigationEvent
}

fun AppNavigationEvent.Destination.finalRoute(): String = AppRoute(route).withArgs(args)
