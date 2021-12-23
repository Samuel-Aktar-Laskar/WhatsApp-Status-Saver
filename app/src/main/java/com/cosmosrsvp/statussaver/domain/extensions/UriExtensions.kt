package com.cosmosrsvp.statussaver.domain.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.cosmosrsvp.statussaver.util.appDir
import com.cosmosrsvp.statussaver.util.enum.getAllVideoFormats
import java.io.File
import com.cosmosrsvp.statussaver.ui.activities.MainActivity

import androidx.core.content.FileProvider
import com.cosmosrsvp.statussaver.BuildConfig


private const val TAG = "UriExtensionsTag"
fun Uri.onShareButtonClicked(): Intent {
    val waIntent= Intent(Intent.ACTION_SEND)
    waIntent.setType("image/jpg")
    waIntent.putExtra(Intent.EXTRA_STREAM,this)
    return  waIntent
}

fun Uri.onWhatsAppShareButtonClicked(context: Context): Intent? {
    /*
    *The uri obtained cannot be used to create the intent,
    * we need the help of FileProvider. But since, there is no java.io.File,
    * Rather it's a document file's uri, I currently don't know how to do this.
    * I am applying a trick to do that so,
    * I am creating a temp file and then create it's uri using FileProvider and then send it
    */
    val tempFile = this.downloadToTempFile(context) ?: return null
    val uri = FileProvider.getUriForFile(
        context,
        BuildConfig.APPLICATION_ID.toString() + ".provider",
        tempFile
    )
    val waIntent= Intent(Intent.ACTION_SEND)
    waIntent.type = "image/jpg"
    waIntent.setPackage("com.whatsapp")
    waIntent.putExtra(Intent.EXTRA_STREAM,uri)
    return  waIntent
}

fun Uri.onWhatsAppShareButtonClicked(): Intent {
    val waIntent= Intent(Intent.ACTION_SEND)
    waIntent.type = "image/jpg"
    waIntent.setPackage("com.whatsapp")
    waIntent.putExtra(Intent.EXTRA_STREAM,this)
    return waIntent
}

fun Uri.onDownloadButtonClicked(context: Context): Boolean{
    var targetName: String? = this.path?.split("/")?.last() ?: return false
    val targetFile= File(appDir, targetName!!)
    if (targetFile.exists()) return false
    copyBufferedFile(
        context = context,
        inputUri = this,
        outputUri = Uri.fromFile(targetFile)
    )
    return true
}

fun Uri.onDeleteButtonClicked(context: Context){
    try {
        val tree = DocumentFile.fromSingleUri(context,this)
        tree?.delete()
    }
    catch (e: Exception){
        Log.d(TAG, "Error in deleting file :${e.localizedMessage}")
    }
}

fun Uri.isVideo(): Boolean{
    for (extension in getAllVideoFormats()){
        this.path?.endsWith(extension.value)?.let {
            if (it){
                return true
            }
        }
    }
    return false
}

fun Uri.downloadToTempFile(context: Context): File?{
    var targetName: String? = this.path?.split("/")?.last() ?: return null
    var suffix = targetName?.split(".")?.last()
    val targetFile= File.createTempFile("TempFile", ".$suffix", context.externalCacheDir)
   context.externalCacheDir?.listFiles()?.let { arrayOfFiles ->
       arrayOfFiles.forEach {
           if (it.exists()) it.delete()
       }
   }
    copyBufferedFile(
        context = context,
        inputUri = this,
        outputUri = Uri.fromFile(targetFile)
    )
    return targetFile
}
