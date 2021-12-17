package com.cosmosrsvp.statussaver.ui.activities


import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cosmosrsvp.statussaver.R
import com.google.android.material.textfield.TextInputEditText
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.cosmosrsvp.statussaver.domain.util.downloadUTubeVideo
import com.cosmosrsvp.statussaver.util.enum.VideoType
import android.content.IntentFilter
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.cosmosrsvp.statussaver.domain.extensions.*
import com.cosmosrsvp.statussaver.domain.util.FileWrapper
import com.cosmosrsvp.statussaver.domain.util.toast
import java.util.concurrent.atomic.AtomicLong


private const val TAG = "DownloadsActivity"
val quality = arrayOf("High Quality", "Normal Quality")

class VideoDownloader : AppCompatActivity() , View.OnClickListener{
    private lateinit var youtubeLinkEdt: TextInputEditText
    private lateinit var spinner: Spinner
    private lateinit var vidThumbnail: AppCompatImageView
    private lateinit var buttonPanel: LinearLayoutCompat
    private lateinit var downloadBtn: AppCompatButton
    private var downloadReference: AtomicLong = AtomicLong(0)
    private val fileReference: FileWrapper= FileWrapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_downloader)
        downloadBtn = findViewById(R.id.downloadVideo)
        youtubeLinkEdt = findViewById(R.id.url)
        vidThumbnail=findViewById(R.id.vid_thumbnail)
        spinner=findViewById(R.id.quality_spinner)
        buttonPanel=findViewById(R.id.buttonPanel)
        findViewById<AppCompatImageButton>(R.id.whatsapp_share_button2).setOnClickListener(this)
        findViewById<AppCompatImageButton>(R.id.share_button2).setOnClickListener(this)
        findViewById<AppCompatImageButton>(R.id.delete_button2).setOnClickListener(this)
        downloadBtn.setOnClickListener(this)
        buttonPanel.visibility=View.GONE

        val spinnerArray: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            quality
            )
        spinnerArray.setDropDownViewResource(R.layout.simple_spinner_item)
        spinner.adapter=spinnerArray

        youtubeLinkEdt.addTextChangedListener{
            Log.d(TAG, "On Text Change :$it")
            handleTextChange()
        }

    }

    override fun onResume() {
        super.onResume()
        if (intent?.action == Intent.ACTION_SEND) {
            if ("text/plain" == intent.type)
                handleSendText(intent)
        }
    }

    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            youtubeLinkEdt.setText(it)
            handleTextChange()
        }
    }

    private fun handleTextChange(){
        val url: String =youtubeLinkEdt.text.toString()
        buttonPanel.visibility=View.GONE
        downloadBtn.isEnabled=true
        when(url.LinkType()){
            VideoType.UTUBE_VIDEO-> {
                val url1="https://img.youtube.com/vi/${url.getMainId(VideoType.UTUBE_VIDEO)}/0.jpg"
                Glide.with(this).load(url1).into(vidThumbnail)
            }
            VideoType.UTUBE_SHORTS-> {
                val url1="https://img.youtube.com/vi/${url.getMainId(VideoType.UTUBE_SHORTS)}/0.jpg"
                Log.d(TAG, "Link :$url1")
                Glide.with(this).load(url1).into(vidThumbnail)
            }
            else -> {}
        }

    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (p0.id){
                R.id.downloadVideo->{
                    val link=youtubeLinkEdt.text.toString()
                    if (link.isEmpty()){
                        youtubeLinkEdt.requestFocus()
                        youtubeLinkEdt.error = "Please enter link"
                        return
                    }
                    when(link.LinkType()){
                        VideoType.UTUBE_VIDEO-> {
                            link.ToGoodUrl(VideoType.UTUBE_VIDEO)
                                ?.let { gLink ->
                                    downloadUTubeVideo(
                                        gLink,
                                        applicationContext,
                                        spinner.selectedItemPosition == 1,
                                        downloadReference,
                                        fileReference
                                    ){
                                        downloadBtn.isEnabled=true
                                    }
                                }
                            val url="https://img.youtube.com/vi/${link.getMainId(VideoType.UTUBE_VIDEO)}/0.jpg"
                            Glide.with(this).load(url).into(vidThumbnail)
                            toast(applicationContext, "Initializing download")
                            downloadBtn.isEnabled=false
                        }

                        VideoType.UTUBE_SHORTS-> {
                            link.ToGoodUrl(VideoType.UTUBE_SHORTS)
                                ?.let { gLink ->
                                    downloadUTubeVideo(
                                        gLink,
                                        applicationContext,
                                        spinner.selectedItemPosition == 0,
                                        downloadReference,
                                        fileReference
                                    ){
                                        downloadBtn.isEnabled=true
                                    }
                                }
                            val url="https://img.youtube.com/vi/${link.getMainId(VideoType.UTUBE_SHORTS)}/0.jpg"
                            Glide.with(this).load(url).into(vidThumbnail)
                            downloadBtn.isEnabled=false
                            toast(applicationContext, "Initializing download")
                        }

                        VideoType.UNKNOWN_TYPE-> {
                            toast(applicationContext, "Link not supported for download")
                        }

                        else -> {}
                    }
                    val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                    registerReceiver(broadcastReceiver, filter)

                }
                R.id.whatsapp_share_button2->{
                    fileReference.file?.let {
                        val uri = FileProvider.getUriForFile(
                            this,
                            getPackageName().toString() + ".provider",
                            it
                        )
                        val waIntent=uri.onWhatsAppShareButtonClicked()
                        startActivity(waIntent)
                    }
                }
                R.id.share_button2->{
                    fileReference.file?.let {
                        val uri = FileProvider.getUriForFile(
                            this,
                            getPackageName().toString() + ".provider",
                            it
                        )
                        val waIntent= uri.onShareButtonClicked()
                        startActivity(waIntent)
                    }
                }
                R.id.delete_button2->{
                    fileReference.file?.onDeleteButtonClicked()
                    buttonPanel.visibility=View.GONE
                    toast(this, "Deleted")
                }
                else -> {}
            }
        }
    }

    private val broadcastReceiver: BroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {

            val referenceId = p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (referenceId== null ) return
            Log.d(TAG, "Broadcase receiver called with id :$referenceId and our id is :$downloadReference")
            if(referenceId == downloadReference.get()){
                Log.d(TAG, "Download Complete")
                toast(applicationContext, "Download Completed")
                downloadBtn.isEnabled=true
                buttonPanel.visibility=View.VISIBLE

            }
        }

    }


}