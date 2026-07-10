package com.quickcleanpro.phonecleaner.use.feature.applock.data

import android.content.Context
import com.quickcleanpro.phonecleaner.use.feature.applock.data.AppLockRepositoryImpl
import com.quickcleanpro.phonecleaner.use.service.notification.PersistentNotificationService

object AppLockManager {
    fun enableMonitoring(context: Context) {
        AppPrefsUtils.putBoolean(AppLockRepositoryImpl.KEY_MONITORING_ENABLED, true)
        PersistentNotificationService.enableMonitoring(context)
    }

    fun disableMonitoring(context: Context) {
        AppPrefsUtils.putBoolean(AppLockRepositoryImpl.KEY_MONITORING_ENABLED, false)
        PersistentNotificationService.disableMonitoring(context)
    }

    fun isMonitoringEnabled(): Boolean = AppPrefsUtils.getBoolean(AppLockRepositoryImpl.KEY_MONITORING_ENABLED, true)

    fun isAppLocked(packageName: String): Boolean {
        val lockedStr = AppPrefsUtils.getString(AppLockRepositoryImpl.KEY_LOCKED_PACKAGES, "")
        if (lockedStr.isBlank()) return false
        return packageName in lockedStr.split(',').filter { it.isNotBlank() }.toSet()
    }
}
