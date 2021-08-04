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
    var isLocked: Boolean = false
        private set

    fun initialize() {
        initializeErrorListener()
        initializeInfoListener()
        initializeListeners()
        initializeDrmInfoListener()
    }

    fun setDataSource(url: String) {
        if(isLocked) {
            "The player is locked. Press the blue button to force unlock.".log(
                loggingViewModel, LogMessage.Type.ERROR
            )
            return
        }

        lock()
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
                    "An application error has occurred",
                    e.localizedMessage ?: e.message ?: "unknown"
                )
                type = LogMessage.Type.ERROR
            }
            unlock()
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
                try {
                    setSurface(holder.surface)
                    setDataSource(streamService.current().url)
                } catch (e: Exception) {
                    log(loggingViewModel) {
                        type = LogMessage.Type.ERROR
                        lines(
                            "An application error has occurred",
                            e.localizedMessage ?: e.message ?: "unknown"
                        )
                    }
                }
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            "surfaceDestroyed".log(loggingViewModel)
        }
    }

    fun lock() {
        "locking the player".log(loggingViewModel)
        isLocked = true
    }

    fun unlock() {
        "unlocking the player".log(loggingViewModel)
        isLocked = false
    }

    private fun initializeErrorListener() {
        mediaPlayer.setOnErrorListener { _, what, extra ->
            Timber.e("error: $what $extra")

            log(loggingViewModel) {
                type = LogMessage.Type.ERROR
                lines(
                    "Error",
                    " what: $what",
                    " extra: $extra"
                )
            }

            unlock()

            false
        }
    }

    private fun initializeInfoListener() {
        mediaPlayer.setOnInfoListener { _, what, extra ->
            Timber.e("info: $what")

            log(loggingViewModel) {
                lines(
                    "Info:",
                    " what: $what",
                    " extra: $extra"
                )
            }
            false
        }
    }

    private fun initializeDrmInfoListener() {
        mediaPlayer.setOnDrmInfoListener { mp, drmInfo ->
            Timber.e("drm info: $drmInfo")

            log(loggingViewModel) {
                lines(
                    "Drm Info:",
                    " $drmInfo"
                )
            }
        }
    }

    private fun initializeListeners() {
        mediaPlayer.setOnPreparedListener {
            unlock()
            "prepared".log(loggingViewModel)
            mediaPlayer.start()
        }

        mediaPlayer.setOnCompletionListener {
            "complete".log(loggingViewModel)
        }

        mediaPlayer.setOnBufferingUpdateListener { _, percent ->
            "buffering $percent".log(loggingViewModel)
        }

        mediaPlayer.setOnVideoSizeChangedListener { _, width, height ->
            "video size changed $width $height".log(loggingViewModel)
        }
    }
}