package com.cosmosrsvp.statussaver.ui.fragments.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import com.bumptech.glide.Glide
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.domain.model.StatusModel
import com.cosmosrsvp.statussaver.ui.activities.show_images.ViewImageActivity

private const val IS_DOWNLOADS="isFromDownloads"
private const val TAG="StatusMediaAdapter"
class StatusMediaAdapter
    constructor(
        context: Context,
        val StatusModels: List<StatusModel>,
        val onDownloadButtonClicked: (dFile: DocumentFile)-> Unit,
        val onShareButtonClicked: (dFile: DocumentFile)-> Unit,
        val onWhatsAppShareButtonClicked: (dFile: DocumentFile)-> Unit
    )
    : MediaAdapter(context = context) {
    val LIST_NAME="dLoadMedFiLst"
    val CURRENT_POSITION="currentPosi"
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val model: StatusModel = StatusModels.get(position)
        Log.d(TAG, "OnBindViewHolder name :${model.mediaFile}")
        Glide.with(context).load(model.mediaFile.uri).into(holder.ImageView)
        holder.playImg.visibility= if (model.isVideo) View.VISIBLE else View.GONE
        holder.delete_button.visibility= View.GONE

        toggleDownloaded(holder.downlaod_button,model.isDownloaded)


        holder.downlaod_button.setOnClickListener {
            onDownloadButtonClicked(model.mediaFile)
            toggleDownloaded(holder.downlaod_button,true)
        }
        holder.share_button.setOnClickListener {
            onShareButtonClicked(model.mediaFile)
        }
        holder.whatsapp_share_button.setOnClickListener {
            onWhatsAppShareButtonClicked(model.mediaFile)
        }
        holder.itemView.setOnClickListener{
            val intent= Intent(context, ViewImageActivity::class.java)
            intent.putStringArrayListExtra(LIST_NAME,StatusModels.map {
                it.mediaFile.uri.toString() // Check this again
            } as ArrayList<String>)
            intent.putExtra(CURRENT_POSITION,position)
            intent.putExtra(IS_DOWNLOADS, false)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return StatusModels.size
    }

    private fun toggleDownloaded(button: ImageButton, switch: Boolean){
        // True means it is downloaded, vice versa
        button.setImageDrawable(ContextCompat.getDrawable(context,
            if (switch)
            R.drawable.icon_downloaded
        else R.drawable.outline_file_download_20
        ))
        button.isEnabled=!switch
    }
}