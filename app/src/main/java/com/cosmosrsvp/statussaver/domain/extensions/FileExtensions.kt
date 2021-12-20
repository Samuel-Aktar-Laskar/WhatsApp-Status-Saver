package com.cosmosrsvp.statussaver.domain.extensions


import android.os.Environment
import com.cosmosrsvp.statussaver.util.enum.getAllImageExtensions
import com.cosmosrsvp.statussaver.util.enum.getAllVideoFormats
import java.io.File
private val storageDir: File= Environment.getExternalStorageDirectory()
private val appDir: File= File(storageDir, "Status_Saver_With_Video_Downloader")

fun File.onDownloadButtonClicked():Boolean{
    val targetFile= File(appDir, this.name)
    if (targetFile.exists()) return false
    this.copyTo(targetFile)
    return true
}

fun File.onDeleteButtonClicked(){
    try {
        this.delete()
    }
    catch (e: Exception){

    }

}

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


fun File.isImage(): Boolean{
    val absPathSplited: List<String> = this.absolutePath.split(".")
    val requiredExtension: String = absPathSplited.get(
        absPathSplited.size-1
    )
    for (extension in getAllImageExtensions()){
        if (requiredExtension.equals(extension.value)){
            return true
        }
    }
    return false
}