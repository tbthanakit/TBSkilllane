package com.tb.myskilllane.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tb.myskilllane.database.RealmManager
import com.tb.myskilllane.database.entity.DataEntity
import com.tb.myskilllane.model.DataResponse

class DetailViewModel: ViewModel() {

    private var position = 0
    val currentPosition = MutableLiveData<Int>()
    val showingData: MutableList<DataEntity> = mutableListOf()

    fun getImageForShow(datas: Array<DataResponse>) {
        val images: MutableList<DataEntity> = mutableListOf()
        RealmManager.getInstance().getDataList().forEach { realmData ->
            if (datas.any { realmData.id == it.id && realmData.author == it.author
                        && realmData.downloadUrl == it.downloadUrl }) {
                images.add(realmData)
            }
        }
        showingData.clear()
        showingData.addAll(images)
        currentPosition.postValue(position)
    }

    fun increase() {
        position = if (position+1 >= showingData.size) 0 else position+1
        currentPosition.postValue(position)
    }

    fun decrease() {
        position = if (position-1 < 0) showingData.size-1 else position-1
        currentPosition.postValue(position)
    }

}