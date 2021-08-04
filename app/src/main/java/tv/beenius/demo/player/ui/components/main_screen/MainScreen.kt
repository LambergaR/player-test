package tv.beenius.demo.player.ui.components.main_screen

import android.view.SurfaceHolder
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import tv.beenius.demo.player.ui.components.PlayerComponent
import tv.beenius.demo.player.ui.components.TextDialog
import tv.beenius.demo.player.ui.components.logger.LoggerComponent
import tv.beenius.demo.player.ui.components.logger.LoggingViewModel

@ExperimentalComposeUiApi
@Composable
fun MainScreen(
    loggingViewModel: LoggingViewModel,
    mainScreenViewModel: MainScreenViewModel,
    surfaceCallback: SurfaceHolder.Callback,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {

        var dialogOpened: Boolean by remember { mutableStateOf(false) }
        var logShown: Boolean by remember { mutableStateOf(mainScreenViewModel.loggerVisible) }

        mainScreenViewModel.onUrlAddDialogRequested {
            dialogOpened = true
        }

        mainScreenViewModel.onLoggerVisibilityChanged {
            logShown = it
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .background(Color.Gray)
                .focusable(false)
        ) {
            PlayerComponent(callback = surfaceCallback)
        }

        if(logShown) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .focusable(false)
            ) {
                LoggerComponent(
                    viewModel = loggingViewModel
                )
            }
        }

        if(dialogOpened) {
            TextDialog(
                modifier = Modifier,
                onDismiss = {
                    it?.let { text ->
                        mainScreenViewModel.notifyUrlAdded(text)
                    }
                    dialogOpened = false
                }
            )
        }
    }
}