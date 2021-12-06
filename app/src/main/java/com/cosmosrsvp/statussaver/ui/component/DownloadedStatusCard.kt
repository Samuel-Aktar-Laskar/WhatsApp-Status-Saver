package com.cosmosrsvp.statussaver.ui.component

import android.media.ThumbnailUtils
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.net.toUri
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.domain.model.DownloadedStatusModel
import com.cosmosrsvp.statussaver.domain.util.WidthPixels
import com.cosmosrsvp.statussaver.domain.util.px

import com.skydoves.landscapist.glide.GlideImage
import java.io.File


@Composable
fun downloadedStatusCard(
    model: DownloadedStatusModel,
    onShareClicked: (file: File)->Unit
){
    Box(modifier= Modifier.fillMaxSize()) {
        Column(
        ) {
            if (model.isVideo){
                var bMap=model.mediaFile?.let {
                    ThumbnailUtils.createVideoThumbnail(
                        it.getAbsolutePath(),
                        MediaStore.Video.Thumbnails.MICRO_KIND
                    )
                }
                bMap?.let {
                    GlideImage(
                        imageModel = it,
                        contentScale = ContentScale.Crop,
                        requestOptions = {
                            RequestOptions()
                                .override(220,350)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            // add a border (optional)
                            .height(350.dp)
                            .border(width = 1.dp, color = Color.White),
                        alignment = Alignment.Center
                    )

                }
                bMap=null
            }
            else{
                model.mediaFile?.let {
                    GlideImage(
                        imageModel = it.absolutePath,
                        contentScale = ContentScale.Crop,
                        requestOptions = {
                            RequestOptions()
                                .override(220,350)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            // add a border (optional)
                            .height(350.dp)
                            .border(width = 1.dp, color = Color.White)
                    )

                }


            }
        }
        if (model.isVideo){
            Icon(
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center),
                tint = Color.White
            )
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


