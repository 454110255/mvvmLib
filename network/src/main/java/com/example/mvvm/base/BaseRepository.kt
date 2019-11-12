package com.example.mvvm.base

import com.example.mvvm.okhttp.callback.BaseCallback
import com.example.mvvm.okhttp.callback.DefaultDisposableObserver
import com.example.mvvm.okhttp.config.RetrofitConfig
import com.example.mvvm.okhttp.manager.RetrofitHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

open class BaseRepository {
    private val mDis = CompositeDisposable()
    fun <T> getService(cls: Class<T>): T {
        return RetrofitHelper.getInstance().createRequest(cls, getBaseUrl())
    }

    protected open fun getBaseUrl(): String {
        return RetrofitConfig.BASE_URL
    }

    protected fun <T, R> execute(observable: Observable<T>, callback: BaseCallback<R>) {
        val observer = observable.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe {
                callback?.let {
                    callback.onBefore()
                }
            }
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(DefaultDisposableObserver<T, R>(callback))
        mDis?.add(observer)
    }

    /***
     * 解除绑定
     */
    fun destroy() {
        mDis?.clear()
    }


}
