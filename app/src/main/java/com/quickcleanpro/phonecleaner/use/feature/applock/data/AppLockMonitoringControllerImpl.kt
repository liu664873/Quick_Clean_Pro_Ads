package com.quickcleanpro.phonecleaner.use.feature.applock.data

import android.content.Context
import com.quickcleanpro.phonecleaner.use.feature.applock.domain.AppLockMonitoringController
import com.quickcleanpro.phonecleaner.use.feature.applock.domain.AppLockRepository
import com.quickcleanpro.phonecleaner.use.service.notification.PersistentNotificationService

internal class AppLockMonitoringControllerImpl(
    context: Context,
    private val repository: AppLockRepository,
) : AppLockMonitoringController {
    private val appContext = context.applicationContext

    override fun enableMonitoring() {
        repository.setMonitoringEnabled(true)
        PersistentNotificationService.enableMonitoring(appContext)
    }

    override fun disableMonitoring() {
        repository.setMonitoringEnabled(false)
        PersistentNotificationService.disableMonitoring(appContext)
    }

    override fun syncMonitoringService() {
        if (repository.isPinSet() &&
            repository.isMonitoringEnabled() &&
            repository.lockedAppCount() > 0 &&
            repository.hasOverlayPermission() &&
            repository.hasUsageAccess()
        ) {
            PersistentNotificationService.enableMonitoring(appContext)
        } else {
            PersistentNotificationService.disableMonitoring(appContext)
        }
    }
}
