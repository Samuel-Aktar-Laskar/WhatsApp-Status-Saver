package com.cosmosrsvp.statussaver.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition


@Composable
fun loadPicture(
    url: String
): MutableState<Bitmap?>{
    val bitmapState: MutableState<Bitmap?> = remember{mutableStateOf(null)}

    Glide.with(LocalContext.current)
        .asBitmap()
        .load(url)
        .into(object: CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmapState.value=resource
            }
            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })

    return  bitmapState
}
