package com.cosmosrsvp.statussaver.ui.fragments.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.cosmosrsvp.statussaver.domain.model.DownloadedStatusModel
import com.cosmosrsvp.statussaver.ui.activities.show_images.ViewImageActivity
import java.io.File

private const val IS_DOWNLOADS="isFromDownloads"
class DownloadsMediaAdapter
    constructor(
    context: Context,
    var DownloadedStatusModels: ArrayList<DownloadedStatusModel>,
    val onShareButtonClicked: (file: File)-> Unit,
    val onWhatsAppShareButtonClcked: (file: File)-> Unit,
    val onDeleteButtonClicked: (file: File, cont: DownloadsMediaAdapter)->Unit
)
    : MediaAdapter(context = context) {
    val TAG="downlaodsMdiaAdaptertag"
    val LIST_NAME="dLoadMedFiLst"
    val CURRENT_POSITION="currentPosi"
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
            Log.d(TAG,"List: $DownloadedStatusModels")
            DownloadedStatusModels.remove(model)
            notifyItemRemoved(holder.layoutPosition)
            onDeleteButtonClicked(model.mediaFile, this)
        }
        holder.itemView.setOnClickListener{
            val intent=Intent(context,ViewImageActivity::class.java)
            intent.putStringArrayListExtra(LIST_NAME,DownloadedStatusModels.map {
                it.mediaFile.absolutePath
            } as ArrayList<String>)
            intent.putExtra(CURRENT_POSITION,position)
            intent.putExtra(IS_DOWNLOADS,true)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return DownloadedStatusModels.size
    }

}