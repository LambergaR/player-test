package tv.beenius.demo.player.ui.components

import android.view.SurfaceHolder
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@ExperimentalComposeUiApi
@Composable
fun MainScreen(
    loggingViewModel: LoggingViewModel,
    playerViewModel: PlayerViewModel,
    surfaceCallback: SurfaceHolder.Callback,
    inputFocusRequester: FocusRequester,
    logFocusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .border(
                    width = 4.dp,
                    color = Color.Green
                )
        ) {
            var inputValue by remember {
                mutableStateOf("")
            }

            val keyboardController = LocalSoftwareKeyboardController.current

            TextField(
                value = inputValue,
                onValueChange = {
                    inputValue = it
                },
                modifier = Modifier
                    .focusRequester(inputFocusRequester)
                    .fillMaxWidth(),
                keyboardActions = KeyboardActions(
                    onSend = {
                        keyboardController?.hide()
                        inputFocusRequester.freeFocus()
                        playerViewModel.changeUrl(inputValue)
                        logFocusRequester.requestFocus()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send,
                    keyboardType = KeyboardType.Uri
                )
            )
        }

        Row(
            modifier = Modifier
                .weight(1f)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                PlayerComponent(callback = surfaceCallback)
            }
            Column(modifier = Modifier.weight(1f)) {
                LoggerComponent(
                    viewModel = loggingViewModel,
                    modifier = Modifier
                        .focusRequester(logFocusRequester)
                )
            }
        }
    }
}