package com.quickcleanpro.phonecleaner.use.app.di

import com.quickcleanpro.phonecleaner.use.feature.antivirus.presentation.VirusScanViewModel
import com.quickcleanpro.phonecleaner.use.feature.applock.presentation.AppLockViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.audios.AudiosManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.documents.DocumentsManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.duplicates.DuplicateFilesManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.largefiles.LargeFilesManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.photoprivacy.PhotoPrivacyManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.photos.PhotosManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.screenshots.ScreenshotsManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.similarphotos.SimilarPhotosManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.videos.VideosManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.home.presentation.HomeViewModel
import com.quickcleanpro.phonecleaner.use.feature.onboarding.presentation.OnboardingScanViewModel
import com.quickcleanpro.phonecleaner.use.feature.junkclean.presentation.JunkCleanViewModel
import com.quickcleanpro.phonecleaner.use.feature.startup.presentation.SplashViewModel
import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.appusage.AppUsageViewModel
import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.common.device.BatteryInfoViewModel
import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.common.device.DeviceInfoViewModel
import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.common.notification.NotificationCleanerViewModel
import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.networkscan.NetworkScanDevicesViewModel
import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.networkscan.NetworkScanViewModel
import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.networkspeed.NetworkSpeedViewModel
import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.networkusage.NetworkUsageViewModel
import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.whatsappcleaner.WhatsAppCleanerViewModel
import com.quickcleanpro.phonecleaner.use.feature.notification.presentation.NotificationPermissionSessionViewModel
import com.quickcleanpro.phonecleaner.use.feature.settings.presentation.ManagePermissionsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule =
    module {
        viewModel { HomeViewModel(get(), get(), get()) }
        viewModel { NotificationPermissionSessionViewModel() }
        viewModel { AppUsageViewModel(get()) }
        viewModel { DeviceInfoViewModel(get(), get(), get(), get()) }
        viewModel { BatteryInfoViewModel(get(), get(), get(), get()) }
        viewModel { NetworkScanViewModel(get(), get()) }
        viewModel { NetworkScanDevicesViewModel(get()) }
        viewModel { NetworkSpeedViewModel(get(), get()) }
        viewModel { NetworkUsageViewModel(get(), Dispatchers.IO) }
        viewModel { WhatsAppCleanerViewModel(androidApplication(), get()) }
        viewModel { NotificationCleanerViewModel(androidApplication(), get()) }
        viewModel { OnboardingScanViewModel(get()) }
        viewModel { JunkCleanViewModel(get(), get(), Dispatchers.IO) }
        viewModel { SplashViewModel() }
        viewModel { PhotosManagerViewModel(get(), Dispatchers.IO) }
        viewModel { ScreenshotsManagerViewModel(get(), Dispatchers.IO) }
        viewModel { VideosManagerViewModel(get(), Dispatchers.IO) }
        viewModel { AudiosManagerViewModel(get(), Dispatchers.IO) }
        viewModel { SimilarPhotosManagerViewModel(get(), Dispatchers.IO) }
        viewModel { PhotoPrivacyManagerViewModel(get(), Dispatchers.IO) }
        viewModel { LargeFilesManagerViewModel(get(), Dispatchers.IO) }
        viewModel { DocumentsManagerViewModel(get(), Dispatchers.IO) }
        viewModel { DuplicateFilesManagerViewModel(get(), Dispatchers.IO) }
        viewModel { VirusScanViewModel(androidApplication(), get(), get()) }
        viewModel { ManagePermissionsViewModel(Dispatchers.IO) }
        viewModel {
            AppLockViewModel(
                application = androidApplication(),
                repository = get(),
                monitoringController = get(),
                ioDispatcher = Dispatchers.IO,
            )
        }
    }
