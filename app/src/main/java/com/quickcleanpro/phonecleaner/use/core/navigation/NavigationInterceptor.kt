package com.quickcleanpro.phonecleaner.use.core.navigation

fun interface NavigationInterceptor {
    fun intercept(event: AppNavigationEvent): InterceptResult
}

sealed interface InterceptResult {
    data object Proceed : InterceptResult

    data object Block : InterceptResult

    data class Redirect(
        val event: AppNavigationEvent,
    ) : InterceptResult
}
