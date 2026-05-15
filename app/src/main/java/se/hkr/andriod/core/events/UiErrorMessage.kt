package se.hkr.andriod.core.events

data class UiErrorMessage(
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
