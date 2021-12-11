package com.cosmosrsvp.statussaver.ui.Fragments.ViewModel

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cosmosrsvp.statussaver.domain.model.DownloadedStatusModel
import com.cosmosrsvp.statussaver.domain.model.StatusModel
import com.cosmosrsvp.statussaver.util.enum.getAllImageExtensions
import com.cosmosrsvp.statussaver.util.enum.getAllVideoFormats
import com.google.android.exoplayer2.offline.Download
import java.io.File


class fragment_MediaViewModel
    : ViewModel() {

    private val TAG= "statusFragmentViewModel"
    val isPermissionGranted: MutableState<Boolean> = mutableStateOf(false)
    val mediaFileListInWhatsAppDir: MutableState<List<StatusModel>> = mutableStateOf(listOf())
    val mediaFileListInAppDir: MutableLiveData<MutableList<DownloadedStatusModel>> by lazy {
        MutableLiveData<MutableList<DownloadedStatusModel>>()
    }
    private val storageDir: File=Environment.getExternalStorageDirectory()
    private val appDir: File= File(storageDir, "Status_Saver_With_Video_Downloader")
    init {
        val isDirBuilt=appDir.mkdirs()
        refreshAppMediaFileList()
        refreshWhatsAppMediaFilelist()
        Log.d(TAG,"Is directory built? :${isDirBuilt}")
    }


    fun refreshAppMediaFileList(){
        appDir.listFiles()?.let { appDirFiles->
            val tempList: MutableList<DownloadedStatusModel> = mutableListOf()
            for( file in appDirFiles){
                var model: DownloadedStatusModel? =null
                if (file.isVideo()){
                    model= DownloadedStatusModel(
                        mediaFile = file,
                        isVideo = true
                    )
                }
                else if(file.isImage()){
                    model = DownloadedStatusModel(
                        mediaFile = file,
                        isVideo = false
                    )
                }
                model?.let {
                    tempList.add(model)
                }
            }
            mediaFileListInAppDir.value=tempList
        }
    }

    fun refreshWhatsAppMediaFilelist(){
        val whatsAppStatusFile= File(storageDir,"WhatsApp/Media/.Statuses")
        val tempList: MutableList<StatusModel> = mutableListOf()
        Log.d(TAG, "Refreshing whatsapp status dir")
        whatsAppStatusFile.listFiles()?.let { statusDirectoryFiles->
            for (file in statusDirectoryFiles ){
                var statusModel: StatusModel?=null
                if (file.isImage()){
                    statusModel= mediaFileListInAppDir.value?.let {
                        StatusModel(
                            mediaFile = file,
                            isVideo = false,
                            isDownloaded = it.hasFile(File(appDir, file.name))
                        )
                    }
                }
                else if (file.isVideo()){
                    statusModel= mediaFileListInAppDir.value?.let {
                        StatusModel(
                            mediaFile = file,
                            isVideo = true,
                            isDownloaded = it.hasFile(File(appDir, file.name))
                        )
                    }
                }
                statusModel?.let {
                    tempList.add(statusModel)
                }
            }
        }
        mediaFileListInWhatsAppDir.value=tempList
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

    fun onDownloadButtonClicked(file: File){
        val targetFile= File(appDir, file.name)
        file.copyTo(targetFile)
        refreshAppMediaFileList()
        refreshWhatsAppMediaFilelist()
    }

    fun onShareButtonClicked(uri: Uri): Intent{
        val waIntent= Intent(Intent.ACTION_SEND)
        waIntent.setType("image/jpg")
        waIntent.putExtra(Intent.EXTRA_STREAM,uri)
        return  waIntent
    }

    fun onWhatsAppShareButtonClicked(uri: Uri): Intent{
        val waIntent= Intent(Intent.ACTION_SEND)
        waIntent.setType("image/jpg")
        waIntent.setPackage("com.whatsapp")
        waIntent.putExtra(Intent.EXTRA_STREAM,uri)
        return  waIntent
    }

    fun onDeleteButtonClicked(file: File){
        try {
            file.delete()
        }
        catch (e: Exception){

        }

    }



    fun List<DownloadedStatusModel>.hasFile(file: File): Boolean{
        for(content in this){
            if (content.mediaFile.equals(file))
                return true
        }
        return false
    }
}