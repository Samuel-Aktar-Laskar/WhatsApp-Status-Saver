package com.cosmosrsvp.statussaver.domain.extensions

import android.os.Build
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.cosmosrsvp.statussaver.domain.model.MainModel
import com.cosmosrsvp.statussaver.domain.model.StatusModel
import com.cosmosrsvp.statussaver.util.EXISTS
import com.cosmosrsvp.statussaver.util.NONEXISTENT

private const val TAG="ListExtensions"

fun  <T: MainModel> ArrayList<T>.sortList(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        try {
            this.sortWith{ f1, f2 ->
                f2.mediaFile.lastModified().compareTo(f1.mediaFile.lastModified())
            }
        }
        catch (e: Exception){
            Log.d(TAG,"Error in sorting file: ${e.localizedMessage}")
        }
}

fun <T: MainModel> MutableList<T>.hasFile(file: DocumentFile): Boolean{
    for(content in this){
        if (content.mediaFile.name.equals( file.name))
            return true
    }
    return false
}



fun MutableList<StatusModel>.hasModel(RModel: StatusModel): Int{

    for (i in 0 until this.size){
        val model=this[i]
        if (model.mediaFile.name.equals(RModel.mediaFile.name)){
            return if (model.isDownloaded != RModel.isDownloaded )
                i // Edited
            else EXISTS
        }
    }
    return NONEXISTENT
}



