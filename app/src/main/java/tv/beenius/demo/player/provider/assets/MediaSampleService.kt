package tv.beenius.demo.player.provider.assets

import android.content.Context

interface MediaSampleService {
    fun read(context: Context): List<MediaSample>
}