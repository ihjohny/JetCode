package com.appsbase.jetcode.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.appsbase.jetcode.core.database.entity.UserProgressEntity
import com.appsbase.jetcode.core.database.entity.LessonProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {

    @Query("SELECT * FROM user_progress WHERE userId = :userId")
    fun getUserProgress(userId: String): Flow<UserProgressEntity?>

    @Query("SELECT * FROM lesson_progress WHERE userId = :userId")
    fun getUserLessonProgresses(userId: String): Flow<List<LessonProgressEntity>>

    @Query("SELECT * FROM lesson_progress WHERE userId = :userId AND lessonId = :lessonId")
    fun getLessonProgress(userId: String, lessonId: String): Flow<LessonProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProgress(userProgress: UserProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessonProgress(lessonProgress: LessonProgressEntity)

    @Update
    suspend fun updateUserProgress(userProgress: UserProgressEntity)

    @Update
    suspend fun updateLessonProgress(lessonProgress: LessonProgressEntity)

    @Query("DELETE FROM user_progress WHERE userId = :userId")
    suspend fun clearUserProgress(userId: String)

    @Query("DELETE FROM lesson_progress WHERE userId = :userId")
    suspend fun clearUserLessonProgresses(userId: String)

    @Query(
        """
        SELECT COUNT(*) FROM lesson_progress 
        WHERE userId = :userId AND isCompleted = 1
    """
    )
    fun getCompletedLessonsCount(userId: String): Flow<Int>

    @Query(
        """
        SELECT SUM(score) FROM lesson_progress 
        WHERE userId = :userId AND isCompleted = 1
    """
    )
    fun getTotalScore(userId: String): Flow<Int>
}
