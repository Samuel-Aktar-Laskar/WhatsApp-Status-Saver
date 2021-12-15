package com.cosmosrsvp.statussaver.ui.activities.show_images

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.viewpager2.widget.ViewPager2
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.domain.util.isVideo
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import java.io.File

class ViewImageActivity: AppCompatActivity() {
    private val LIST_NAME="dLoadMedFiLst"
    private val CURRENT_POSITION="currentPosi"
    private lateinit var viewPager: ViewPager2
    private val TAG = "viewImgaeActivity"
    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_view_image)

        if (intent.hasExtra(LIST_NAME) || intent.hasExtra(CURRENT_POSITION)){
            viewPager=findViewById(R.id.pager)
            val FilePathList=intent.getStringArrayListExtra(LIST_NAME)
            if (FilePathList==null) super.onBackPressed()

            val FileList: ArrayList<File> = FilePathList?.map{
                File(it)
            } as ArrayList<File>

            player=ExoPlayer.Builder(this).build()
            player.playWhenReady=true
            val pagerAdapter=ViewPagerAdapter(
                this,
                FileList,
                player= player
            )
            viewPager.adapter=pagerAdapter
            viewPager.currentItem=intent.getIntExtra(CURRENT_POSITION,0)

            var currentPosition=-1
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
                        val file=FileList[position]
                        player.stop()

                        if (file.isVideo()){
                            val uri: Uri = Uri.fromFile(file)
                            val mediaItem: MediaItem = MediaItem.fromUri(uri)
                            player.setMediaItem(mediaItem)
                            player.playWhenReady=true
                            player.prepare()
                        }
                        // playerView.player=player
                    }
                        currentPosition=position
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        player.stop()
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }
}
