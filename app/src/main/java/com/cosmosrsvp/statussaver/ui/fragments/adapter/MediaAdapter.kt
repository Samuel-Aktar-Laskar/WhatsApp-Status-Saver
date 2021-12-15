package com.cosmosrsvp.statussaver.ui.fragments.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cosmosrsvp.statussaver.R

open class MediaAdapter
constructor(
     val context: Context,
    )
    : RecyclerView.Adapter<MediaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.status_card, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return -1
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ImageView = itemView.findViewById<ImageView>(R.id.status_picture)
        val playImg= itemView.findViewById<ImageView>(R.id.play_img)
        val downlaod_button: ImageButton= itemView.findViewById<ImageButton>(R.id.download_button)
        val whatsapp_share_button= itemView.findViewById<ImageButton>(R.id.whatsapp_share_button)
        val share_button= itemView.findViewById<ImageButton>(R.id.share_button)
        val delete_button= itemView.findViewById<ImageButton>(R.id.delete_button)
    }

}