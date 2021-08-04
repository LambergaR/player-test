package tv.beenius.demo.player.provider.assets

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader

class MediaSampleServiceImpl : MediaSampleService {
    private val sampleName = "media.json"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun read(context: Context) = context
            .assets
            .open(sampleName)
            .bufferedReader()
            .use(BufferedReader::readText)
            .let {
                json.decodeFromString<List<MediaSample>>(it)
            }
}