package com.mbrlabs.mundus.editor.history

import com.badlogic.gdx.utils.Array

/**
 * Add commands to undo/redo a previously called process.
 */
class CommandHistory(private val limit: Int) {

    private var pointer: Int = 0
    private val commands: Array<Command> = Array(limit)

    init {
        pointer = -1
    }

    fun add(command: Command): Int {
        if (size() == 0) {
            commands.add(command)
            pointer++
            return pointer
        }

        if (pointer < size() - 1) {
            removeCommands(pointer + 1, commands.size - 1)
            commands.add(command)
            pointer++
        } else {
            if (size() == limit) {
                removeCommand(0)
                commands.add(command)
            } else {
                commands.add(command)
                pointer++
            }
        }

        return pointer
    }

    private fun removeCommand(index: Int) {
        val cmd = commands.get(index)
        if (cmd is DisposableCommand) {
            cmd.dispose()
        }
        commands.removeIndex(index)
    }

    private fun removeCommands(from: Int, to: Int) {
        for (i in from..to) {
            val cmd = commands.get(i)
            if (cmd is DisposableCommand) {
                cmd.dispose()
            }
        }

        commands.removeRange(from, to)
    }

    fun goBack(): Int {
        if (pointer >= 0) {
            commands.get(pointer).undo()
            pointer--
        }

        return pointer
    }

    fun goForward(): Int {
        if (pointer < commands.size - 1) {
            pointer++
            commands.get(pointer).execute()
        }

        return pointer
    }

    fun clear() {
        commands.filterIsInstance<DisposableCommand>()
            .forEach { it.dispose() }
        commands.clear()
        pointer = -1
    }

    fun size(): Int {
        return commands.size
    }

    companion object {

        val DEFAULT_LIMIT = 50
    }

}
