package com.cosmosrsvp.statussaver.domain.extensions


import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.documentfile.provider.DocumentFile
import com.cosmosrsvp.statussaver.util.appDir
import com.cosmosrsvp.statussaver.util.enum.getAllImageExtensions
import com.cosmosrsvp.statussaver.util.enum.getAllVideoFormats
import java.io.File



fun File.onDownloadButtonClicked():Boolean{
    val targetFile= File(appDir, this.name)
    if (targetFile.exists()) return false
    this.copyTo(targetFile)
    return true
}
fun DocumentFile.onDownloadButtonClicked(context: Context):Boolean{
    val targetFile= File(appDir, this.name)
    if (targetFile.exists()) return false
    copyBufferedFile(
        context = context,
        inputUri = this.uri,
        outputUri = Uri.fromFile(targetFile)
    )
    return true
}



fun File.onDeleteButtonClicked(){
    try {
        this.delete()
    }
    catch (e: Exception){

    }
}
fun DocumentFile.onDeleteButtonClicked(){
    try {
        this.delete()
    }
    catch (e: Exception){

    }
}

fun DocumentFile.isVideo(): Boolean{
    val absPathSplited: List<String> = this.name?.split(".") ?: return false
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


fun DocumentFile.isImage(): Boolean{
    val absPathSplited: List<String> = this.name?.split(".") ?: return false
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