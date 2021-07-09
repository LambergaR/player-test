package tv.beenius.demo.player

import android.os.Bundle
import android.view.KeyEvent
import android.view.SurfaceHolder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import timber.log.Timber
import tv.beenius.demo.player.player.NativePlayerWrapper
import tv.beenius.demo.player.provider.assets.MediaSampleServiceImpl
import tv.beenius.demo.player.provider.stream.StreamService
import tv.beenius.demo.player.provider.stream.StreamServiceImpl
import tv.beenius.demo.player.ui.components.LoggingViewModel
import tv.beenius.demo.player.ui.components.MainScreen
import tv.beenius.demo.player.ui.components.PlayerViewModel
import tv.beenius.demo.player.ui.theme.PlayerTheme
import tv.beenius.demo.player.util.LogMessage

class MainActivity : ComponentActivity() {

    private val player = NativePlayerWrapper()
    private lateinit var streamService: StreamService

    private val inputFocusRequester = FocusRequester()
    private val loggerFocusRequester = FocusRequester()

    private val loggingViewModel = LoggingViewModel()
    private val playerViewModel = PlayerViewModel()

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.i("onCreate")

        player.mediaPlayer.setOnErrorListener { _, what, extra ->
            Timber.e("error: $what $extra")
            loggingViewModel.addMessage(
                LogMessage.Builder()
                    .type(LogMessage.Type.ERROR)
                    .addLine("MediaPlayer - Error")
                    .addLine(" what: $what")
                    .addLine(" extra: $extra")
                    .build()
            )
            false
        }

        player.mediaPlayer.setOnPreparedListener {
            Timber.e("prepared")
            loggingViewModel.addMessage(
                LogMessage.Builder()
                    .addLine("MediaPlayer - Prepared")
                    .build()
            )
            player.mediaPlayer.start()
        }

        streamService = StreamServiceImpl(
            mediaSampleService = MediaSampleServiceImpl(),
            context = applicationContext
        )

        playerViewModel.onValueChange {
            setDataSource(it)
        }

        setContent {
            PlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(
                        loggingViewModel = loggingViewModel,
                        playerViewModel = playerViewModel,
                        surfaceCallback = callback,
                        inputFocusRequester = inputFocusRequester,
                        logFocusRequester = loggerFocusRequester
                    )
                }
            }
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return when(keyCode) {
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                streamService.previous()
                updateDataSource()
                true
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                streamService.next()
                updateDataSource()
                true
            }
            KeyEvent.KEYCODE_PROG_GREEN -> {
                inputFocusRequester.requestFocus()
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }

    private fun updateDataSource() {
        setDataSource(streamService.current().url)
    }

    private fun setDataSource(url: String) {
        try {
            player.mediaPlayer.apply {
                reset()

                setDataSource(url)
                loggingViewModel.addMessage(
                    LogMessage.Builder()
                        .addLine("setDataSource")
                        .addLine(url)
                        .build()
                )

                prepareAsync()
            }
        } catch (e: Exception) {
            loggingViewModel.addMessage(
                LogMessage.Builder()
                    .addLine("An error has occurred")
                    .addLine(e.localizedMessage ?: e.message ?: "unknown")
                    .type(LogMessage.Type.ERROR)
                    .build()
            )
        }
    }

    private val callback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            Timber.i("surfaceCreated")
            loggingViewModel.addMessage(LogMessage("surfaceCreated"))

            player.mediaPlayer.apply {
                setSurface(holder.surface)
                updateDataSource()
            }
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            Timber.i("surfaceChanged")
            loggingViewModel.addMessage(LogMessage("surfaceChanged"))

            player.mediaPlayer.apply {
                setSurface(holder.surface)
                updateDataSource()
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Timber.i("surfaceDestroyed")
            loggingViewModel.addMessage(LogMessage("surfaceDestroyed"))
            // todo
        }

    }
}