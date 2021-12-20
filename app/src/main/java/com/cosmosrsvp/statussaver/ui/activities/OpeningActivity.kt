package com.cosmosrsvp.statussaver.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.storage.StorageManager
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.cosmosrsvp.statussaver.R
import com.cosmosrsvp.statussaver.domain.util.toast
import com.cosmosrsvp.statussaver.util.TREE_URI
import com.cosmosrsvp.statussaver.util.URI_PATH
import com.cosmosrsvp.statussaver.util.whatsAppDir2
import com.google.android.material.button.MaterialButton

class OpeningActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opening)
        findViewById<MaterialButton>(R.id.permissionButton).setOnClickListener(this)
        if (CheckPermissions()){
            startMainActivity()
        }
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.permissionButton->{
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED){
                        requestPermissionLauncher.launch(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    }
                    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        askPermission()
                    }
                }
            }
        }
    }

    private fun CheckPermissions(): Boolean{
        var b2 = true
        var b1: Boolean = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && whatsAppDir2.exists()) {
            val uri: String?= getSharedPreferences(TREE_URI, MODE_PRIVATE).getString(URI_PATH, null)
            b2 = uri != null
        }
        return b1 && b2
        }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && whatsAppDir2.exists()) {
                    askPermission()
                }
                else {
                    startMainActivity()
                }
            } else {
                toast(this,"The app needs this permission to function", true)
            }
        }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun askPermission() {
        val storageManager = application.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val intent =  storageManager.primaryStorageVolume.createOpenDocumentTreeIntent()

        val targetDirectory = "WhatsApp%2FMedia%2F.Statuses" // add your directory to be selected by the user
        var uri = intent.getParcelableExtra<Uri>("android.provider.extra.INITIAL_URI") as Uri
        var scheme = uri.toString()
        scheme = scheme.replace("/root/", "/document/")
        scheme += "%3A$targetDirectory"
        uri = Uri.parse(scheme)
        intent.putExtra("android.provider.extra.INITIAL_URI", uri)
        requestUri.launch(intent)
    }

    private var requestUri = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result != null && result.resultCode == Activity.RESULT_OK) {
            result.data?.let { treeUri ->
                treeUri.data?.let {
                    contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
                val preferences=getSharedPreferences(TREE_URI, MODE_PRIVATE)
                preferences.edit().putString(URI_PATH,treeUri.data.toString()).apply()
                startMainActivity()
            }
        }
    }
    private fun startMainActivity(){
        val intent= Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }
}