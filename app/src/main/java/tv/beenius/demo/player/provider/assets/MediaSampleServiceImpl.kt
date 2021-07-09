package tv.beenius.demo.player.provider.assets

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader

class MediaSampleServiceImpl : MediaSampleService {
//    private val sampleName = "media.exolist.json"
    private val sampleName = "media.json"

    override fun read(context: Context) = context
            .assets
            .open(sampleName)
            .bufferedReader()
            .use(BufferedReader::readText)
            .let {
                Json {
                    ignoreUnknownKeys = true
                }.decodeFromString<List<MediaSampleCategory>>(it)
            }
}