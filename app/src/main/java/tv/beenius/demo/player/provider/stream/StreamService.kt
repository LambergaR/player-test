package tv.beenius.demo.player.provider.stream

import android.content.Context

interface StreamService {
    fun initialize(context: Context)
    fun current(): VideoStream
    fun next(): VideoStream
    fun previous(): VideoStream
}