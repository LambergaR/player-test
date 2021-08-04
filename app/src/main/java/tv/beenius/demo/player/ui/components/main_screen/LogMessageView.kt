package tv.beenius.demo.player.ui.components.main_screen

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tv.beenius.demo.player.util.LogMessage
import java.time.format.DateTimeFormatter

@Composable
fun LogMessageView(
    message: LogMessage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(
                top = 2.dp, bottom = 2.dp
            )
            .focusable(enabled = false)
    ) {
        val textStyle = if(message.type == LogMessage.Type.ERROR) {
            MaterialTheme.typography.subtitle2.copy(color = Color.Red)
        } else {
            MaterialTheme.typography.subtitle2
        }

        Text(
            modifier = Modifier,
            text = DateTimeFormatter.ISO_INSTANT.format(message.instant),
            style = textStyle,
        )

        for(line in message.lines) {
            Text(
                text = line,
                modifier = Modifier
                    .padding(start = 10.dp),
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}