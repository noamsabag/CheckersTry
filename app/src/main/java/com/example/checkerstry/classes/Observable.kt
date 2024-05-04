package com.example.checkerstry.classes

import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

class ObservableProperty<T>(initial: T) {
    private var observers: List<(T, T) -> Unit> = listOf()

    var value: T by observable(initial) { property, oldValue, newValue ->
        observers.forEach { if (oldValue != newValue) it(oldValue, newValue) }
    }

    fun addObserver(observer: (T, T) -> Unit) {
        observers += observer
    }
}