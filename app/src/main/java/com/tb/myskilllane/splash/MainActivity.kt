package com.tb.myskilllane.splash

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tb.myskilllane.detail.DetailActivity
import com.tb.myskilllane.service.JsonMapperManager


class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        hideNavigationBar()
        initViewModel()

        viewModel.getData()
    }

    private fun initViewModel() {
        viewModel.whenDataFailure.observe(this, Observer {
            if (it!!) {
                AlertDialog.Builder(this)
                    .setTitle("Sorry, something went wrong !")
                    .setPositiveButton("RETRY") { _, _ ->
                        viewModel.getData()
                    }
                    .create()
                    .show()
            }
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            DetailActivity.open(this, JsonMapperManager.getInstance().gson.toJson(it!!))
        })
    }

    private fun hideNavigationBar() {
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
    }
}
