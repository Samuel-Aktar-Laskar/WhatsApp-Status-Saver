package com.cosmosrsvp.statussaver.ui.fragments.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.domain.model.StatusModel
import com.cosmosrsvp.statussaver.ui.activities.show_images.ViewImageActivity
import java.io.File

private const val IS_DOWNLOADS="isFromDownloads"
class StatusMediaAdapter
    constructor(
        context: Context,
        val StatusModels: List<StatusModel>,
        val onDownloadButtonClicked: (file: File)-> Unit,
        val onShareButtonClicked: (file: File)-> Unit,
        val onWhatsAppShareButtonClcked: (file: File)-> Unit
    )
    : MediaAdapter(context = context) {
    val LIST_NAME="dLoadMedFiLst"
    val CURRENT_POSITION="currentPosi"
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
        holder.itemView.setOnClickListener{
            val intent= Intent(context, ViewImageActivity::class.java)
            intent.putStringArrayListExtra(LIST_NAME,StatusModels.map {
                it.mediaFile.absolutePath
            } as ArrayList<String>)
            intent.putExtra(CURRENT_POSITION,position)
            intent.putExtra(IS_DOWNLOADS, false)
            context.startActivity(intent)
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