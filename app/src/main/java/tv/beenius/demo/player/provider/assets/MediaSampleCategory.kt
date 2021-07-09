package tv.beenius.demo.player.provider.assets

import kotlinx.serialization.Serializable

@Serializable
data class MediaSampleCategory(
    val name: String,
    val samples: List<MediaSample>
)
