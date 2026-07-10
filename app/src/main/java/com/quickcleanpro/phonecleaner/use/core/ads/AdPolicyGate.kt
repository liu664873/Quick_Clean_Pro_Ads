package com.quickcleanpro.phonecleaner.use.core.ads

data class AdRuntimeState(
    val permissionFlowActive: Boolean = false,
    val externalActivityReturning: Boolean = false,
    val scanningOrCleaning: Boolean = false,
    val fullScreenCoolingDown: Boolean = false,
    val forceDisableAds: Boolean = false,
)

class AdPolicyGate(
    private val stateProvider: () -> AdRuntimeState = { AdRuntimeState() },
) {
    fun canShowFullScreen(scene: AdScene, areaKey: String): Boolean {
        val state = stateProvider()
        if (areaKey.isBlank()) return false
        if (state.forceDisableAds) return false
        if (state.permissionFlowActive && scene !is AdScene.PermissionRejected) return false
        if (state.externalActivityReturning && scene !is AdScene.PermissionRejected) return false
        if (state.scanningOrCleaning) return false
        if (state.fullScreenCoolingDown) return false
        return true
    }
}
