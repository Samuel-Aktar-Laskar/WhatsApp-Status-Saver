package com.cosmosrsvp.statussaver.ui.fragments.status_fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts

import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.domain.extensions.onShareButtonClicked
import com.cosmosrsvp.statussaver.domain.extensions.onWhatsAppShareButtonClicked
import com.cosmosrsvp.statussaver.domain.extensions.sortList
import com.cosmosrsvp.statussaver.domain.model.StatusModel
import com.cosmosrsvp.statussaver.domain.util.toast
import com.cosmosrsvp.statussaver.ui.fragments.adapter.StatusMediaAdapter
import com.cosmosrsvp.statussaver.ui.fragments.view_model.FragmentMediaViewModel

class Status_fragment : Fragment() {
    val TAG: String= "StatusFragmentTag"
    val viewModel: FragmentMediaViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.isPermissionGranted.value=true
            }
            else -> {
                // You can directly ask for the permission.
                viewModel.isPermissionGranted.value=false
            }
        }

        viewModel.isPermissionGranted.value?.let {
            if (!it)
                requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
        }

        val view: View=inflater.inflate(R.layout.status_fragmnent, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.status_recycler_view)
        val swipeRefreshLayout: SwipeRefreshLayout = view.findViewById(R.id.refreshStatuses)
        val mainList: ArrayList<StatusModel> = arrayListOf()

        val adapter = StatusMediaAdapter(
            context=requireContext(),
            StatusModels = mainList,
            onDownloadButtonClicked = {
                viewModel.onDownloadButtonClicked(dFile = it, context= requireContext())
            },
            onShareButtonClicked = {
                val waIntent= it.uri.onShareButtonClicked()
                startActivity(waIntent)
            },
            onWhatsAppShareButtonClicked = { documentFile ->
                val waIntent=documentFile.uri.onWhatsAppShareButtonClicked(requireContext())
                waIntent?.let {
                    startActivity(it) //Remember this
                }
            }
            )
        viewModel.getModelsInWhatsAppDir().observe(this){
            Log.d(TAG, "List changed, being observed..")
            var i=0
            for(o in it){
                if (mainList.size>i)
                    mainList[i++] = o
                else {
                    mainList.add(o)
                    i++
                }
            }
            mainList.sortList()
            adapter.notifyDataSetChanged()
        }
        recyclerView.layoutManager= GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshWhatsAppMediaFileList()
            swipeRefreshLayout.isRefreshing=false
        }
        return view
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.isPermissionGranted.value=true
                viewModel.initBlock()
            } else {
                toast(requireContext(),"The app needs this permission to function", true)
            }
        }
}