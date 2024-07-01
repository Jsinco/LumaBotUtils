package dev.jsinco.lumabotutils.listeners

interface Listener {
    fun onEvent(type: ListenerType, event: Any?)

    fun registerFor(): List<ListenerType>
}