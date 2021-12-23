package com.cosmosrsvp.statussaver.ui.fragments.view_model
import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cosmosrsvp.statussaver.domain.extensions.*
import com.cosmosrsvp.statussaver.domain.model.DownloadedStatusModel
import com.cosmosrsvp.statussaver.domain.model.StatusModel
import com.cosmosrsvp.statussaver.util.*
import java.io.File

private const val TAG= "statusFragmentViewModel"
class FragmentMediaViewModel(application: Application)
    : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    val isPermissionGranted: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    private val mediaFileListInWhatsAppDir: MutableLiveData<MutableList<StatusModel>> by lazy {
        MutableLiveData<MutableList<StatusModel>>()
    }
    private val mediaFileListInAppDir: MutableLiveData<MutableList<DownloadedStatusModel>> by lazy {
        MutableLiveData<MutableList<DownloadedStatusModel>>()
    }

    fun getModelsInDownloadsDir() : LiveData<MutableList<DownloadedStatusModel>>{
        return mediaFileListInAppDir
    }

    fun getModelsInWhatsAppDir() : LiveData<MutableList<StatusModel>>{
        return mediaFileListInWhatsAppDir
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
            val tempModelList : MutableList<DownloadedStatusModel> = mutableListOf()
            Log.d(TAG,"Before filling download list")
            for( file in appDirFiles){
                var model: DownloadedStatusModel? = null
                if (DocumentFile.fromFile(file).isVideo()){
                    model= DownloadedStatusModel(
                        mediaFile = DocumentFile.fromFile(file),
                        isVideo = true
                    )
                } else if(DocumentFile.fromFile(file).isImage()){
                    model = DownloadedStatusModel(
                        mediaFile =DocumentFile.fromFile(file),
                        isVideo = false
                    )
                }
                model?.let {
                    tempModelList.add(it)
                }
            }
            mediaFileListInAppDir.value = tempModelList
            Log.d(TAG, "After filling download list, size :${mediaFileListInAppDir.value?.size}")
        }
    }

    fun onDownloadButtonClicked(dFile: DocumentFile, context: Context){
        dFile.onDownloadButtonClicked(context)
        refreshAppMediaFileList()
        refreshWhatsAppMediaFileList()
    }

    fun refreshWhatsAppMediaFileList(){
        Log.d(TAG, "Refreshing whatsapp status dir")
        val tempStatusModels: MutableList<StatusModel> = mutableListOf()
        if (whatsAppDir1.exists()) {
            populateTheList(whatsAppDir1, tempStatusModels)
            Log.d(TAG, "Directory 1 exists")
        }
        if (whatsAppDir2.exists()){
            val sp=context.getSharedPreferences(TREE_URI, AppCompatActivity.MODE_PRIVATE)
            val stUri= sp.getString(URI_PATH, null) ?: return
            val uri = Uri.parse(stUri)
            populateTheList(uri, tempStatusModels)
            Log.d(TAG, "Directory 2 exists and uri is :$uri")
        }
        mediaFileListInWhatsAppDir.value = tempStatusModels
    }

    private fun populateTheList(file: File,tempStatusModels: MutableList<StatusModel> ){
        file.listFiles()?.forEach { statusFile ->
            makeModel(DocumentFile.fromFile(statusFile), tempStatusModels)
        }
    }

    private fun populateTheList(uri: Uri,tempStatusModels: MutableList<StatusModel>){
            val tree = DocumentFile.fromTreeUri(context, uri)!!
            if (tree.isDirectory){
                tree.listFiles().forEach { dFile->
                    makeModel(dFile, tempStatusModels)
                    Log.d(TAG, "dFile obtained is :${dFile.name}")
                }
            }
    }

    private fun makeModel(statusFile: DocumentFile, tempStatusModels: MutableList<StatusModel>){
        var statusModel: StatusModel?=null
        if (statusFile.isImage()){
            Log.d(TAG,"It is image")
            statusModel= mediaFileListInAppDir.value?.let {
                StatusModel(
                    mediaFile = statusFile,
                    isVideo = false,
                    isDownloaded = it.hasFile(DocumentFile.fromFile(File(appDir, statusFile.name)))
                )
            }
        }
        else if (statusFile.isVideo()){
            Log.d(TAG, "It is video")
            statusModel= mediaFileListInAppDir.value?.let {
                StatusModel(
                    mediaFile = statusFile,
                    isVideo = true,
                    isDownloaded = it.hasFile(DocumentFile.fromFile(File(appDir, statusFile.name)))
                )
            }
        }
        statusModel?.let {
            Log.d(TAG, "Adding it")
//           val index = mediaFileListInWhatsAppDir.value?.hasModel(it) ?: return
//            when (index){
//                NONEXISTENT->{
//                    tempStatusModels.add(it)
//                }
//                EXISTS-> null
//                else -> { // It is edited
//                    tempStatusModels.set(index,it)
//                }
//            }
            tempStatusModels.add(it)
        }
    }

}