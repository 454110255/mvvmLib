package com.example.mvvm.okhttp.utils

import com.example.mvvm.okhttp.callback.BaseCallback
import com.example.mvvm.okhttp.callback.DefaultDisposableObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun <T, R> Observable<T>.callback(callback: BaseCallback<R>?): Disposable {

    return subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .doOnSubscribe {
            callback?.let {
                callback.onBefore()
            }
        }
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(DefaultDisposableObserver<T, R>(callback))
}