package com.cosmosrsvp.statussaver.domain.util

import android.content.res.Resources.getSystem

val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()
val WidthPixels: Int = getSystem().displayMetrics.widthPixels
val PERCENT_REDUCTION: Double=0.5;