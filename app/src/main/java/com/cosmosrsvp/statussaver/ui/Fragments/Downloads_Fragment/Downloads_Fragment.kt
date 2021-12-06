package com.cosmosrsvp.statussaver.ui.Fragments.Downloads_Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.ui.Fragments.Adapter.DownloadsMediaAdapter
import com.cosmosrsvp.statussaver.ui.Fragments.ViewModel.fragment_MediaViewModel
import com.google.android.material.snackbar.Snackbar




class Downloads_Fragment: Fragment() {
    val TAG: String= "downloadFragmentTag"
    val viewModel: fragment_MediaViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =inflater.inflate(R.layout.download_fragment, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.download_recycler_view)
        val adapter = DownloadsMediaAdapter(
            context=requireContext(),
            DownloadedStatusModels = viewModel.mediaFileListInAppDir.value,
            onShareButtonClicked = {
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    requireActivity().getPackageName().toString() + ".provider",
                    it
                )
                val waIntent= viewModel.onShareButtonClicked(uri)
                startActivity(waIntent)
            },
            onWhatsAppShareButtonClcked = {
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    requireActivity().getPackageName().toString() + ".provider",
                    it
                )
                val waIntent=viewModel.onWhatsAppShareButtonClicked(uri)
                startActivity(waIntent)
            },
            onDeleteButtonClicked = {file, adap->
                val snackbar = Snackbar
                    .make(view, "Status is deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        val snackbar1 = Snackbar.make(
                            view,
                            "Status is restored!",
                            Snackbar.LENGTH_SHORT
                        )
                        snackbar1.show()
                        viewModel.refreshAppMediaFileList()
                        adap.notifyDataSetChanged()
                    }
                viewModel.refreshAppMediaFileList()

                snackbar.show()
                false
            }
        )
        recyclerView.layoutManager= GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter
        return view
    }
}