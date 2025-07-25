package com.appsbase.jetcode.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "materials")
@TypeConverters(Converters::class)
data class MaterialEntity(
    @PrimaryKey val id: String,
    val type: String,
    val title: String,
    val content: String,
    val metadata: Map<String, String> = emptyMap(),
)
