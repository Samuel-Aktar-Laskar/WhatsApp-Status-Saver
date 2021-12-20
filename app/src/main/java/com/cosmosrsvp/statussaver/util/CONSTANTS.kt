package com.cosmosrsvp.statussaver.util

import android.os.Environment
import java.io.File

//val storageDir: File = Environment.getExternalStorageDirectory()
val storageDir: File = Environment.getExternalStorageDirectory()
val appDir: File = File(storageDir, "Status_Saver_With_Video_Downloader")
val whatsAppDir1= File(storageDir,"WhatsApp/Media/.Statuses")
val whatsAppDir2= File(storageDir, "Android/media/com.whatsapp/WhatsApp/Media/.Statuses")


val EXISTS=-452
val NONEXISTENT=-453
