package com.tb.myskilllane

import android.app.Application
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.tb.myskilllane.database.RealmManager

class TBApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RealmManager.init(this)

        val downloaderConfig = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .build()

        PRDownloader.initialize(this, downloaderConfig)
    }
}