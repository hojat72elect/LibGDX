package com.badlogic.ashley.signals

import com.badlogic.gdx.utils.Array
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class SignalTests {

    @Test
    fun addListenerAndDispatch() {
        val dummy = Dummy()
        val signal = Signal<Dummy>()
        val listener = ListenerMock()
        signal.add(listener)

        for (i in 0..9) {
            assertEquals(i, listener.count)
            signal.dispatch(dummy)
            assertEquals((i + 1), listener.count)
        }
    }

    @Test
    fun addListenersAndDispatch() {
        val dummy = Dummy()
        val signal = Signal<Dummy>()
        val listeners: Array<ListenerMock> = Array<ListenerMock>()

        val numListeners = 10

        while (listeners.size < numListeners) {
            val listener = ListenerMock()
            listeners.add(listener)
            signal.add(listener)
        }

        val numDispatchs = 10

        for (i in 0..<numDispatchs) {
            for (listener in listeners) {
                assertEquals(i, listener.count)
            }

            signal.dispatch(dummy)

            for (listener in listeners) {
                assertEquals((i + 1), listener.count)
            }
        }
    }

    @Test
    fun addListenerDispatchAndRemove() {
        val dummy = Dummy()
        val signal = Signal<Dummy>()
        val listenerA = ListenerMock()
        val listenerB = ListenerMock()

        signal.add(listenerA)
        signal.add(listenerB)

        val numDispatchs = 5

        for (i in 0..<numDispatchs) {
            assertEquals(i, listenerA.count)
            assertEquals(i, listenerB.count)

            signal.dispatch(dummy)

            assertEquals((i + 1), listenerA.count)
            assertEquals((i + 1), listenerB.count)
        }

        signal.remove(listenerB)

        for (i in 0..<numDispatchs) {
            assertEquals((i + numDispatchs), listenerA.count)
            assertEquals(numDispatchs, listenerB.count)

            signal.dispatch(dummy)

            assertEquals((i + 1 + numDispatchs), listenerA.count)
            assertEquals(numDispatchs, listenerB.count)
        }
    }

    @Test
    fun removeWhileDispatch() {
        val dummy = Dummy()
        val signal = Signal<Dummy>()
        val listenerA = RemoveWhileDispatchListenerMock()
        val listenerB = ListenerMock()

        signal.add(listenerA)
        signal.add(listenerB)

        signal.dispatch(dummy)

        assertEquals(1, listenerA.count)
        assertEquals(1, listenerB.count)
    }

    @Test
    fun removeAll() {
        val dummy = Dummy()
        val signal = Signal<Dummy>()

        val listenerA = ListenerMock()
        val listenerB = ListenerMock()

        signal.add(listenerA)
        signal.add(listenerB)

        signal.dispatch(dummy)

        assertEquals(1, listenerA.count)
        assertEquals(1, listenerB.count)

        signal.removeAllListeners()

        signal.dispatch(dummy)

        assertEquals(1, listenerA.count)
        assertEquals(1, listenerB.count)
    }

    private class Dummy

    private class ListenerMock : Listener<Dummy> {
        var count: Int = 0

        override fun receive(signal: Signal<Dummy>, receivedObject: Dummy) {
            ++count

            assertNotNull(signal)
            assertNotNull(receivedObject)
        }
    }

    private class RemoveWhileDispatchListenerMock : Listener<Dummy> {
        var count: Int = 0

        override fun receive(signal: Signal<Dummy>, receivedObject: Dummy) {
            ++count
            signal.remove(this)
        }
    }
}
