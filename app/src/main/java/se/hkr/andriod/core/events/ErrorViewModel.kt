package se.hkr.andriod.core.events

import androidx.lifecycle.ViewModel

class ErrorViewModel : ViewModel() {
    val dispatcher = ErrorDispatcher()
}
