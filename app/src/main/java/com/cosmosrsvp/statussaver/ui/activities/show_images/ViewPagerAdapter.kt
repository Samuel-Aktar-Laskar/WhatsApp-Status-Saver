package com.cosmosrsvp.statussaver.ui.activities.show_images

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.domain.util.isVideo
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import java.io.File
import java.lang.Exception

private const val TAG = "viewpageradapter"

class ViewPagerAdapter
constructor(
    private val context: Context,
    private val fileList: ArrayList<File>,
    private val player: ExoPlayer,
    ): RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>(){

    override fun getItemCount(): Int {
        return fileList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val frameLayout: FrameLayout =itemView.findViewById(R.id.frame_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View=LayoutInflater.from(context).inflate(R.layout.display_recycler_layout,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file: File= fileList[position]

        holder.frameLayout.removeAllViews()
        if (file.isVideo()){
            try {
                val playerView2=StyledPlayerView(context)
                playerView2.player=player
                holder.frameLayout.addView(playerView2)
            }
            catch (e: Exception){
                Log.d(TAG, "Error in loading video: ${e.localizedMessage}")
            }
        }
        else {
            val imageView2= SubsamplingScaleImageView(context)
            imageView2.setImage(ImageSource.uri(file.absolutePath))
            holder.frameLayout.addView(imageView2)
        }
    }

}