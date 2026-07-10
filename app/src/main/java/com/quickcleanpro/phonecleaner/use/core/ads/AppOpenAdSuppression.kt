package com.quickcleanpro.phonecleaner.use.core.ads

import com.pdffox.adv.AdvertiseSdk
import java.util.concurrent.atomic.AtomicBoolean

object AppOpenAdSuppression {
    private val lock = Any()
    private var activeCount = 0
    private var restoreEnabled: Boolean? = null

    fun acquire(): () -> Unit {
        synchronized(lock) {
            if (activeCount == 0) {
                restoreEnabled = runCatching { AdvertiseSdk.isAppOpenAdEnabled }.getOrDefault(true)
                runCatching { AdvertiseSdk.isAppOpenAdEnabled = false }
            }
            activeCount += 1
        }

        val released = AtomicBoolean(false)
        return {
            if (released.compareAndSet(false, true)) {
                release()
            }
        }
    }

    private fun release() {
        var shouldRestore = false
        var enabledToRestore = true
        synchronized(lock) {
            if (activeCount <= 0) return
            activeCount -= 1
            if (activeCount == 0) {
                shouldRestore = true
                enabledToRestore = restoreEnabled ?: true
                restoreEnabled = null
            }
        }

        if (shouldRestore) {
            runCatching { AdvertiseSdk.isAppOpenAdEnabled = enabledToRestore }
        }
    }
}
