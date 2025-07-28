package com.appsbase.jetcode.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appsbase.jetcode.data.database.entity.PracticeResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PracticeResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPracticeResult(result: PracticeResultEntity)

    @Query("SELECT * FROM practice_results WHERE practiceSetId = :practiceSetId AND userId = :userId LIMIT 1")
    fun getPracticeResultById(
        practiceSetId: String,
        userId: String,
    ): Flow<PracticeResultEntity?>

    @Query("SELECT * FROM practice_results WHERE userId = :userId ORDER BY updatedAt DESC")
    fun getAllPracticeResults(
        userId: String,
    ): Flow<List<PracticeResultEntity>>

    @Query("SELECT * FROM practice_results WHERE practiceSetId IN (:practiceSetIds) AND userId = :userId")
    fun getPracticeResultsByIds(
        practiceSetIds: List<String>,
        userId: String,
    ): Flow<List<PracticeResultEntity>>

    @Query("DELETE FROM practice_results WHERE practiceSetId = :practiceSetId AND userId = :userId")
    suspend fun deletePracticeResult(
        practiceSetId: String,
        userId: String,
    )

    @Query("DELETE FROM practice_results WHERE userId = :userId")
    suspend fun deleteAllUserPracticeResults(userId: String)
}
