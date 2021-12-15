package com.cosmosrsvp.statussaver.domain.extensions

import android.os.Build
import android.util.Log
import com.cosmosrsvp.statussaver.domain.model.MainModel
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

