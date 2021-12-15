package com.cosmosrsvp.statussaver.domain.extensions

import android.content.Intent
import android.net.Uri

fun Uri.onShareButtonClicked(): Intent {
    val waIntent= Intent(Intent.ACTION_SEND)
    waIntent.setType("image/jpg")
    waIntent.putExtra(Intent.EXTRA_STREAM,this)
    return  waIntent
}

fun Uri.onWhatsAppShareButtonClicked(): Intent {
    val waIntent= Intent(Intent.ACTION_SEND)
    waIntent.setType("image/jpg")
    waIntent.setPackage("com.whatsapp")
    waIntent.putExtra(Intent.EXTRA_STREAM,this)
    return  waIntent
}