package com.cosmosrsvp.statussaver.domain.model

import java.io.File

data class DownloadedStatusModel(
    override val mediaFile: File,
    override val isVideo: Boolean,
): MainModel