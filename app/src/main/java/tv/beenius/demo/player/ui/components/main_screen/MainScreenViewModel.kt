package tv.beenius.demo.player.ui.components.main_screen

import androidx.lifecycle.ViewModel

class MainScreenViewModel: ViewModel() {

    private var urlAddedCallback: ((String) -> Unit)? = null
    private var urlAddDialogRequestedCallback: (() -> Unit)? = null
    private var loggerVisibilityCallback: ((Boolean) -> Unit)? = null

    public var loggerVisible: Boolean = true
        private set

    fun showUrlAddDialog() {
        urlAddDialogRequestedCallback?.invoke()
    }

    fun showLogger() {
        if(!loggerVisible) {
            loggerVisible = true
            loggerVisibilityCallback?.invoke(true)
        }
    }

    fun hideLogger() {
        if(loggerVisible) {
            loggerVisible = false
            loggerVisibilityCallback?.invoke(false)
        }
    }

    fun toggleLogger() {
        if(loggerVisible) {
            hideLogger()
        } else {
            showLogger()
        }
    }

    fun notifyUrlAdded(url: String) {
        urlAddedCallback?.invoke(url)
    }

    fun onUrlAdded(l: (String) -> Unit) {
        urlAddedCallback = l
    }

    fun onUrlAddDialogRequested(l: () -> Unit) {
        urlAddDialogRequestedCallback = l
    }

    fun onLoggerVisibilityChanged(l: (Boolean) -> Unit) {
        loggerVisibilityCallback = l
    }

}