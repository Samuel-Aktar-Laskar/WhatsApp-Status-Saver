package com.cosmosrsvp.statussaver.domain.extensions

import android.os.Build
import android.util.Log
import com.cosmosrsvp.statussaver.domain.model.DownloadedStatusModel
import com.cosmosrsvp.statussaver.domain.model.MainModel
import com.cosmosrsvp.statussaver.domain.model.StatusModel
import com.cosmosrsvp.statussaver.util.EXISTS
import com.cosmosrsvp.statussaver.util.NONEXISTENT
import com.cosmosrsvp.statussaver.util.enum.StatusModelType
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributeView

private const val TAG="ListExtensions"

fun  <T: MainModel> ArrayList<T>.sortList(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        try {
            this.sortWith{ f1, f2 ->
                val view1 = Files.getFileAttributeView(
                    f1.mediaFile.toPath(),
                    BasicFileAttributeView::class.java
                )
                val view2 = Files.getFileAttributeView(
                    f2.mediaFile.toPath(),
                    BasicFileAttributeView::class.java
                )
                view2.readAttributes().creationTime().compareTo(view1.readAttributes().creationTime())
            }
        }
        catch (e: Exception){
            Log.d(TAG,"Error in sorting file: ${e.localizedMessage}")
        }
}

fun <T: MainModel> MutableList<T>.hasFile(file: File): Boolean{
    for(content in this){
        if (content.mediaFile == file)
            return true
    }
    return false
}

fun MutableList<StatusModel>.hasModel(RModel: StatusModel): Int{

    for (i in 0 until this.size){
        val model=this[i]
        if (model.mediaFile == RModel.mediaFile){
            if (model.isDownloaded != RModel.isDownloaded )
                return i // Edited
            else return EXISTS
        }
    }
    return NONEXISTENT
}



