package com.cosmosrsvp.statussaver.util

import android.os.Environment
import java.io.File

val storageDir: File = Environment.getExternalStorageDirectory()
val appDir: File = File(storageDir, "Status_Saver_With_Video_Downloader")
