package com.cosmosrsvp.statussaver.domain.util

import android.content.Context
import android.content.res.Resources.getSystem
import android.widget.Toast

val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()
val WidthPixels: Int = getSystem().displayMetrics.widthPixels
val PERCENT_REDUCTION: Double=0.5;

fun toast(context: Context,text: String,  isLong: Boolean=false ){
    Toast.makeText(context, text, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}

