package tv.beenius.demo.player.util

import java.time.Instant

data class LogMessage(
    val lines: List<String>,
    val instant: Instant,
    val type: Type = Type.LOG
) {
    constructor(value: String) : this(
        lines = listOf(value),
        instant = Instant.now(),
        type = Type.LOG
    )

    enum class Type {
        LOG, ERROR
    }

    class Builder {
        private val lines = ArrayList<String>()
        private val instant = Instant.now()

        private var type = Type.LOG

        fun addLine(l: String): Builder {
            lines.add(l)
            return this
        }

        fun type(t: Type): Builder {
            type = t
            return this
        }

        fun build() = LogMessage(
            instant = instant,
            lines = lines,
            type = type
        )
    }
}