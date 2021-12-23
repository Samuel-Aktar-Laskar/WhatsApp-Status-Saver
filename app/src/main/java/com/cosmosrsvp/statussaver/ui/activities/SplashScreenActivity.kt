package com.cosmosrsvp.statussaver.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.cosmosrsvp.statussaver.util.TREE_URI
import com.cosmosrsvp.statussaver.util.URI_PATH
import com.cosmosrsvp.statussaver.util.whatsAppDir2
import kotlinx.coroutines.*


class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.Default).launch {
            delay(750)
                if (checkPermissions()){
                    val intent= Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    startActivity(Intent(this@SplashScreenActivity, OpeningActivity::class.java))
                    finish()
                }
        }
    }
    private fun checkPermissions(): Boolean{
        var b2 = true
        val b1: Boolean = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && whatsAppDir2.exists()) {
            val uri: String?= getSharedPreferences(TREE_URI, MODE_PRIVATE).getString(URI_PATH, null)
            b2 = uri != null
        }
        return b1 && b2
    }
}