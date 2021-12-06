package com.cosmosrsvp.statussaver.ui.Fragments.Status_Fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.ui.Fragments.Adapter.StatusMediaAdapter
import com.cosmosrsvp.statussaver.ui.Fragments.ViewModel.fragment_MediaViewModel

class Status_fragment : Fragment() {
    val TAG: String= "StatusFragmentTag"
    val viewModel: fragment_MediaViewModel by activityViewModels()
    @OptIn(ExperimentalFoundationApi::class)
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

        if (!viewModel.isPermissionGranted.value)
            requestPermissionLauncher.launch(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

        val view: View=inflater.inflate(R.layout.status_fragmnent, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.status_recycler_view)
        val adapter = StatusMediaAdapter(
            context=requireContext(),
            StatusModels = viewModel.mediaFileListInWhatsAppDir.value,
            onDownloadButtonClicked = {
                viewModel.onDownloadButtonClicked(it)
            },
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
            }
            )
        recyclerView.layoutManager= GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter
        return view
    }
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.isPermissionGranted.value=true
            } else {
                Toast.makeText(requireContext(), "The app needs this permission to function", Toast.LENGTH_LONG).show()
            }
        }
}