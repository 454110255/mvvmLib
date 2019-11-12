package com.example.mvvm.base

import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm.okhttp.utils.ToastUtil

abstract class BaseActivity<R : BaseRepository> : AppCompatActivity(), IBaseViewModel {
    override fun showToast(msg: String) {
        ToastUtil.show(msg)
    }

    override fun onReLogin() {

    }

    override fun onReLoginForResult(requestCode: Int) {

    }

    override fun onCloseActivity() {

    }
}