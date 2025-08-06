package com.appsbase.jetcode.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ColorPalette {
    BLUE_THEME,
    PURPLE_THEME,
    GREEN_THEME,
    ORANGE_THEME,
    PINK_THEME,
    TEAL_THEME,
    INDIGO_THEME,
    CUSTOM
}

@Serializable
enum class FontFamily {
    DEFAULT,
    ROBOTO,
    OPEN_SANS,
    LATO,
    INTER,
    POPPINS,
    NUNITO,
    MONTSERRAT
}

@Serializable
data class AppThemeSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val colorPalette: ColorPalette = ColorPalette.BLUE_THEME,
    val fontFamily: FontFamily = FontFamily.DEFAULT
)
