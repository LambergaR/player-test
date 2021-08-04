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
