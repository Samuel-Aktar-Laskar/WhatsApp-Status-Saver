package com.cosmosrsvp.statussaver.domain.model

import java.io.File

data class StatusModel(
    val mediaFile: File,
    val isVideo: Boolean,
    val isDownloaded: Boolean
)