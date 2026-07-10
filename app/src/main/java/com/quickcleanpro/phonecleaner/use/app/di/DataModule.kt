package com.quickcleanpro.phonecleaner.use.app.di

import com.quickcleanpro.phonecleaner.use.feature.antivirus.data.TrustlookVirusScanEngine
import com.quickcleanpro.phonecleaner.use.feature.antivirus.data.VirusSecurityRepositoryImpl
import com.quickcleanpro.phonecleaner.use.feature.antivirus.domain.VirusScanEngine
import com.quickcleanpro.phonecleaner.use.feature.antivirus.domain.VirusSecurityRepository
import com.quickcleanpro.phonecleaner.use.feature.applock.data.AppLockMonitoringControllerImpl
import com.quickcleanpro.phonecleaner.use.feature.applock.data.AppLockRepositoryImpl
import com.quickcleanpro.phonecleaner.use.feature.toolbox.data.AndroidNetworkInfoReader
import com.quickcleanpro.phonecleaner.use.feature.toolbox.data.AppUsageRepositoryImpl
import com.quickcleanpro.phonecleaner.use.core.repository.impl.BatteryHistoryRepositoryImpl
import com.quickcleanpro.phonecleaner.use.feature.junkclean.data.CleanRepositoryImpl
import com.quickcleanpro.phonecleaner.use.feature.toolbox.data.DeviceInfoRepositoryImpl
import com.quickcleanpro.phonecleaner.use.feature.files.data.FileRepositoryImpl
import com.quickcleanpro.phonecleaner.use.feature.toolbox.data.NetworkRepositoryImpl
import com.quickcleanpro.phonecleaner.use.core.repository.impl.NotificationRepositoryImpl
import com.quickcleanpro.phonecleaner.use.core.repository.impl.SettingsRepositoryImpl
import com.quickcleanpro.phonecleaner.use.feature.toolbox.data.ToolboxRepositoryImpl
import com.quickcleanpro.phonecleaner.use.feature.toolbox.data.source.battery.BatteryHistorySamplerImpl
import com.quickcleanpro.phonecleaner.use.feature.applock.domain.AppLockMonitoringController
import com.quickcleanpro.phonecleaner.use.feature.applock.domain.AppLockRepository
import com.quickcleanpro.phonecleaner.use.feature.toolbox.domain.AppUsageRepository
import com.quickcleanpro.phonecleaner.use.feature.toolbox.domain.BatteryHistorySampler
import com.quickcleanpro.phonecleaner.use.core.repository.BatteryHistoryRepository
import com.quickcleanpro.phonecleaner.use.feature.junkclean.domain.CleanRepository
import com.quickcleanpro.phonecleaner.use.feature.junkclean.domain.CleanSessionStore
import com.quickcleanpro.phonecleaner.use.feature.toolbox.domain.DeviceInfoRepository
import com.quickcleanpro.phonecleaner.use.feature.files.domain.FileRepository
import com.quickcleanpro.phonecleaner.use.feature.toolbox.domain.NetworkInfoReader
import com.quickcleanpro.phonecleaner.use.feature.toolbox.domain.NetworkRepository
import com.quickcleanpro.phonecleaner.use.core.repository.NotificationRepository
import com.quickcleanpro.phonecleaner.use.core.repository.SettingsRepository
import com.quickcleanpro.phonecleaner.use.feature.toolbox.domain.ToolboxRepository
import com.quickcleanpro.phonecleaner.use.feature.junkclean.data.scanner.source.SharedScanState
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule: Module =
    module {
        single<DeviceInfoRepository> { DeviceInfoRepositoryImpl(androidContext()) }
        single<VirusScanEngine> { TrustlookVirusScanEngine(androidContext()) }
        single<VirusSecurityRepository> { VirusSecurityRepositoryImpl(androidContext()) }
        single<AppLockRepository> { AppLockRepositoryImpl(androidContext()) }
        single<AppLockMonitoringController> { AppLockMonitoringControllerImpl(androidContext(), get()) }
        single<AppUsageRepository> { AppUsageRepositoryImpl(androidContext()) }
        single<NetworkInfoReader> { AndroidNetworkInfoReader(androidContext()) }
        single<NetworkRepository> { NetworkRepositoryImpl(androidContext()) }
        single<NotificationRepository> { NotificationRepositoryImpl(androidContext()) }
        single<FileRepository> { FileRepositoryImpl(androidContext()) }
        single<CleanSessionStore> { SharedScanState() }
        single<CleanRepository> { CleanRepositoryImpl(androidContext(), get()) }
        single<ToolboxRepository> { ToolboxRepositoryImpl(get(), get(), get()) }
        single<SettingsRepository> { SettingsRepositoryImpl(androidContext()) }
        single<BatteryHistoryRepository> { BatteryHistoryRepositoryImpl(androidContext()) }
        single<BatteryHistorySampler> {
            BatteryHistorySamplerImpl(
                deviceInfoRepository = get(),
                historyRepository = get(),
            )
        }
    }
