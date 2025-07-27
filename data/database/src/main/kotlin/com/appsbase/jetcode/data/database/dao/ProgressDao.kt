package com.appsbase.jetcode.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appsbase.jetcode.data.database.entity.TopicProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: TopicProgressEntity)

    @Query("SELECT * FROM topic_progress WHERE topicId = :topicId AND userId = :userId LIMIT 1")
    fun getTopicProgressById(
        topicId: String,
        userId: String,
    ): Flow<TopicProgressEntity?>

    @Query("SELECT * FROM topic_progress WHERE topicId IN (:topicIds) AND userId = :userId")
    fun getTopicsProgressByIds(
        topicIds: List<String>,
        userId: String,
    ): Flow<List<TopicProgressEntity>>
}