package com.cosmosrsvp.statussaver.domain.model

import java.io.File

interface MainModel {
    val mediaFile: File
    val isVideo: Boolean
}