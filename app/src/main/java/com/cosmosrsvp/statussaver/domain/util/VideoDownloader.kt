package com.cosmosrsvp.statussaver.domain.util

import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.SparseArray
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.cosmosrsvp.statussaver.util.appDir
import java.io.File
import java.lang.Exception
import java.util.*
import java.util.concurrent.atomic.AtomicLong

private const val TAG="VideoDownloaderFun"


fun downloadUTubeVideo(url: String, context: Context, normalQuality: Boolean, referenceId: AtomicLong, fileReference: FileWrapper, enableDownload: ()->Unit){

    try {
        object : YouTubeExtractor(context) {
            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                vMeta: VideoMeta?
            ) {
                if (ytFiles != null) {
                    val iTag = if(normalQuality) 18 else 22
                    try{
                        val downloadUrl = ytFiles[iTag].url
                        Log.d(TAG, "Download url: $downloadUrl")
                        downloadUrl(downloadUrl, context, referenceId, fileReference)
                    }
                    catch (e: NullPointerException){
                        Log.d(TAG, "Error in DownloadUTubeVideo")
                        toast(context, "Failed to download video\nTry changing download quality")
                        enableDownload()
                    }

                }
            }
        }.extract(url)
    }
    catch (e: Exception){
        Log.d(TAG, "Error in extracting: ${e.localizedMessage}")
    }

}

fun downloadUrl(downloadUrl: String, context:Context, referenceId: AtomicLong, fileReference: FileWrapper){
    try {
        val downloadManager = context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri: Uri =
            Uri.parse(downloadUrl)
        val request: DownloadManager.Request = DownloadManager.Request(downloadUri)

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(true)
        request.setAllowedOverMetered(true)

        request.setTitle("Downloading Video")
        request.setDescription("Please wait..")
        val calender: Calendar = Calendar.getInstance()
        fileReference.file=File(appDir,calender.timeInMillis.toString()+".mp4")
        val storageLocUri: Uri = Uri.fromFile(fileReference.file)
        request.setDestinationUri(storageLocUri)
            .setMimeType(getMime(downloadUri, context))
        val id=downloadManager.enqueue(request)
        referenceId.set(id)
        Log.d(TAG, "Reference id is :$id")
        toast(context = context, "Download started")
    }
    catch (e: Exception){
        Log.d(TAG, "Error in downloading: ${e.localizedMessage}")
    }

}

fun getMime(uri: Uri, context: Context): String?{
    val resolver: ContentResolver =context.contentResolver
    val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
    return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri))
}