package com.quickcleanpro.phonecleaner.use.skin.toolbox.networkusage

import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.networkusage.NetworkUsageViewModel

import androidx.compose.runtime.Composable
import com.quickcleanpro.phonecleaner.use.skin.toolbox.networkusage.views.NetworkUsageScreenState
import org.koin.androidx.compose.koinViewModel

@Composable
fun NetworkUsageScreen(viewModel: NetworkUsageViewModel = koinViewModel()) {
    NetworkUsageScreenState(viewModel = viewModel)
}
