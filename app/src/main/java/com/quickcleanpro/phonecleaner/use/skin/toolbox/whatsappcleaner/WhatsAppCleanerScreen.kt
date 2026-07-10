package com.quickcleanpro.phonecleaner.use.skin.toolbox.whatsappcleaner

import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.whatsappcleaner.WhatsAppCleanerViewModel

import androidx.compose.runtime.Composable
import com.quickcleanpro.phonecleaner.use.skin.toolbox.whatsappcleaner.views.WhatsAppCleanerScreenState
import org.koin.androidx.compose.koinViewModel

@Composable
fun WhatsAppCleanerScreen(viewModel: WhatsAppCleanerViewModel = koinViewModel()) {
    WhatsAppCleanerScreenState(viewModel = viewModel)
}
