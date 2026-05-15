package se.hkr.andriod.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import se.hkr.andriod.core.events.ErrorDispatcher

class MainViewModelFactory(
    private val errorDispatcher: ErrorDispatcher
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(errorDispatcher) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
