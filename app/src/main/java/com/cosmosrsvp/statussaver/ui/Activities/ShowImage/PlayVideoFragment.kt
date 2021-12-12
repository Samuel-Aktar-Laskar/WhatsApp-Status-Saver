package com.cosmosrsvp.statussaver.ui.Activities.ShowImage

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cosmosrsvp.statussaver.R
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
    private var isPaused=false
    private val IS_PAUSED="SavedBundlePause"


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            file=File(it.getString(FILE_PATH))
        }
        player= ExoPlayer.Builder(requireContext()).build()
        player?.playWhenReady=true
        Log.d(TAG, "OnCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View=inflater.inflate(R.layout.fragment_play_video, container, false)
        playerView=null
        playerView=view.findViewById(R.id.exoplayerView)
        Log.d(TAG, "OnCreateView and file name: ${file.name}")
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "OnSavedInstance")
        outState.putBoolean(IS_PAUSED, isPaused)
        super.onSaveInstanceState(outState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerView?.let {
            it.player=player
        }
        InitializePlayer()
        Log.d(TAG, "OnViewCreated")
        savedInstanceState?.let {
            if(it.getBoolean(IS_PAUSED)){
                onPause()
            }
        }
    }

    fun InitializePlayer(){
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
    }

    override fun onPause() {
        super.onPause()
        player?.stop()
        Log.d(TAG, "OnPause")
    }

    override fun onResume() {
        super.onResume()
        player?.prepare()
        player?.seekTo(0)
        Log.d(TAG, "OnResume")
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player=null
        playerView=null
        Log.d(TAG, "OnStop")
    }




}