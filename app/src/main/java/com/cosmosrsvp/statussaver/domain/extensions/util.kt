package com.cosmosrsvp.statussaver.domain.extensions

import android.content.Context
import android.net.Uri
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.IOException

@Throws(IOException::class)
fun copyBufferedFile(
    context: Context,
    inputUri: Uri,
    outputUri: Uri

) {
    BufferedInputStream(context.contentResolver.openInputStream(inputUri))
        .use { `in` ->
            BufferedOutputStream(context.contentResolver.openOutputStream(outputUri)).use { out ->
                val buf = ByteArray(1024)
                var nosRead: Int
                while (`in`.read(buf).also { nosRead = it } != -1) // read this carefully ...
                {
                    out.write(buf, 0, nosRead)
                }
            }
        }
}