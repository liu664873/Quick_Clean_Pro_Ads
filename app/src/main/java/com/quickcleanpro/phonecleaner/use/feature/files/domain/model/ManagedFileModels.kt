package com.quickcleanpro.phonecleaner.use.feature.files.domain.model

import android.net.Uri
import com.quickcleanpro.phonecleaner.use.core.common.format.FileSizeFormatter

enum class ManagedFileType { Image, Video, Audio, Document, Other }

data class ManagedFileItem(
    val id: Long,
    val uri: Uri,
    val path: String?,
    val name: String,
    val sizeBytes: Long,
    val modifiedSeconds: Long,
    val mimeType: String?,
    val bucketName: String?,
    val type: ManagedFileType,
) {
    val formattedSize: String get() = FileSizeFormatter.format(sizeBytes)
}
