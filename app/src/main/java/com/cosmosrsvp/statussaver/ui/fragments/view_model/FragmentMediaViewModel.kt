package com.cosmosrsvp.statussaver.ui.fragments.view_model
import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cosmosrsvp.statussaver.domain.extensions.*
import com.cosmosrsvp.statussaver.domain.model.DownloadedStatusModel
import com.cosmosrsvp.statussaver.domain.model.StatusModel
import com.cosmosrsvp.statussaver.util.*
import java.io.File
import kotlin.concurrent.thread


class FragmentMediaViewModel(application: Application)
    : AndroidViewModel(application) {
    private val TAG= "statusFragmentViewModel"
    private val context = getApplication<Application>().applicationContext
    val isPermissionGranted: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val mediaFileListInWhatsAppDir: MutableLiveData<MutableList<StatusModel>> by lazy {
        MutableLiveData<MutableList<StatusModel>>()
    }
    val mediaFileListInAppDir: MutableLiveData<MutableList<DownloadedStatusModel>> by lazy {
        MutableLiveData<MutableList<DownloadedStatusModel>>()
    }
    init {
        mediaFileListInWhatsAppDir.value= mutableListOf()
        mediaFileListInAppDir.value =  mutableListOf()
        initBlock()
    }

    fun initBlock(){
        val isDirBuilt=appDir.mkdirs()
        refreshAppMediaFileList()
        refreshWhatsAppMediaFileList()
        Log.d(TAG,"Is directory built? :${isDirBuilt}")
    }

    fun refreshAppMediaFileList(){
        appDir.listFiles()?.let { appDirFiles->
            mediaFileListInAppDir.value?.clear()
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
                    mediaFileListInAppDir.value?.add(it)
                }
            }
        }
    }




    fun onDownloadButtonClicked(file: File){
        file.onDownloadButtonClicked()
        refreshAppMediaFileList()
        refreshWhatsAppMediaFileList()
    }

    fun refreshWhatsAppMediaFileList(){
        Log.d(TAG, "Refreshing whatsapp status dir")
        if (whatsAppDir1.exists()) {
            populateTheList(whatsAppDir1)
            Log.d(TAG, "Directory 1 exists")
        }
        if (whatsAppDir2.exists()){
            val sp=context.getSharedPreferences(TREE_URI, AppCompatActivity.MODE_PRIVATE)
            val stUri= sp.getString(URI_PATH, null) ?: return
            val uri = Uri.parse(stUri)
            populateTheList(uri)
            Log.d(TAG, "Directory 2 exists")
        }
    }

    private fun populateTheList(file: File){
        file.listFiles()?.forEach { statusFile ->
            makeModel(statusFile)
        }
    }

    private fun populateTheList(uri: Uri){
        thread {
            val tree = DocumentFile.fromTreeUri(context, uri)!!
          //  val uriList  = arrayListOf<Uri>()
            listFiles(tree).forEach { uri ->
                // Collect all the Uri from here
                Log.d(TAG, "Making model form uri and path is ${uri.path}")
                makeModel(File(uri.path))
            }

        }
    }

    private fun listFiles(folder: DocumentFile): List<Uri> {
        return if (folder.isDirectory) {
            folder.listFiles().mapNotNull { file ->
                if (file.name != null) file.uri else null
            }
        } else {
            emptyList()
        }
    }

    private fun makeModel(statusFile: File){
        var statusModel: StatusModel?=null
        if (statusFile.isImage()){
            statusModel= mediaFileListInAppDir.value?.let {
                StatusModel(
                    mediaFile = statusFile,
                    isVideo = false,
                    isDownloaded = (it as ArrayList).hasFile(File(appDir, statusFile.name))
                )
            }
        }
        else if (statusFile.isVideo()){
            statusModel= mediaFileListInAppDir.value?.let {
                StatusModel(
                    mediaFile = statusFile,
                    isVideo = true,
                    isDownloaded = (it as ArrayList).hasFile(File(appDir, statusFile.name))
                )
            }
        }
        statusModel?.let {
            val index = mediaFileListInWhatsAppDir.value?.hasModel(it) ?: return
            when (index){
                NONEXISTENT->{
                    mediaFileListInWhatsAppDir.value?.add(it)
                }
                EXISTS-> null
                else -> { // It is edited
                    mediaFileListInWhatsAppDir.value?.set(index,it)
                }
            }
        }
    }

}