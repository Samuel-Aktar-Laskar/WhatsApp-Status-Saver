package com.cosmosrsvp.statussaver.ui.Fragments.Status_Fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cosmosrsvp.statussaver.ui.component.statusCard

class Status_fragment : Fragment() {
    val TAG: String= "StatusFragmentTag"
    val viewModel: Satus_Fragment_ViewModel by viewModels()
    @ExperimentalFoundationApi
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
        return ComposeView(requireContext()).
                apply {
                        setContent {
                            Column(modifier = Modifier.fillMaxSize()) {
                                LazyVerticalGrid(
                                    cells =GridCells.Fixed(2) ,
                                    content = {
                                        itemsIndexed(
                                            items = viewModel.mediaFileListInWhatsAppDir.value,
                                        ){index, item->
                                            Log.d(TAG,"Index value: $index and isDownloaded : ${item.isDownloaded}")
                                            statusCard(
                                                model =item,
                                                onDownlaodClicked = {file->
                                                    viewModel.onDownlaodButtonClicked(file)
                                                },
                                                onShareClicked = {file->
                                                    viewModel.onShareButtonClicked(file)
                                                }
                                                )
                                        }
                                    }
                                )
                            }
                        }
                }
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