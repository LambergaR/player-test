package tv.beenius.demo.player.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog

@ExperimentalComposeUiApi
@Composable
fun TextDialog(
    modifier: Modifier = Modifier,
    onDismiss: (String?) -> Unit,
) {
    var text: String? by remember { mutableStateOf(null) }

    Dialog(
        onDismissRequest = {
            onDismiss(text)
        }
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface
        ) {
            Column {
                TextField(
                    modifier = modifier,
                    singleLine = true,
                    keyboardActions = KeyboardActions(
                        onSend = {
                            onDismiss(text)
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Send,
                        keyboardType = KeyboardType.Uri
                    ),
                    value = text ?: "",
                    onValueChange = {
                        text = it
                    }
                )
            }
        }
    }
}