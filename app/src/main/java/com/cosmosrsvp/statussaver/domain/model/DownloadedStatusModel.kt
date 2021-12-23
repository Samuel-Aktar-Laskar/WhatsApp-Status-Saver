package com.cosmosrsvp.statussaver.domain.model

import androidx.documentfile.provider.DocumentFile

data class DownloadedStatusModel(
    override val mediaFile: DocumentFile,
    override val isVideo: Boolean,
): MainModel