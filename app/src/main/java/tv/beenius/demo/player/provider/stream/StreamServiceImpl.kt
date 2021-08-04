package tv.beenius.demo.player.provider.stream

import android.content.Context
import tv.beenius.demo.player.provider.assets.MediaSample
import tv.beenius.demo.player.provider.assets.MediaSampleService

class StreamServiceImpl(
    private val mediaSampleService: MediaSampleService
) : StreamService {

    private var sampleIndex = 0
    lateinit var samples: List<MediaSample>

    override fun initialize(context: Context) {
        samples = mediaSampleService.read(context)
    }

    override fun current() = samples[sampleIndex].toStream()

    override fun next() = samples[
            sampleIndex.roll(
                up = true,
                max = samples.maxIndex()
            ).also {
                sampleIndex = it
            }
    ].toStream()

    override fun previous() = samples[
            sampleIndex.roll(
                up = false,
                max = samples.maxIndex()
            ).also {
                sampleIndex = it
            }
    ].toStream()

    private fun MediaSample.toStream() = VideoStream(
        title = name,
        url = uri
    )

    private fun Int.roll(up: Boolean = true, min: Int = 0, max: Int) =
        if(up) {
            if(this >= max) {
                min
            } else this + 1
        } else {
            if(this <= min) {
                max
            } else this - 1
        }

    private fun List<*>.maxIndex() = this.size - 1
}