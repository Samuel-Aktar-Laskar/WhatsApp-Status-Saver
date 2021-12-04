package com.cosmosrsvp.statussaver.util.enum

enum class VideoExtension(
    val value: String
) {
    MP4("mp4"),
    MOV("mov"),
    WMV("wmv"),
    AVI("avi"),
    MKV("mkv")
}

fun getAllVideoFormats(): List<VideoExtension>{
    return listOf(VideoExtension.AVI, VideoExtension.MP4, VideoExtension.MKV, VideoExtension.MOV, VideoExtension.WMV)
}
