package com.quickcleanpro.phonecleaner.use.feature.junkclean.domain

import android.net.Uri
import com.quickcleanpro.phonecleaner.use.core.model.clean.JunkFile

data class JunkDeleteOutcome(
    val junkFile: JunkFile,
    val deleted: Boolean,
    val freedBytes: Long,
    val authorizationUri: Uri? = null,
)

data class JunkAuthorizedDeleteResult(
    val cleanedFiles: List<JunkFile>,
    val failedFiles: List<JunkFile>,
    val freedBytes: Long,
)
