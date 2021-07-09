package tv.beenius.demo.player.ui.components

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import tv.beenius.demo.player.util.LogMessage
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

@Composable
fun LoggerComponent(
    viewModel: LoggingViewModel,
    modifier: Modifier = Modifier
) {
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier
            .focusable(enabled = true),
        state = state,
    ) {
        items(items = viewModel.messages) { message ->
            LogMessageView(
                message = message,
                modifier = modifier
            )
        }
    }

    DisposableEffect(viewModel.messages.size) {

        scope.launch {
            state.animateScrollToItem(viewModel.messages.size - 1)
        }

        onDispose {  }
    }
}

@Composable
fun LogMessageView(
    message: LogMessage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            top = 2.dp, bottom = 2.dp
        )
    ) {
        val textStyle = if(message.type == LogMessage.Type.ERROR) {
            MaterialTheme.typography.subtitle2.copy(color = Color.Red)
        } else {
            MaterialTheme.typography.subtitle2
        }

        Text(
            text = DateTimeFormatter.ISO_INSTANT.format(message.instant),
            style = textStyle,
        )

        for(line in message.lines) {
            Text(
                text = line,
                modifier = Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}

class LoggingViewModel: ViewModel() {
    var messages: List<LogMessage> by mutableStateOf(listOf())
        private set

    fun addMessage(message: LogMessage) {
        messages = messages + listOf(message)
    }
}



