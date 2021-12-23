package com.cosmosrsvp.statussaver.ui.activities.show_images

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.domain.extensions.*
import com.cosmosrsvp.statussaver.domain.util.OnSwipeTouchListener
import com.cosmosrsvp.statussaver.domain.util.toast
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import androidx.core.content.FileProvider






private const val LIST_NAME="dLoadMedFiLst"
private const val CURRENT_POSITION="currentPosi"
private const val TAG = "viewImagesActivity"
private const val IS_DOWNLOADS="isFromDownloads"

class ViewImageActivity: AppCompatActivity(), View.OnClickListener {

    private lateinit var viewPager: ViewPager2
    private lateinit var player: ExoPlayer
    var currentPosition=-1
    lateinit var uriList: ArrayList<Uri>
    private lateinit var  pagerAdapter : ViewPagerAdapter
    private lateinit var deleteBtn: ImageButton
    private lateinit var shareBtn: ImageButton
    private lateinit var whatsappBtn: ImageButton
    private lateinit var downloadBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
            setContentView(R.layout.activity_view_image)
        downloadBtn=findViewById(R.id.download_button1)
        whatsappBtn= findViewById(R.id.whatsapp_share_button1)
        shareBtn= findViewById(R.id.share_button1)
        deleteBtn= findViewById(R.id.delete_button1)
        val playerView: PlayerView= findViewById(R.id.styledPlayerView)//playerView
        downloadBtn.setOnClickListener(this)
        whatsappBtn.setOnClickListener(this)
        shareBtn.setOnClickListener(this)
        deleteBtn.setOnClickListener(this)

        player=ExoPlayer.Builder(this).build()
        playerView.player=player

        var isDownloads: Boolean?=null
        if (intent.hasExtra(IS_DOWNLOADS)){
            isDownloads=intent.getBooleanExtra(IS_DOWNLOADS, false)
            if (isDownloads){
                downloadBtn.visibility= View.GONE
            }
            else{
                deleteBtn.visibility= View.GONE
            }
        }

        if (intent.hasExtra(LIST_NAME) || intent.hasExtra(CURRENT_POSITION)){
            viewPager=findViewById(R.id.pager)
            val fileUriList=intent.getStringArrayListExtra(LIST_NAME)
            if (fileUriList==null) super.onBackPressed()
           uriList = fileUriList?.map{
               Log.d(TAG, "Received string is :${Uri.parse(it).path}")
               Uri.parse(it)
            } as ArrayList<Uri>

            pagerAdapter=ViewPagerAdapter(
                context = this,
                uriList = uriList
            )
            viewPager.adapter=pagerAdapter
            viewPager.currentItem=intent.getIntExtra(CURRENT_POSITION,0)

            playerView.setOnTouchListener(object: OnSwipeTouchListener(applicationContext){
                override fun onSwipeLeft() {
                    super.onSwipeLeft()
                    viewPager.currentItem++
                }
                override fun onSwipeRight() {
                    super.onSwipeRight()
                    viewPager.currentItem--
                }
                override fun onSwipeTop() {
                    super.onSwipeTop()
                    isDownloads?.let {
                        toggleVisibility(false, it)
                    }
                    playerView.showController()
                }
                override fun onSwipeBottom() {
                    super.onSwipeBottom()
                    isDownloads?.let {
                        toggleVisibility(true, it)
                    }
                    playerView.hideController()
                }
            })


            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                        Log.d(TAG, "Page position: $position")
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    Log.d(TAG, "Page Scrolled: $state")
                }

                override fun onPageScrolled(position: Int,
                                            positionOffset: Float,
                                            positionOffsetPixels: Int) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    Log.d(TAG, "On page scrolled: $positionOffset and page position: $position")

                    if (currentPosition!=position){
                        val uri=uriList[position]
                        player.stop()
                        Log.d(TAG, "FIle name is $uri")
                        if (uri.isVideo()){
                            Log.d(TAG, "Video to be played")
                            playerView.visibility=View.VISIBLE
                            val mediaItem: MediaItem = MediaItem.fromUri(uri)
                            player.setMediaItem(mediaItem)
                            player.playWhenReady=true
                            player.prepare()
                            isDownloads?.let {
                                toggleVisibility(false, it)
                            }
                        }
                        else {
                            playerView.visibility=View.GONE
                            isDownloads?.let {
                                toggleVisibility(true, it)
                            }
                        }
                    }
                    currentPosition=position
                }
            })
        }
        else super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            when (p0.id){
                R.id.download_button1->{
                    val b= uriList[currentPosition].onDownloadButtonClicked(this)
                    if (!b)
                    toast(this,"File is already downloaded")
                    else toast(this,"Download successful")
                }
                R.id.whatsapp_share_button1->{
                    uriList[currentPosition].let {
                        val waIntent=it.onWhatsAppShareButtonClicked(this) ?: return
                        startActivity(waIntent)
                    }
                }
                R.id.share_button1->{
                    uriList[currentPosition].let {
                        val waIntent= it.onShareButtonClicked()
                        startActivity(waIntent)
                    }
                }
                R.id.delete_button1->{
                    uriList[currentPosition].onDeleteButtonClicked(this)
                    toast(this, "Deleted")
                    pagerAdapter.notifyItemRemoved(currentPosition)
                    ++viewPager.currentItem
                }
            }
        }
    }

    fun toggleVisibility(switch: Boolean, isDownloads: Boolean){
        val visibility= if (switch) View.VISIBLE else View.GONE
        shareBtn.visibility=visibility
        whatsappBtn.visibility=visibility
        if (isDownloads){
            deleteBtn.visibility=visibility
        }
        else downloadBtn.visibility=visibility

    }
}


