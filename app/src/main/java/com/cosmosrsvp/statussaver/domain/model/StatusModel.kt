package com.cosmosrsvp.statussaver.domain.model

import androidx.documentfile.provider.DocumentFile

data class StatusModel(
    override val mediaFile: DocumentFile,
    override val isVideo: Boolean,
    val isDownloaded: Boolean
): MainModel