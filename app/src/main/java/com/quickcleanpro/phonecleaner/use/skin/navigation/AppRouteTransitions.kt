package com.quickcleanpro.phonecleaner.use.skin.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import com.quickcleanpro.phonecleaner.use.core.navigation.AppRoute

internal object AppRouteTransitions {
    fun enterTransition(
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        initialRoute: String?,
        targetRoute: String?,
    ): EnterTransition {
        if (initialRoute == null || targetRoute == AppRoute.Splash.value) {
            return EnterTransition.None
        }
        if (initialRoute == AppRoute.Splash.value) {
            return fadeIn(animationSpec = fadeSpec())
        }
        return scope.slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = slideSpec(),
        ) + fadeIn(animationSpec = fadeSpec())
    }

    fun exitTransition(
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        initialRoute: String?,
        targetRoute: String?,
    ): ExitTransition {
        if (initialRoute == null ||
            initialRoute == AppRoute.Splash.value ||
            targetRoute == AppRoute.Splash.value
        ) {
            return ExitTransition.None
        }
        return scope.slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = slideSpec(),
        ) + fadeOut(animationSpec = fadeSpec())
    }

    fun popEnterTransition(
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        initialRoute: String?,
        targetRoute: String?,
    ): EnterTransition {
        if (initialRoute == null || targetRoute == AppRoute.Splash.value) {
            return EnterTransition.None
        }
        if (initialRoute == AppRoute.Splash.value) {
            return fadeIn(animationSpec = fadeSpec())
        }
        return scope.slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = slideSpec(),
        ) + fadeIn(animationSpec = fadeSpec())
    }

    fun popExitTransition(
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        initialRoute: String?,
        targetRoute: String?,
    ): ExitTransition {
        if (initialRoute == null ||
            initialRoute == AppRoute.Splash.value ||
            targetRoute == AppRoute.Splash.value
        ) {
            return ExitTransition.None
        }
        return scope.slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = slideSpec(),
        ) + fadeOut(animationSpec = fadeSpec())
    }

    private fun slideSpec() =
        tween<IntOffset>(
            durationMillis = TransitionDurationMillis,
            easing = FastOutSlowInEasing,
        )

    private fun fadeSpec() =
        tween<Float>(
            durationMillis = TransitionDurationMillis,
            easing = FastOutSlowInEasing,
        )
}

private const val TransitionDurationMillis = 280
