package com.cosmosrsvp.statussaver.ui.activities.show_images

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import androidx.core.content.FileProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.domain.extensions.*
import com.cosmosrsvp.statussaver.domain.util.toast
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import java.io.File


private const val LIST_NAME="dLoadMedFiLst"
private const val CURRENT_POSITION="currentPosi"
private const val TAG = "viewImgaeActivity"
private const val IS_DOWNLOADS="isFromDownloads"

class ViewImageActivity: AppCompatActivity(), View.OnClickListener {

    private lateinit var viewPager: ViewPager2
    private lateinit var player: ExoPlayer
    var currentPosition=-1
    lateinit var FileList: ArrayList<File>
    lateinit var  pagerAdapter : ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_view_image)
        val downloadBtn: ImageButton=findViewById(R.id.download_button1)
        val whatsappBtn: ImageButton= findViewById(R.id.whatsapp_share_button1)
        val shareBtn: ImageButton= findViewById(R.id.share_button1)
        val deleteBtn: ImageButton= findViewById(R.id.delete_button1)
        downloadBtn.setOnClickListener(this)
        whatsappBtn.setOnClickListener(this)
        shareBtn.setOnClickListener(this)
        deleteBtn.setOnClickListener(this)

        if (intent.hasExtra(IS_DOWNLOADS)){
            if (intent.getBooleanExtra(IS_DOWNLOADS, false)){
                downloadBtn.visibility= View.GONE
            }
            else{
                deleteBtn.visibility= View.GONE
            }
        }


        if (intent.hasExtra(LIST_NAME) || intent.hasExtra(CURRENT_POSITION)){
            viewPager=findViewById(R.id.pager)
            val FilePathList=intent.getStringArrayListExtra(LIST_NAME)
            if (FilePathList==null) super.onBackPressed()
           FileList = FilePathList?.map{
                File(it)
            } as ArrayList<File>

            player=ExoPlayer.Builder(this).build()
            player.playWhenReady=true
             pagerAdapter=ViewPagerAdapter(
                this,
                FileList,
                player= player
            )
            viewPager.adapter=pagerAdapter
            viewPager.currentItem=intent.getIntExtra(CURRENT_POSITION,0)

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
        else super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        player.stop()
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            when (p0.id){
                R.id.download_button1->{
                    val b=FileList.get(currentPosition).onDownloadButtonClicked()
                    if (!b)
                    toast(this,"File is already downloaded")
                    else toast(this,"Download successful")
                }
                R.id.whatsapp_share_button1->{
                    FileList.get(currentPosition).let {
                        val uri = FileProvider.getUriForFile(
                            this,
                            getPackageName().toString() + ".provider",
                            it
                            )
                        val waIntent=uri.onWhatsAppShareButtonClicked()
                        startActivity(waIntent)
                    }

                }
                R.id.share_button1->{
                    FileList.get(currentPosition).let {
                        val uri = FileProvider.getUriForFile(
                            this,
                            getPackageName().toString() + ".provider",
                            it
                        )
                        val waIntent= uri.onShareButtonClicked()
                        startActivity(waIntent)
                    }
                }
                R.id.delete_button1->{
                    FileList.get(currentPosition).onDeleteButtonClicked()
                    toast(this, "Deleted")
                    pagerAdapter.notifyItemRemoved(currentPosition)
                    ++viewPager.currentItem
                }

            }
        }
    }
}
