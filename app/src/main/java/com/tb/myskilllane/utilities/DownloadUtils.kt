package com.tb.myskilllane.utilities

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import java.util.*

object DownloadUtils {

    fun getRootDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }

    fun getProgressDisplayLine(author: String, currentBytes: Long): String {
        return "Download Author $author => ${getBytesToKBString(currentBytes)} downloading."
    }

    private fun getBytesToKBString(bytes: Long): String {
        return String.format(Locale.ENGLISH, "%3d kb", bytes / 1024)
    }

}