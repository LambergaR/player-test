package tv.beenius.demo.player.ui.components.logger

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import tv.beenius.demo.player.ui.components.main_screen.LogMessageView

@Composable
fun LoggerComponent(
    viewModel: LoggingViewModel,
    modifier: Modifier = Modifier
) {
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()

    viewModel.listState = state

    val listModifier = modifier
        .fillMaxSize()
        .padding(
            start = 6.dp,
            end = 6.dp,
            top = 4.dp,
            bottom = 4.dp
        )

    LazyColumn(
        modifier = listModifier,
        state = state
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







