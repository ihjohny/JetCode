package com.appsbase.jetcode.data.repository.mapper

import com.appsbase.jetcode.data.preferences.entity.ThemeModeEntity
import com.appsbase.jetcode.domain.model.ThemeMode

/**
 * Mappers to convert between preferences entities and domain models
 */

fun ThemeModeEntity.toDomain(): ThemeMode = when (this) {
    ThemeModeEntity.LIGHT -> ThemeMode.LIGHT
    ThemeModeEntity.DARK -> ThemeMode.DARK
    ThemeModeEntity.SYSTEM -> ThemeMode.SYSTEM
}

fun ThemeMode.toEntity(): ThemeModeEntity = when (this) {
    ThemeMode.LIGHT -> ThemeModeEntity.LIGHT
    ThemeMode.DARK -> ThemeModeEntity.DARK
    ThemeMode.SYSTEM -> ThemeModeEntity.SYSTEM
}
