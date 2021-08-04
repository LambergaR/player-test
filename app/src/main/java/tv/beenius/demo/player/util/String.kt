package tv.beenius.demo.player.util

import timber.log.Timber
import tv.beenius.demo.player.ui.components.logger.LoggingViewModel

inline fun String.log(vm: LoggingViewModel) {
    Timber.i(this)
    log(
        vm = vm,
        value = this
    )
}

inline fun String.log(vm: LoggingViewModel, type: LogMessage.Type) {
    when(type) {
        LogMessage.Type.ERROR -> Timber.e(this)
        else -> Timber.i(this)
    }

    log(vm) {
        this.type = type
        lines(this@log)
    }
}
