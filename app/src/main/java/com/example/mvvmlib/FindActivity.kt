package com.example.mvvmlib

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmlib.databinding.ActivityFindBinding
import com.example.mvvmlib.viewmodel.FindViewModel
import kotlinx.android.synthetic.main.activity_find.*

class FindActivity : AppCompatActivity() {
    private lateinit var viewModel: FindViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FindViewModel::class.java)
        val findBinding = ActivityFindBinding.inflate(LayoutInflater.from(this))
        findBinding.viewModel = viewModel
        findBinding.lifecycleOwner = this
        setContentView(findBinding.root)
        recycler_list.layoutManager = LinearLayoutManager(this)
        recycler_list.adapter = viewModel.mAdapter

        viewModel.getFindList()
    }
}
