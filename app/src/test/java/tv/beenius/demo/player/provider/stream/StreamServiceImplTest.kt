package tv.beenius.demo.player.provider.stream

import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Test
import tv.beenius.demo.player.provider.assets.MediaSample
import tv.beenius.demo.player.provider.assets.MediaSampleCategory
import tv.beenius.demo.player.provider.assets.MediaSampleService

class StreamServiceImplTest : TestCase() {

    private val mediaSampleService = mockk<MediaSampleService> {
        every { read(any()) } returns List(10) { catIndex ->
            MediaSampleCategory(
                name = "Media Sample Category $catIndex",
                samples = List(20) { sampleIndex ->
                    MediaSample(
                        name = "Media Sample $sampleIndex",
                        uri = "https://sample.media/$sampleIndex"
                    )
                }
            )
        }
    }

    @Test
    fun `test next sample overflow`() {
        val streamService: StreamService = StreamServiceImpl(
            mediaSampleService = mediaSampleService,
            context = mockk()
        )

        streamService.next().let {
            assertEquals(
                "Media Sample Category 0 - Media Sample 1",
                it.title
            )

            assertEquals(
                "https://sample.media/1",
                it.url
            )
        }

        repeat(20) {
            streamService.next()
        }

        streamService.next().let {
            assertEquals(
                "Media Sample Category 1 - Media Sample 2",
                it.title
            )

            assertEquals(
                "https://sample.media/2",
                it.url
            )
        }
    }

    @Test
    fun `test next category overflow`() {
        val streamService: StreamService = StreamServiceImpl(
            mediaSampleService = mediaSampleService,
            context = mockk()
        )

        repeat((13 * 20) + 2) {
            streamService.next()
        }

        assertEquals(
            "Media Sample Category 3 - Media Sample 2",
            streamService.current().title
        )

        assertEquals(
            "https://sample.media/2",
            streamService.current().url
        )
    }

    @Test
    fun `test previous sample overflow`() {
        val streamService: StreamService = StreamServiceImpl(
            mediaSampleService = mediaSampleService,
            context = mockk()
        )

        streamService.previous().let {
            assertEquals(
                "Media Sample Category 9 - Media Sample 19",
                it.title
            )

            assertEquals(
                "https://sample.media/19",
                it.url
            )
        }
    }

    @Test
    fun `test previous category overflow`() {
        val streamService: StreamService = StreamServiceImpl(
            mediaSampleService = mediaSampleService,
            context = mockk()
        )

        repeat((13 * 20) + 3) {
            streamService.previous()
        }

        assertEquals(
            "Media Sample Category 6 - Media Sample 17",
            streamService.current().title
        )

        assertEquals(
            "https://sample.media/17",
            streamService.current().url
        )
    }

    @Test
    fun `test previous sample overflow on a specific sample config`() {
        val sampleService = mockk<MediaSampleService> {
            every { read(any()) } returns listOf(
                MediaSampleCategory(
                    name = "Custom",
                    samples = listOf(
                        MediaSample(
                            name = "Big Buck Bunny",
                            uri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                        ),
                        MediaSample(
                            name = "Blender Movie - 2016",
                            uri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
                        )
                    )
                ),
                MediaSampleCategory(
                    name = "Clear DASH",
                    samples = listOf(
                        MediaSample(
                            name = "HD (MP4, H264)",
                            uri = "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd"
                        ),
                        MediaSample(
                            name = "UHD (MP4, H264)",
                            uri = "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears_uhd.mpd"
                        ),
                        MediaSample(
                            name = "HD (MP4, H265)",
                            uri = "https://storage.googleapis.com/wvmedia/clear/hevc/tears/tears.mpd"
                        ),
                        MediaSample(
                            name = "UHD (MP4, H265)",
                            uri = "https://storage.googleapis.com/wvmedia/clear/hevc/tears/tears_uhd.mpd"
                        ),
                        MediaSample(
                            name = "HD (WebM, VP9)",
                            uri = "https://storage.googleapis.com/wvmedia/clear/vp9/tears/tears.mpd"
                        ),
                        MediaSample(
                            name = "UHD (WebM, VP9)",
                            uri = "https://storage.googleapis.com/wvmedia/clear/vp9/tears/tears_uhd.mpd"
                        )
                    )
                )
            )
        }

        val streamService: StreamService = StreamServiceImpl(
            mediaSampleService = sampleService,
            context = mockk()
        )

        streamService.current().let {
            assertEquals(
                "Custom - Big Buck Bunny",
                it.title
            )
            assertEquals(
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                it.url
            )
        }

        Pair(
            "Clear DASH - UHD (WebM, VP9)",
            "https://storage.googleapis.com/wvmedia/clear/vp9/tears/tears_uhd.mpd"
        ).let { last ->
            streamService.previous().let {
                assertEquals(last.first, it.title)
                assertEquals(last.second, it.url)
            }
            streamService.current().let {
                assertEquals(last.first, it.title)
                assertEquals(last.second, it.url)
            }
        }

    }
}