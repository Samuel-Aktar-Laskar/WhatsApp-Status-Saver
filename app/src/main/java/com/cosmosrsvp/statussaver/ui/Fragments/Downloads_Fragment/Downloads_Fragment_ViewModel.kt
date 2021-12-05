package com.cosmosrsvp.statussaver.ui.Fragments.Downloads_Fragment

import android.os.Environment
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.cosmosrsvp.statussaver.domain.model.StatusModel
import java.io.File

class Downloads_Fragment_ViewModel : ViewModel(){
    val TAG= "dloadFragViewModel"
    var mediaFileListInAppDir: MutableState<List<File>> = mutableStateOf(listOf())
    private val storageDir: File = Environment.getExternalStorageDirectory()
    private val appDir: File = File(storageDir, "Status_Saver_With_Video_Downloader")
    init {
        val isDirBuilt=appDir.mkdirs()
        Log.d(TAG,"Is directory built? :${isDirBuilt}")
    }
}