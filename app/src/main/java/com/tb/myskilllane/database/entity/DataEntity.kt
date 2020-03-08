package com.tb.myskilllane.database.entity

import io.realm.RealmObject

open class DataEntity(
    var id: String = "",
    var author: String = "",
    var downloadUrl: String = "",
    var isDownload: Boolean = false,
    var uriPath: String = ""
) : RealmObject() {

    fun transformToRealm(data: DataEntity) {
        this@DataEntity.id = data.id
        this@DataEntity.author = data.author
        this@DataEntity.downloadUrl = data.downloadUrl
        this@DataEntity.isDownload = data.isDownload
        this@DataEntity.uriPath = data.uriPath
    }

    fun transform(): DataEntity{
        return DataEntity().apply {
            this.id = this@DataEntity.id
            this.author = this@DataEntity.author
            this.downloadUrl = this@DataEntity.downloadUrl
            this.isDownload = this@DataEntity.isDownload
            this.uriPath = this@DataEntity.uriPath
        }
    }

}