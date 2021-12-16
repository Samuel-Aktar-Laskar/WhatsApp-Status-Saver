package com.cosmosrsvp.statussaver.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cosmosrsvp.statussaver.R
import at.huber.youtubeExtractor.VideoMeta

import at.huber.youtubeExtractor.YtFile

import android.util.SparseArray
import android.widget.Button

import at.huber.youtubeExtractor.YouTubeExtractor
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "DownloadsActivity"
class VideoDownloader : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_downloader)
        val downloadBtn: Button = findViewById(R.id.downloadVideo)
        val youtubeLinkEdt: TextInputEditText=findViewById(R.id.url)



        downloadBtn.setOnClickListener{
            try {


                object : YouTubeExtractor(this) {
                    override fun onExtractionComplete(
                        ytFiles: SparseArray<YtFile>?,
                        vMeta: VideoMeta?
                    ) {
                        if (ytFiles != null) {
                            val itag = 22
                            val downloadUrl = ytFiles[itag].url
                            Log.d(TAG, "Download url: $downloadUrl")
                        }
                    }
                }.extract(youtubeLinkEdt.text.toString())


            }
            catch (e: Exception){
                Log.d(TAG, "Error in extracting url: ${e.localizedMessage}")
            }

        }


    }
}