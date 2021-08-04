package tv.beenius.demo.player.player

import android.media.MediaPlayer
import android.view.SurfaceHolder
import timber.log.Timber
import tv.beenius.demo.player.provider.stream.StreamService
import tv.beenius.demo.player.ui.components.logger.LoggingViewModel
import tv.beenius.demo.player.util.LogMessage
import tv.beenius.demo.player.util.log

class NativePlayerWrapper(
    val mediaPlayer: MediaPlayer = MediaPlayer(),
    val loggingViewModel: LoggingViewModel,
    val streamService: StreamService
) {
    fun initialize() {
        initializeErrorListener()
        initializeOnPreparedListener()
    }

    fun setDataSource(url: String) {
        try {
            mediaPlayer.apply {
                reset()

                setDataSource(url)

                log(loggingViewModel) {
                    lines(
                        "setDataSource",
                        url
                    )
                }

                prepareAsync()
            }
        } catch (e: Exception) {
            log(loggingViewModel) {
                lines(
                    "An error has occured",
                    e.localizedMessage ?: e.message ?: "unknown"
                )
                type = LogMessage.Type.ERROR
            }
        }
    }

    fun provideSurfaceCallback() = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            "surfaceCreated".log(loggingViewModel)

            mediaPlayer.apply {
                setSurface(holder.surface)
            }
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            "surfaceChanged".log(loggingViewModel)

            mediaPlayer.apply {
                setSurface(holder.surface)
                setDataSource(streamService.current().url)
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            "surfaceDestroyed".log(loggingViewModel)
        }

    }

    private fun initializeErrorListener() {
        mediaPlayer.setOnErrorListener { _, what, extra ->
            Timber.e("error: $what $extra")

            log(loggingViewModel) {
                type = LogMessage.Type.ERROR
                lines(
                    "MediaPlayer - Error",
                    " what: $what",
                    " extra: $extra"
                )
            }

            false
        }
    }

    private fun initializeOnPreparedListener() {
        mediaPlayer.setOnPreparedListener {
            Timber.e("prepared")
            log(loggingViewModel, "MediaPlayer - Prepared")
            mediaPlayer.start()
        }
    }
}