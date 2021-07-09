package tv.beenius.demo.player.provider.stream

interface StreamService {
    fun current(): VideoStream
    fun next(): VideoStream
    fun previous(): VideoStream
}