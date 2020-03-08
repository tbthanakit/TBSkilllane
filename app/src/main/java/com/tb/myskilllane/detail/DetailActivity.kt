package com.tb.myskilllane.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.Status
import com.tb.myskilllane.R
import com.tb.myskilllane.database.RealmManager
import com.tb.myskilllane.database.entity.DataEntity
import com.tb.myskilllane.extension.gone
import com.tb.myskilllane.extension.visible
import com.tb.myskilllane.model.DataResponse
import com.tb.myskilllane.service.JsonMapperManager
import com.tb.myskilllane.utilities.DownloadUtils
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    val data by lazy {
        intent.getStringExtra(TAG) ?: ""
    }
    var downloader = 0
    private val viewModel by lazy {
        ViewModelProvider(this).get(DetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initViewModel()
        initListener()

    }

    private fun initListener() {
        btn_left.setOnClickListener {
            viewModel.decrease()
        }

        btn_right.setOnClickListener {
            viewModel.increase()
        }
    }

    private fun initViewModel() {
        viewModel.currentPosition.observe(this, Observer {
            display(position = it!!)
        })
    }

    private fun display(position: Int) {
        display_image_layout.visible()
        new_image_layout.gone()

        Glide.with(this)
            .load(viewModel.showingData[position].uriPath)
            .into(iv)

        tv.text = viewModel.showingData[position].author
    }

    override fun onStart() {
        super.onStart()
        toggleDownload()
    }

    override fun onStop() {
        super.onStop()
        toggleDownload()
    }

    private fun toggleDownload() {
        if (imageMustDownload().isNullOrEmpty()) {
            display_image_layout.visible()
            new_image_layout.gone()
            viewModel.getImageForShow(JsonMapperManager.getInstance().gson.fromJson(data, Array<DataResponse>::class.java))
        } else {
            display_image_layout.gone()
            new_image_layout.visible()
            imageMustDownload().forEachIndexed { index, dataEntity ->

                if (Status.RUNNING == PRDownloader.getStatus(downloader)) {
                    PRDownloader.pause(downloader)
                    return
                }

                if (Status.PAUSED == PRDownloader.getStatus(downloader)) {
                    PRDownloader.resume(downloader)
                    return
                }

                downloader = PRDownloader.download(
                        dataEntity.downloadUrl
                        , DownloadUtils.getRootDirPath(this)
                        , "${dataEntity.id}.png"
                    )
                    .build()
                    .setOnStartOrResumeListener { }
                    .setOnPauseListener { }
                    .setOnCancelListener { }
                    .setOnProgressListener { progress ->
                        download_tv.text = DownloadUtils.getProgressDisplayLine(
                            dataEntity.author,
                            progress.currentBytes
                        )

                    }
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            RealmManager.getInstance().updateDownloadStatusById(
                                uriPath = DownloadUtils.getRootDirPath(this@DetailActivity) + "/" + dataEntity.id + ".png",
                                id = dataEntity.id
                            )
                        }

                        override fun onError(error: Error) {}
                    })
            }
            display_image_layout.visible()
            new_image_layout.gone()
            viewModel.getImageForShow(JsonMapperManager.getInstance().gson.fromJson(data, Array<DataResponse>::class.java))
        }
    }

    private fun imageMustDownload(): MutableList<DataEntity> {
        val imagesDownload: MutableList<DataEntity> = mutableListOf()
        val dataJson = JsonMapperManager.getInstance().gson.fromJson(data, Array<DataResponse>::class.java)
        dataJson.forEachIndexed { index, dataResponse ->
            if (RealmManager.getInstance().getDataList().any {
                    dataResponse.id == it.id && dataResponse.author == it.author && !it.isDownload
                }) {
                imagesDownload.add(
                    DataEntity().apply {
                        this.id = dataResponse.id
                        this.author = dataResponse.author
                        this.downloadUrl = dataResponse.downloadUrl
                    }
                )
            }
        }
        return imagesDownload
    }

    companion object {

        val TAG = "DetailActivity"

        fun open(context: Context, data: String) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(TAG, data)
            context.startActivity(intent)
        }

    }
}
