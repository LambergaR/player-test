package tv.beenius.demo.player.util

import tv.beenius.demo.player.ui.components.logger.LoggingViewModel
import java.time.Instant

fun log(vm: LoggingViewModel, l: LogMessage.() -> Unit) {
    vm.addMessage(
        LogMessage().apply {
            l(this)
        }
    )
}

fun log(vm: LoggingViewModel, value: String) {
    vm.addMessage(LogMessage().apply {
        lines = arrayListOf(value)
    })
}

class LogMessage{
    var lines: List<String> = emptyList()
    var instant: Instant = Instant.now()
    var type: Type = Type.LOG

    enum class Type {
        LOG, ERROR
    }

    fun lines(vararg logLines: String) {
        lines = arrayListOf(*logLines)
    }
}