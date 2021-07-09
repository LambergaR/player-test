package tv.beenius.demo.player.provider.assets

import kotlinx.serialization.Serializable

@Serializable
data class MediaSample(
    val name: String,
    val uri: String
)