package me.jraynor.engine.registry.utils


data class FontData(val id: String, val name: String)

enum class Font {
    LIGHT, REGULAR, SEMIBOLD, BOLD, EXTRABOLD, ITALIC
}

enum class Pack(val path: String) {
    BLOCK("/blocks"),
    FONT("/fonts"),
    SHADER(""),
    TEXTURE("/textures"),
    UI("/ui"),
    MISC("/misc");

    fun getName(): String {
        return path.substring(1);
    }

    fun parse(packIn: String): Pack {
        values().iterator().forEach { pack ->
            if (pack.path.contentEquals(packIn))
                return pack
        }
        return MISC
    }
}