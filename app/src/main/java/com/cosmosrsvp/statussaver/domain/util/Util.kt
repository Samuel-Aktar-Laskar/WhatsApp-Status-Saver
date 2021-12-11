package com.cosmosrsvp.statussaver.domain.util

import android.content.res.Resources.getSystem
import com.cosmosrsvp.statussaver.util.enum.getAllVideoFormats
import java.io.File

val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()
val WidthPixels: Int = getSystem().displayMetrics.widthPixels
val PERCENT_REDUCTION: Double=0.5;

fun File.isVideo(): Boolean{
    val absPathSplited: List<String> = this.absolutePath.split(".")
    val requiredExtension: String = absPathSplited.get(
        absPathSplited.size-1
    )
    for (extension in getAllVideoFormats()){
        if (requiredExtension.equals(extension.value)){
            return true
        }
    }
    return false
}