package com.example.mvvmlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mvvm.okhttp.callback.BaseCallback
import com.example.mvvm.okhttp.manager.RetrofitHelper
import com.example.mvvmlib.api.ApiTest
import com.example.mvvm.okhttp.utils.callback
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {

    private val mDis = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val disposable = RetrofitHelper.getInstance().createRequest(ApiTest::class.java)
            .getData("v1557852")
            .callback(object : BaseCallback<String>() {
                override fun onResponse(response: String?) {
                }

                override fun onError(errorData: String?) {
                    Log.w("tag", " errorData--->$errorData")
                }

            })

        mDis.add(disposable)

    }

    override fun onDestroy() {
        super.onDestroy()
        //解除RxJava绑定, 取消Retrofit请求, 防止内存溢出
        mDis.clear()
    }
}
