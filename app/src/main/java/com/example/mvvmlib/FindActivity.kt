package com.example.mvvmlib

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvmlib.databinding.ActivityFindBinding
import com.example.mvvmlib.viewmodel.FindViewModel

class FindActivity : AppCompatActivity() {
    private val viewModel = FindViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val findBinding = ActivityFindBinding.inflate(LayoutInflater.from(this))
        findBinding.viewModel = viewModel
        findBinding.lifecycleOwner = this
        setContentView(findBinding.root)
        viewModel.getFindList()
    }
}
