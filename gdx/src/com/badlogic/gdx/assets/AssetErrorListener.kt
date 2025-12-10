package com.badlogic.gdx.assets

interface AssetErrorListener {
    fun error(asset: AssetDescriptor<*>, throwable: Throwable)
}
