package com.mbrlabs.mundus.editor.history

interface Command {

    fun execute()

    fun undo()

}
