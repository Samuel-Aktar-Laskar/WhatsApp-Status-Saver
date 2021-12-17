package com.cosmosrsvp.statussaver.domain.extensions


import android.util.Log
import com.cosmosrsvp.statussaver.util.enum.VideoType
private const val TAG="stringEntnsions"

fun String.ToGoodUrl(type: VideoType):String?{
    return when (type) {
        VideoType.UTUBE_VIDEO-> this
        VideoType.UTUBE_SHORTS->{
            val mainPart=this.getMainId(type = type)
            Log.d(TAG,"shorts_url: $mainPart")
            return "https://youtu.be/$mainPart"
        }
        else -> null
    }
}

fun String.LinkType(): VideoType{
    return when {
        this.contains("youtube.com/shorts/") -> VideoType.UTUBE_SHORTS
        this.contains("youtu.be/") -> VideoType.UTUBE_VIDEO
        this.contains("youtube.com/watch") -> VideoType.UTUBE_WATCH
        else -> VideoType.UNKNOWN_TYPE
    }
}

fun String.getMainId(type: VideoType):String?{
    return when (type) {
        VideoType.UTUBE_VIDEO-> {
            val parts=this.split("/")
            return parts[parts.size-1]
        }
        VideoType.UTUBE_SHORTS->{
            val parts=this.split("/")
            val lastPart=parts[parts.size-1]
            val mainPart=lastPart.split("?")[0]
            Log.d(TAG,"shorts_url: $mainPart")
            return mainPart
        }
        else -> null
    }
}