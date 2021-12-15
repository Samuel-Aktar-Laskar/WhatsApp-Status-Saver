package com.cosmosrsvp.statussaver.domain.model

import java.io.File

data class StatusModel(
    override val mediaFile: File,
    override val isVideo: Boolean,
    val isDownloaded: Boolean
): MainModel