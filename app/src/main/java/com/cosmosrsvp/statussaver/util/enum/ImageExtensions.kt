package com.cosmosrsvp.statussaver.util.enum

enum class ImageExtensions(
    val value: String
) {
    PNG("png"),
    EPS("eps"),
    JPEG("jpeg"),
    JPG("jpg")
}

fun getAllImageExtensions(): List<ImageExtensions>{
    return listOf(ImageExtensions.EPS,ImageExtensions.JPEG, ImageExtensions.JPG, ImageExtensions.PNG)
}