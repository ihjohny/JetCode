package com.appsbase.jetcode.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "skills")
@TypeConverters(Converters::class)
data class SkillEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val iconUrl: String?,
    val difficulty: String,
    val estimatedDuration: Int,
    val topicIds: List<String> = emptyList(),
)