package com.cosmosrsvp.statussaver.domain.model

import java.io.File

data class DownloadedStatusModel(
    val mediaFile: File,
    val isVideo: Boolean,
)