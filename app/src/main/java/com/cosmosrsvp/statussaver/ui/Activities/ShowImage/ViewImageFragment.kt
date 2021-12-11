package com.cosmosrsvp.statussaver.ui.Activities.ShowImage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.cosmosrsvp.statussaver.R
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import java.io.File


private const val ARG_PARAM1 = "param1"
class ViewImageFragment : Fragment() {

    private lateinit var file: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            file=File(it.getString(ARG_PARAM1))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View= inflater.inflate(R.layout.fragment_view_imge, container, false)
        val image= view.findViewById<SubsamplingScaleImageView>(R.id.displayImage)
        image.setImage(ImageSource.uri(file.absolutePath))
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(file: File) =
            ViewImageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, file.absolutePath)
                }
            }
    }
}