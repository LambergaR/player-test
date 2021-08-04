package tv.beenius.demo.player.ui.components.logger

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tv.beenius.demo.player.util.LogMessage
import kotlin.math.abs

class LoggingViewModel: ViewModel() {
    var messages: List<LogMessage> by mutableStateOf(listOf())
        private set

    var manualScrollActive = false

    var listState: LazyListState? = null

    fun addMessage(message: LogMessage) {
        messages = messages + listOf(message)
    }

    fun scrollDown() = listState?.apply {
        viewModelScope.launch {
            scrollBy(200f).let {
                if(abs(it) < 0.001f) {
                    manualScrollActive = false
                }
            }
        }
    }

    fun scrollUp() = listState?.apply {
        viewModelScope.launch {
            scrollBy(-200f).let {
                if(abs(it) > 0.001f) {
                    manualScrollActive = true
                }
            }
        }
    }

    fun scrollReset() = listState?.apply {
        viewModelScope.launch {
            scrollToItem(0)
        }
    }
}