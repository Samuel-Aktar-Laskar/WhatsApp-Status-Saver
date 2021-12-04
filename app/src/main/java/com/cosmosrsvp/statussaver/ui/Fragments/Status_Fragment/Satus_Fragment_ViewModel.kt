package com.cosmosrsvp.statussaver.ui.Fragments.Status_Fragment

import android.os.Environment
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.cosmosrsvp.statussaver.domain.model.StatusModel
import com.cosmosrsvp.statussaver.util.enum.getAllImageExtensions
import com.cosmosrsvp.statussaver.util.enum.getAllVideoFormats
import java.io.File

class Satus_Fragment_ViewModel
    : ViewModel() {

    val TAG= "statusFragmentViewModel"
    val isPermissionGranted: MutableState<Boolean> = mutableStateOf(false)
    val mediaFileList: MutableState<MutableList<StatusModel>> = mutableStateOf(mutableListOf())
    private val storageDir: File=Environment.getExternalStorageDirectory()
    private val appDir: File= File(storageDir, "Status_Saver_With_Video_Downloader")
    init {
        fillMediaFilelist()
        val isDirBuilt=appDir.mkdirs()
        Log.d(TAG,"Is directory built? :${isDirBuilt}")
    }


    fun alertDialogForRequestingPermission(){

    }

    fun fillMediaFilelist(){
        val whatsAppStatusFile= File(storageDir,"WhatsApp/Media/.Statuses")
        whatsAppStatusFile.listFiles()?.let { statusDirectoryFiles->
            for (file in statusDirectoryFiles ){
                if (isImage( absPath = file.absolutePath)){
                    mediaFileList.value.add(
                        StatusModel(
                            mediaFile = file,
                            isVideo = false,
                            isDownloaded = true
                        )
                    )
                }
                else if (isVideo(file.absolutePath)){
                    mediaFileList.value.add(
                        StatusModel(
                            mediaFile = file,
                            isVideo = true,
                            isDownloaded = false
                        )
                    )
                }
            }
        }
    }

    fun isVideo(absPath: String): Boolean{
        val absPathSplited: List<String> = absPath.split(".")
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
    fun isImage(absPath: String): Boolean{
        val absPathSplited: List<String> = absPath.split(".")
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

    fun onDownlaodButtonClicked(file: File){
        val targetFile= File(appDir, file.name)
        file.copyTo(targetFile)
    }

    fun onShareButtonClicked(file: File){

    }

    fun onWhatsAppShareButtonClicked(file: File){

    }


}