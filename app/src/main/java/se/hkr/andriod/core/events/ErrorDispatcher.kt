package se.hkr.andriod.core.events

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import se.hkr.andriod.data.network.ActionResponse

class ErrorDispatcher {
    private val _errors = Channel<UiErrorMessage>(Channel.BUFFERED)
    val errors = _errors.receiveAsFlow()

    suspend fun handle(response: ActionResponse) {
        if (response.statusCode >= 400) {
            _errors.send(UiErrorMessage(response.message))
        }
    }
}
