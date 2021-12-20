package com.cosmosrsvp.statussaver.ui.fragments.downloads_fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.domain.extensions.onDeleteButtonClicked
import com.cosmosrsvp.statussaver.domain.extensions.onShareButtonClicked
import com.cosmosrsvp.statussaver.domain.extensions.onWhatsAppShareButtonClicked
import com.cosmosrsvp.statussaver.domain.extensions.sortList
import com.cosmosrsvp.statussaver.domain.model.DownloadedStatusModel
import com.cosmosrsvp.statussaver.ui.activities.VideoDownloader
import com.cosmosrsvp.statussaver.ui.fragments.adapter.DownloadsMediaAdapter
import com.cosmosrsvp.statussaver.ui.fragments.view_model.FragmentMediaViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*


class Downloads_Fragment: Fragment(), View.OnClickListener {
    val TAG: String= "downloadFragmentTag"
    private val viewModel: FragmentMediaViewModel by activityViewModels()
    private var StatusList: ArrayList<DownloadedStatusModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =inflater.inflate(R.layout.download_fragment, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.download_recycler_view)
        val swipeRefreshLayout: SwipeRefreshLayout= view.findViewById(R.id.refreshDownloads)
        val downloadBtn: FloatingActionButton=view.findViewById(R.id.downloadVideoFab)
        downloadBtn.setOnClickListener(this)



        val adapter = DownloadsMediaAdapter(
            context=requireContext(),
            DownloadedStatusModels = StatusList,
            onShareButtonClicked = {
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    requireActivity().getPackageName().toString() + ".provider",
                    it
                )
                val waIntent= uri.onShareButtonClicked()
                startActivity(waIntent)
            },
            onWhatsAppShareButtonClcked = {
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    requireActivity().getPackageName().toString() + ".provider",
                    it
                )
                val waIntent=uri.onWhatsAppShareButtonClicked()
                startActivity(waIntent)
            },
            onDeleteButtonClicked = {file, adap->
                var undo=false
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
                        undo = true
                    }
                snackbar.show()
                GlobalScope.launch {
                    delay(2750)
                    if (!undo){
                        file.onDeleteButtonClicked()
                        /*withContext(Dispatchers.Main){
                            viewModel.refreshAppMediaFileList()
                        }*/
                    }
                }
            }
        )
        viewModel.mediaFileListInAppDir.observe(requireActivity(),{
            var i=0
            for(o in it){
                if (StatusList.size>i)
                    StatusList.set(i++,o)
                else StatusList.add(o)
            }
            StatusList.sortList()
            adapter.notifyDataSetChanged()
            Log.d(TAG, "Observer called!, status list: $StatusList")
        })
        recyclerView.layoutManager= GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshAppMediaFileList()
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing=false
        }
        return view
    }



    override fun onClick(p0: View?) {
        p0?.let {
            when  (it.id){
                R.id.downloadVideoFab->{                    startActivity(Intent(requireContext(),VideoDownloader::class.java))

                }
            }
        }
    }


}