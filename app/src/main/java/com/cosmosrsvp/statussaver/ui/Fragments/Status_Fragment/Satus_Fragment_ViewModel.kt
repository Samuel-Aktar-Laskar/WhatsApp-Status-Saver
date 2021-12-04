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
    val mediaFileListInWhatsAppDir: MutableState<MutableList<StatusModel>> = mutableStateOf(mutableListOf())
    val mediaFileListInAppDir: MutableState<MutableList<File>> = mutableStateOf(mutableListOf())
    private val storageDir: File=Environment.getExternalStorageDirectory()
    private val appDir: File= File(storageDir, "Status_Saver_With_Video_Downloader")
    init {
        val isDirBuilt=appDir.mkdirs()
        refreshAppMediaFileList()
        refreshWhatsAppMediaFilelist()
        Log.d(TAG,"Is directory built? :${isDirBuilt}")
    }


    fun alertDialogForRequestingPermission(){

    }

    fun refreshAppMediaFileList(){
        appDir.listFiles()?.let { appDirFiles->
            for( file in appDirFiles){
                if (mediaFileListInAppDir.value.hasFile(file))
                else mediaFileListInAppDir.value.add(file)
            }
        }
    }

    fun refreshWhatsAppMediaFilelist(){
        val whatsAppStatusFile= File(storageDir,"WhatsApp/Media/.Statuses")
        whatsAppStatusFile.listFiles()?.let { statusDirectoryFiles->
            for (file in statusDirectoryFiles ){
                var statusModel: StatusModel?=null
                if (isImage( absPath = file.absolutePath)){
                    statusModel= StatusModel(
                                mediaFile = file,
                                isVideo = false,
                                isDownloaded = mediaFileListInAppDir.value.hasFile(File(appDir, file.name))
                    )

                }
                else if (isVideo(file.absolutePath)){
                    statusModel= StatusModel(
                        mediaFile = file,
                        isVideo = true,
                        isDownloaded = mediaFileListInAppDir.value.hasFile(File(appDir, file.name))
                    )
                }
                statusModel?.let {
                    if (!mediaFileListInWhatsAppDir.value.hasModel(it))
                        mediaFileListInWhatsAppDir.value.add(statusModel)
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
        mediaFileListInAppDir.value.add(targetFile)
        refreshWhatsAppMediaFilelist()
    }

    fun onShareButtonClicked(file: File){

    }

    fun onWhatsAppShareButtonClicked(file: File){

    }

    fun List<StatusModel>.hasModel(reqModel: StatusModel): Boolean{
        for(model in this){
            if (model.equals(reqModel)){
                return true
            }
        }
        return false
    }
    fun List<File>.hasFile(file: File): Boolean{
        for(content in this){
            if (content.equals(file))
                return true
        }
        return false
    }


}