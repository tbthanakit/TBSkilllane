package com.tb.myskilllane.database

import android.content.Context
import com.tb.myskilllane.database.entity.DataEntity
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject

class RealmManager private constructor() {

    private val DB_NAME = "TB.realm"
    private lateinit var realmConfiguration: RealmConfiguration

    private fun initDatabase() {
        realmConfiguration = getConfig()
    }

    fun updateDownloadStatusById(uriPath: String?, id: String?) {
        if (uriPath.isNullOrEmpty() && id.isNullOrEmpty()) {
            return
        }

        val data = getDataListById(id = id!!).apply {
            this.isDownload = true
            this.uriPath = uriPath!!
        }

        saveUpdateData(data)
    }

    private fun saveUpdateData(data: DataEntity) {
        val realm = Realm.getInstance(realmConfiguration)
        var entity = realm.where(DataEntity::class.java).findAll().first { it.id == data.id }

        realm.beginTransaction()

        if (entity == null) {
            entity = realm.createObject(DataEntity::class.java)
        }

        entity!!.transformToRealm(data)

        realm.commitTransaction()
        realm.close()
    }

    fun getDataList(): MutableList<DataEntity> {
        val realm = Realm.getInstance(realmConfiguration)
        val entities = realm.where(DataEntity::class.java).findAll()
        val result = realm.copyFromRealm(entities)
        realm.close()
        return result
    }

    fun getDataListById(id: String): DataEntity {
        val realm = Realm.getInstance(realmConfiguration)
        val entities = realm.where(DataEntity::class.java).findAll().first { it.id == id }
        val result = realm.copyFromRealm(entities)
        realm.close()
        return result
    }

    fun saveData(item: DataEntity) {
        val realm = Realm.getInstance(realmConfiguration)
        realm.beginTransaction()
        realm.copyToRealm(item)
        realm.commitTransaction()
        realm.close()
    }

    fun <DataEntity : RealmObject> save(realmClass: Class<DataEntity>, list: List<DataEntity>) {
        val realm = Realm.getInstance(realmConfiguration)
        realm.beginTransaction()
        realm.copyToRealm(list)
        realm.commitTransaction()
        realm.close()
    }

    private fun getConfig(): RealmConfiguration {
        val realmConfig = RealmConfiguration.Builder()
            .name(DB_NAME)
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(1)

        return realmConfig.build()
    }

    companion object {
        private val realmManager = RealmManager()

        fun init(context: Context) {
            Realm.init(context)
            realmManager.initDatabase()
        }

        fun getInstance() = realmManager
    }

}