package com.quickcleanpro.phonecleaner.advertise

import com.quickcleanpro.phonecleaner.use.core.ads.AdPolicyGate
import com.quickcleanpro.phonecleaner.use.core.ads.AdRuntimeState
import com.quickcleanpro.phonecleaner.use.core.ads.AdScene
import com.quickcleanpro.phonecleaner.use.core.feature.FeatureKey
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdPolicyGateTest {
    @Test
    fun permissionRejectedCanShowWhilePermissionFlowIsClearing() {
        val gate = AdPolicyGate(stateProvider = { AdRuntimeState(permissionFlowActive = true) })

        assertTrue(
            gate.canShowFullScreen(
                scene = AdScene.PermissionRejected(FeatureKey.PHOTOS),
                areaKey = "returnFromFileManageAdv",
            ),
        )
    }

    @Test
    fun permissionRejectedCanShowWhileReturningFromSettings() {
        val gate = AdPolicyGate(stateProvider = { AdRuntimeState(externalActivityReturning = true) })

        assertTrue(
            gate.canShowFullScreen(
                scene = AdScene.PermissionRejected(FeatureKey.PHOTOS),
                areaKey = "returnFromFileManageAdv",
            ),
        )
    }

    @Test
    fun returnHomeStillRespectsPermissionFlowGate() {
        val gate = AdPolicyGate(stateProvider = { AdRuntimeState(permissionFlowActive = true) })

        assertFalse(
            gate.canShowFullScreen(
                scene = AdScene.ReturnHome(FeatureKey.PHOTOS),
                areaKey = "returnFromFileManageAdv",
            ),
        )
    }

    @Test
    fun returnHomeStillRespectsSettingsReturnGate() {
        val gate = AdPolicyGate(stateProvider = { AdRuntimeState(externalActivityReturning = true) })

        assertFalse(
            gate.canShowFullScreen(
                scene = AdScene.ReturnHome(FeatureKey.PHOTOS),
                areaKey = "returnFromFileManageAdv",
            ),
        )
    }
}
