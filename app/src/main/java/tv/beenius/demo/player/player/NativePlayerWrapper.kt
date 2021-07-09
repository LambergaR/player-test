package tv.beenius.demo.player.player

import android.media.MediaPlayer
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*
import tv.beenius.demo.player.util.LogMessage

class NativePlayerWrapper(
    val mediaPlayer: MediaPlayer = MediaPlayer()
) {
//    fun setupFlow() {
//        val logMessageFlow = flow {
//
//        }
//    }


    private val logMessages = ArrayList<LogMessage>()

    private fun postLogMessage(message: LogMessage) {
        logMessages.add(message)
    }



}