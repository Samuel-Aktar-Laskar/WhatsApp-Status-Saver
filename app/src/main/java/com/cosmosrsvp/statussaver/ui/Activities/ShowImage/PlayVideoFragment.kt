package com.cosmosrsvp.statussaver.ui.Activities.ShowImage

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.ui.Activities.ShowImage.ViewImageActivity.pageNo.PageNo
import com.google.android.exoplayer2.*
import java.io.File
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView

private const val FILE_PATH = "filePaTh"

class PlayVideoFragment : Fragment(){
    val TAG="playVideoFragmenttag"
    private var player: ExoPlayer?=null
    private var playerView: StyledPlayerView?=null
    private lateinit var file: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            file=File(it.getString(FILE_PATH))
        }
        player=null
        player= ExoPlayer.Builder(requireContext()).build()
        player?.playWhenReady=true
        Log.d(TAG, "OnCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "OnStart")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View=inflater.inflate(R.layout.fragment_play_video, container, false)
        playerView=null
        playerView=view.findViewById(R.id.exoplayerView)

        Log.d(TAG, "OnCreateView and file name: ${file.name}")
        Log.d(TAG, "OnCreateView and page  no: $PageNo")

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerView?.let {
            it.player=player
        }

        // Build the media item.
        val uri: Uri=Uri.fromFile(file)
        val mediaItem: MediaItem = MediaItem.fromUri(uri)
        // Set the media item to be played.


            Log.d(TAG,"It is visible");
            player?.setMediaItem(mediaItem)
            // Prepare the player.
            player?.prepare()
            // Start the playback.
            player?.play()

        Log.d(TAG, "OnViewCreated")
    }

    override fun onPause() {
        super.onPause()
        player?.stop()
        Log.d(TAG, "OnPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "OnResume")
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player=null
        playerView=null
        Log.d(TAG, "OnStop")
    }

    companion object {
        @JvmStatic
        fun newInstance(filepath: String) =
            PlayVideoFragment().apply {
                arguments = Bundle().apply {
                    putString(FILE_PATH, filepath)
                }
                Log.d(TAG, "NewInstance")
            }
    }
}