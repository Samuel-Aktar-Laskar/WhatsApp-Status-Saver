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
import com.cosmosrsvp.statussaver.R
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
                val bMap = model.mediaFile?.let {
                    ThumbnailUtils.createVideoThumbnail(
                        it.getAbsolutePath(),
                        MediaStore.Video.Thumbnails.MICRO_KIND
                    )
                }
                bMap?.let {
                    CircularImageViewDemo(bitmap = it)
                }

            }
            else{
                model.mediaFile?.let {
                    loadPicture(url = it.absolutePath).value?.let {
                        CircularImageViewDemo(bitmap = it)
                    }
                }

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
            val downloadIcon= Icon(
                painter = painterResource(id = R.drawable.icon_download),
                contentDescription =null,
                tint = Color.White
            )
            val downloadedIcon= Icon(
                painter = painterResource(id = R.drawable.ic_downloaded),
                contentDescription =null,
                tint = Color(0xFF004102)
            )
            IconButton(
                onClick = { onDownlaodClicked(model.mediaFile) }
            ) {
               if (model.isDownloaded)
                   downloadedIcon
                else
                    downloadIcon

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



@Composable
fun CircularImageViewDemo(bitmap: Bitmap) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            bitmap= bitmap.asImageBitmap(),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            // crop the image if it's not a square
            modifier = Modifier
                .fillMaxWidth()
                // add a border (optional)
                .height(350.dp)
                .border(width = 1.dp, color = Color.White)

        )
    }
}
