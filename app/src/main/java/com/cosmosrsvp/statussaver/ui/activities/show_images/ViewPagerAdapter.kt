package com.cosmosrsvp.statussaver.ui.activities.show_images

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.domain.extensions.isVideo
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

//private const val TAG = "viewPagerAdapter"

class ViewPagerAdapter
constructor(
    private val context: Context,
    private val uriList: ArrayList<Uri>
    ): RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>(){

    override fun getItemCount(): Int {
        return uriList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val frameLayout: FrameLayout =itemView.findViewById(R.id.frame_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View=LayoutInflater.from(context).inflate(R.layout.display_recycler_layout,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uri: Uri= uriList[position]

        holder.frameLayout.removeAllViews()
        if (!uri.isVideo()){
            val imageView2= SubsamplingScaleImageView(context)
            imageView2.setImage(ImageSource.uri(uri))
            holder.frameLayout.addView(imageView2)
        }
    }

}