package com.appsbase.jetcode.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appsbase.jetcode.core.database.entity.PracticeSetEntity
import com.appsbase.jetcode.core.database.entity.QuizEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PracticeDao {
    @Query("SELECT * FROM practice_sets ORDER BY name ASC")
    fun getAllPracticeSets(): Flow<List<PracticeSetEntity>>

    @Query("SELECT * FROM practice_sets WHERE id = :practiceSetId")
    fun getPracticeSetById(practiceSetId: String): Flow<PracticeSetEntity?>

    @Query("SELECT * FROM quizzes WHERE id IN (:quizIds)")
    fun getQuizzesByIds(quizIds: List<String>): Flow<List<QuizEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPracticeSets(practiceSets: List<PracticeSetEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizzes(quizzes: List<QuizEntity>)

    @Query("DELETE FROM quizzes")
    suspend fun clearQuizzes()

    @Query("DELETE FROM practice_sets")
    suspend fun clearPracticeSets()

    @Query(
        """
        SELECT * FROM practice_sets 
        WHERE name LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%'
        ORDER BY name ASC
    """
    )
    fun searchPracticeSets(query: String): Flow<List<PracticeSetEntity>>
}
