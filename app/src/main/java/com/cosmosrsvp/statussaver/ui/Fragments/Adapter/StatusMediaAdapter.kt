package com.cosmosrsvp.statussaver.ui.Fragments.Adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageButton
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.domain.model.StatusModel
import java.io.File

class StatusMediaAdapter
    constructor(
        context: Context,
        val StatusModels: List<StatusModel>,
        val onDownloadButtonClicked: (file: File)-> Unit,
        val onShareButtonClicked: (file: File)-> Unit,
        val onWhatsAppShareButtonClcked: (file: File)-> Unit
    )
    : MediaAdapter(context = context) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val model: StatusModel = StatusModels.get(position)
        Glide.with(context).load(model.mediaFile).into(holder.ImageView)
        holder.playImg.visibility= if (model.isVideo) View.VISIBLE else View.GONE
        holder.delete_button.visibility= View.GONE
        if (model.isDownloaded){
            onDownloaded(holder.downlaod_button)
        }
        holder.downlaod_button.setOnClickListener {
            onDownloadButtonClicked(model.mediaFile)
            onDownloaded(holder.downlaod_button)
        }
        holder.share_button.setOnClickListener {
            onShareButtonClicked(model.mediaFile)
        }
        holder.whatsapp_share_button.setOnClickListener {
            onWhatsAppShareButtonClcked(model.mediaFile)
        }
    }

    override fun getItemCount(): Int {
        return StatusModels.size
    }

    fun onDownloaded(button: ImageButton){
        button.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.icon_downloaded))
        button.isEnabled=false
    }
}