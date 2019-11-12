package com.example.mvvm.base

class BaseEvent<T>(private val event: T) {
    var hasBeanHandled: Boolean = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeanHandled) {
            null
        } else {
            hasBeanHandled = false
            event
        }
    }
}