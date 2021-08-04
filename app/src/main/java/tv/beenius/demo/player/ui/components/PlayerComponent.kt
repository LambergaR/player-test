package tv.beenius.demo.player.ui.components

import android.view.SurfaceHolder
import android.widget.VideoView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel

@Composable
fun PlayerComponent(
    callback: SurfaceHolder.Callback
) {
    Column(
        modifier = Modifier
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { context ->
                VideoView(context).apply {
                    holder.addCallback(callback)
                }
            }
        )
    }
}