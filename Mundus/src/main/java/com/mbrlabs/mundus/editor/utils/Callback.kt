package com.mbrlabs.mundus.editor.utils

interface Callback<T> {

    fun done(result: T)

    fun error(msg: String)

}
