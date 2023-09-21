package com.base.presentation.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * this class is sample event used to emit and receive a any event
 */
object EventBus {
    private val _eventFlow = MutableSharedFlow<EventBusAction>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun publish(event: EventBusAction) = CoroutineScope(Dispatchers.Main).launch {
        _eventFlow.emit(event)
    }
}

sealed class EventBusAction {

}