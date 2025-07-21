package com.appsbase.jetcode.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "topics")
@TypeConverters(Converters::class)
data class TopicEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val materialIds: List<String> = emptyList(),
    val practiceSetId: String,
    val duration: Int,
)
