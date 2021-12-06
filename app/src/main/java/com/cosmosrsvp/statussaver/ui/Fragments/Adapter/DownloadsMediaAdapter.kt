package com.cosmosrsvp.statussaver.ui.Fragments.Adapter

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.cosmosrsvp.statussaver.domain.model.DownloadedStatusModel
import com.cosmosrsvp.statussaver.domain.model.StatusModel
import java.io.File

class DownloadsMediaAdapter(
    context: Context,
    val DownloadedStatusModels: MutableList<DownloadedStatusModel>,
    val onShareButtonClicked: (file: File)-> Unit,
    val onWhatsAppShareButtonClcked: (file: File)-> Unit,
    val onDeleteButtonClicked: (file: File, cont: DownloadsMediaAdapter)->Unit
)
    : MediaAdapter(context = context) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val model: DownloadedStatusModel = DownloadedStatusModels[position]
        Glide.with(context).load(model.mediaFile).into(holder.ImageView)
        holder.playImg.visibility = if (model.isVideo) View.VISIBLE else View.GONE
        holder.downlaod_button.visibility = View.GONE
        holder.share_button.setOnClickListener {
            onShareButtonClicked(model.mediaFile)
        }
        holder.whatsapp_share_button.setOnClickListener {
            onWhatsAppShareButtonClcked(model.mediaFile)
        }
        holder.delete_button.setOnClickListener {
            DownloadedStatusModels.remove(model)
            notifyItemRemoved(holder.layoutPosition)
            onDeleteButtonClicked(model.mediaFile, this)
            notifyDataSetChanged()

        }
    }

    override fun getItemCount(): Int {
        return DownloadedStatusModels.size
    }
}