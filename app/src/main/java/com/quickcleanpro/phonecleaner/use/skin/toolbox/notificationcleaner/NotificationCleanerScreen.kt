package com.quickcleanpro.phonecleaner.use.skin.toolbox.notificationcleaner

import androidx.compose.runtime.Composable
import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.common.notification.NotificationCleanerViewModel
import com.quickcleanpro.phonecleaner.use.skin.toolbox.notificationcleaner.views.NotificationCleanerScreenState
import org.koin.androidx.compose.koinViewModel

@Composable
fun NotificationCleanerScreen(viewModel: NotificationCleanerViewModel = koinViewModel()) {
    NotificationCleanerScreenState(viewModel = viewModel)
}
