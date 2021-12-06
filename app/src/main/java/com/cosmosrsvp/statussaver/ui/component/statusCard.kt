package com.cosmosrsvp.statussaver.ui.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.cosmosrsvp.statussaver.domain.model.StatusModel
import android.graphics.Bitmap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import com.cosmosrsvp.statussaver.util.loadPicture
import android.provider.MediaStore

import android.media.ThumbnailUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.cosmosrsvp.statussaver.R
import com.skydoves.landscapist.glide.GlideImage
import java.io.File


@Composable
fun statusCard(
    model: StatusModel,
    onDownlaodClicked: (file: File)->Unit,
    onShareClicked: (file: File)->Unit
){
    Box(modifier= Modifier.fillMaxSize()) {
        Column(
        ) {
            if (model.isVideo){
                /*model.mediaFile?.let {
                    ThumbnailUtils.createVideoThumbnail(
                        it.getAbsolutePath(),
                        MediaStore.Video.Thumbnails.MICRO_KIND
                    )
                }?.let {
                    CircularImageViewDemo(bitmap = it)
                }*/

            }
            else{
                GlideImage(
                    imageModel = model.mediaFile.absolutePath,
                    // Crop, Fit, Inside, FillHeight, FillWidth, None
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(350.dp),
                    requestOptions = {
                        RequestOptions()
                            .override(256, 256)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .centerCrop()
                    },
                )

            }
        }
        Row(
            modifier= Modifier
                .fillMaxWidth()
                .background(color = Color(0x66000000))
                .height(50.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.Bottom,
        ) {
            IconButton(
                onClick = {
                    if (!model.isDownloaded)
                    onDownlaodClicked(model.mediaFile)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_download),
                    contentDescription =null,
                    tint = downloadIconColor(model.isDownloaded)
                )
            }
            IconButton(
                onClick = { onShareClicked(model.mediaFile) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_share),
                    contentDescription =null,
                    tint = Color.White
                )
            }
        }
    }


}

fun downloadIconColor(isDownloaded: Boolean): Color{
    if (isDownloaded){
        return Color(0xFF838383)
    }
    else{
        return Color.White
    }
}


