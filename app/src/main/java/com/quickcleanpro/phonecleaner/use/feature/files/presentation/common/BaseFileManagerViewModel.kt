package com.quickcleanpro.phonecleaner.use.feature.files.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quickcleanpro.phonecleaner.use.core.feature.FeatureKey
import com.quickcleanpro.phonecleaner.use.feature.files.domain.model.ManagedFileItem
import com.quickcleanpro.phonecleaner.use.core.common.operation.FeatureOperationEvent
import com.quickcleanpro.phonecleaner.use.core.common.operation.OperationAction
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

internal abstract class BaseFileManagerViewModel(
    protected val featureKey: FeatureKey,
    protected val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    testLoader: (((suspend () -> Unit)) -> Unit)? = null,
) : ViewModel() {
    protected data class FileOperationOutcome(
        val freedBytes: Long = 0L,
        val changedCount: Int = 0,
    )

    protected val operationRunner = FileOperationRunner(viewModelScope, ioDispatcher, testLoader)
    private val operationEventsChannel = Channel<FeatureOperationEvent>(Channel.BUFFERED)
    private var pendingCompletionResult: (() -> Unit)? = null
    val operationEvents: Flow<FeatureOperationEvent> = operationEventsChannel.receiveAsFlow()

    /**
     * Cancel any running scan or delete operation without changing UI state.
     */
    fun cancelActiveOperation() {
        pendingCompletionResult = null
        operationRunner.cancelActiveOperation()
    }

    /**
     * Cancel a running delete and reset the phase back to browsing.
     * Safe to call during any phase 锟?only transitions if currently [FileOperationPhase.Deleting].
     */
    fun cancelDeletingAndReturnToBrowsing() {
        pendingCompletionResult = null
        operationRunner.cancelActiveOperation()
        onCancelDeletingPhase()
    }

    /**
     * Hook for subclasses to reset their phase from Deleting 锟?Browsing.
     */
    protected abstract fun onCancelDeletingPhase()

    protected fun trackScanStarted() {
        trackOperationEvent(FeatureOperationEvent.ScanStarted(featureKey))
    }

    protected fun trackScanFinished(hasResult: Boolean) {
        trackOperationEvent(FeatureOperationEvent.ScanFinished(featureKey, hasResult))
    }

    protected fun trackActionRequested(action: OperationAction) {
        trackOperationEvent(FeatureOperationEvent.ActionRequested(featureKey, action))
    }

    protected fun trackOperationEvent(event: FeatureOperationEvent) {
        operationEventsChannel.trySend(event)
    }

    fun showResultAfterCompletionAd() {
        val resultAction = pendingCompletionResult ?: return
        pendingCompletionResult = null
        resultAction()
        trackOperationEvent(FeatureOperationEvent.ResultShown(featureKey))
    }

    protected fun runFileOperation(
        selectedFiles: List<ManagedFileItem>,
        action: OperationAction,
        onEmptySelection: () -> Unit,
        onStart: () -> Unit,
        operationDelayMillis: Long,
        completeDelayMillis: Long,
        operation: suspend () -> FileOperationOutcome,
        isSuccessful: (FileOperationOutcome) -> Boolean = { true },
        onCompleteAnimation: suspend (FileOperationOutcome) -> Unit,
        onResult: () -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        if (selectedFiles.isEmpty()) {
            onEmptySelection()
            return
        }

        pendingCompletionResult = null
        onStart()
        trackOperationEvent(FeatureOperationEvent.OperationStarted(featureKey, action))
        operationRunner.launch {
            runCatching {
                val outcome = operation()
                if (!isSuccessful(outcome)) {
                    error(deletionFailedMessage())
                }
                operationRunner.delayIfNeeded(operationDelayMillis)
                onCompleteAnimation(outcome)
                operationRunner.delayIfNeeded(completeDelayMillis)
                pendingCompletionResult = onResult
                trackOperationEvent(FeatureOperationEvent.OperationFinished(featureKey, action, success = true))
            }.onFailure { error ->
                if (error is CancellationException) throw error
                pendingCompletionResult = null
                trackOperationEvent(FeatureOperationEvent.OperationFinished(featureKey, action, success = false))
                onFailure(error)
            }
        }
    }
}
