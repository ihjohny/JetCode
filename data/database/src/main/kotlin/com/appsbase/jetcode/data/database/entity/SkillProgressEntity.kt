package com.appsbase.jetcode.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "skill_progress")
@TypeConverters(Converters::class)
data class SkillProgressEntity(
    @PrimaryKey val id: String, // User ID + Skill ID
    val userId: String,
    val skillId: String,
    val completedMaterial: Int,
    val totalMaterial: Int,
    val updatedAt: Long,
)
