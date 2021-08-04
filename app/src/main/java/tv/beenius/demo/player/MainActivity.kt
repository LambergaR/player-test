package tv.beenius.demo.player

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import timber.log.Timber
import tv.beenius.demo.player.player.NativePlayerWrapper
import tv.beenius.demo.player.provider.assets.MediaSampleServiceImpl
import tv.beenius.demo.player.provider.stream.StreamServiceImpl
import tv.beenius.demo.player.ui.components.logger.LoggingViewModel
import tv.beenius.demo.player.ui.components.main_screen.MainScreen
import tv.beenius.demo.player.ui.components.main_screen.MainScreenViewModel
import tv.beenius.demo.player.ui.theme.PlayerTheme
import tv.beenius.demo.player.util.log

class MainActivity : ComponentActivity() {

    private val loggingViewModel = LoggingViewModel()
    private val mainScreenViewModel = MainScreenViewModel()

    private val streamService = StreamServiceImpl(
        mediaSampleService = MediaSampleServiceImpl()
    )

    private val player = NativePlayerWrapper(
        loggingViewModel = loggingViewModel,
        streamService = streamService
    )

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        "onCreate".log(loggingViewModel)

        streamService.initialize(applicationContext)
        player.initialize()

        mainScreenViewModel.onUrlAdded(player::setDataSource)

        setContent {
            PlayerTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(
                        loggingViewModel = loggingViewModel,
                        mainScreenViewModel = mainScreenViewModel,
                        surfaceCallback = player.provideSurfaceCallback()
                    )
                }
            }
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return when(keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                streamService.previous()
                updateDataSource()
                true
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                loggingViewModel.scrollUp()
                true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                streamService.next()
                updateDataSource()
                true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                loggingViewModel.scrollDown()
                true
            }
            KeyEvent.KEYCODE_PROG_RED -> {
                mainScreenViewModel.showUrlAddDialog()
                true
            }
            KeyEvent.KEYCODE_PROG_GREEN -> {
                mainScreenViewModel.toggleLogger()
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }

    private fun updateDataSource() {
        player.setDataSource(streamService.current().url)
    }

}