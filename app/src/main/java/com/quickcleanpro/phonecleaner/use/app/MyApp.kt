package com.quickcleanpro.phonecleaner.use.app

import android.app.Application
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.pdffox.adv.AdvertiseSdk
import com.pdffox.adv.notification.NotificationManager as AdvertiseNotificationManager
import com.quickcleanpro.phonecleaner.BuildConfig
import com.quickcleanpro.phonecleaner.R
import com.quickcleanpro.phonecleaner.use.core.ads.AdvertiseConfigFactory
import com.quickcleanpro.phonecleaner.use.brand.AppProfiles
import com.quickcleanpro.phonecleaner.use.app.di.dataModule
import com.quickcleanpro.phonecleaner.use.app.di.presentationModule
import com.quickcleanpro.phonecleaner.use.core.analytics.AnalyticsTracker
import com.quickcleanpro.phonecleaner.use.core.source.local.SharedPreferencesUtils
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : Application() {
    companion object {
        lateinit var instance: MyApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppProfiles.initialize(context = this)
        SharedPreferencesUtils.init(this)
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(dataModule, presentationModule)
        }

        runCatching {
            runBlocking {
                AdvertiseSdk.init(
                    context = this@MyApp,
                    isTest = BuildConfig.DEBUG,
                    sdkConfig = AdvertiseConfigFactory.create(this@MyApp),
                )
            }
            AnalyticsTracker.initialize()
            ProcessLifecycleOwner.get().lifecycle.addObserver(AppAnalyticsLifecycleObserver)
            loadSdkNotificationDefaultsIfNeeded()
            Log.d("MyApplication", "AdvertiseSdk init success")
        }.onFailure { throwable ->
            Log.e("MyApplication", "AdvertiseSdk init failed", throwable)
        }
    }

    private fun loadSdkNotificationDefaultsIfNeeded() {
        runCatching {

            resources.openRawResource(R.raw.notification_content)
                .bufferedReader()
                .use { it.readText() }
                .takeIf { it.isNotBlank() }
                ?.let(AdvertiseNotificationManager::updateNotificationContent)

            Log.d("MyApplication", "Advertise notification defaults loaded")
        }.onFailure { throwable ->
            Log.e("MyApplication", "Advertise notification defaults load failed", throwable)
        }
    }

    private object AppAnalyticsLifecycleObserver : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            AnalyticsTracker.onAppForeground()
        }

        override fun onStop(owner: LifecycleOwner) {
            AnalyticsTracker.onAppBackground()
        }
    }
}
