package tv.beenius.demo.player.provider.stream

import android.content.Context
import tv.beenius.demo.player.provider.assets.MediaSample
import tv.beenius.demo.player.provider.assets.MediaSampleCategory
import tv.beenius.demo.player.provider.assets.MediaSampleService

class StreamServiceImpl(
    mediaSampleService: MediaSampleService,
    context: Context
) : StreamService {

    private var categoryIndex = 0
    private var sampleIndex = 0
    private val samples = mediaSampleService.read(context)

    override fun current() = samples[categoryIndex].let { category ->
        map(category, category.samples[sampleIndex])
    }

    override fun next() = get(up = true)
    override fun previous() = get(up = false)

    private fun map(category: MediaSampleCategory, sample: MediaSample) = VideoStream(
        title = "${category.name} - ${sample.name}",
        url = sample.uri
    )

    private fun get(up: Boolean): VideoStream {

        if(up) {
            // get next
            if(sampleIndex == samples[categoryIndex].samples.size - 1) {
                // roll category
                // select first element in category
                categoryIndex = categoryIndex.roll(
                    up = true,
                    max = samples.maxIndex()
                )
                sampleIndex = 0
            } else {
                sampleIndex = sampleIndex.roll(
                    up = true,
                    max = samples[categoryIndex].samples.maxIndex()
                )
            }
        } else {
            if(sampleIndex == 0) {
                // roll category
                // select last index in category
                categoryIndex = categoryIndex.roll(
                    up = false,
                    max = samples.maxIndex()
                )
                sampleIndex = samples[categoryIndex].samples.maxIndex()
            } else {
                sampleIndex = sampleIndex.roll(
                    up = false,
                    max = samples[categoryIndex].samples.maxIndex()
                )
            }
        }

        return samples[categoryIndex].let { category ->
            map(category, category.samples[sampleIndex])
        }
    }

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