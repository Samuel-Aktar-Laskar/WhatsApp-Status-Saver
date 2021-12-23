package com.cosmosrsvp.statussaver.domain.model

import androidx.documentfile.provider.DocumentFile

interface MainModel {
    val mediaFile: DocumentFile
    val isVideo: Boolean
}