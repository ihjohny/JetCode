package com.appsbase.jetcode.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.appsbase.jetcode.core.database.entity.SkillEntity
import com.appsbase.jetcode.core.database.entity.TopicEntity
import com.appsbase.jetcode.core.database.entity.LessonEntity
import com.appsbase.jetcode.core.database.entity.MaterialEntity
import com.appsbase.jetcode.core.database.entity.PracticeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LearningDao {

    @Query("SELECT * FROM skills ORDER BY name ASC")
    fun getAllSkills(): Flow<List<SkillEntity>>

    @Query("SELECT * FROM skills WHERE id = :skillId")
    fun getSkillById(skillId: String): Flow<SkillEntity?>

    @Query("SELECT * FROM topics WHERE skillId = :skillId ORDER BY `order` ASC")
    fun getTopicsForSkill(skillId: String): Flow<List<TopicEntity>>

    @Query("SELECT * FROM lessons WHERE topicId = :topicId ORDER BY `order` ASC")
    fun getLessonsForTopic(topicId: String): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE id = :lessonId")
    fun getLessonById(lessonId: String): Flow<LessonEntity?>

    @Query("SELECT * FROM materials WHERE lessonId = :lessonId ORDER BY `order` ASC")
    fun getMaterialsForLesson(lessonId: String): Flow<List<MaterialEntity>>

    @Query("SELECT * FROM practices WHERE lessonId = :lessonId")
    fun getPracticesForLesson(lessonId: String): Flow<List<PracticeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkills(skills: List<SkillEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopics(topics: List<TopicEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterials(materials: List<MaterialEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPractices(practices: List<PracticeEntity>)

    @Query("DELETE FROM skills")
    suspend fun clearSkills()

    @Query("DELETE FROM topics")
    suspend fun clearTopics()

    @Query("DELETE FROM lessons")
    suspend fun clearLessons()

    @Query("DELETE FROM materials")
    suspend fun clearMaterials()

    @Query("DELETE FROM practices")
    suspend fun clearPractices()

    @Query("""
        SELECT * FROM skills 
        WHERE name LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%'
    """)
    fun searchSkills(query: String): Flow<List<SkillEntity>>

    @Query("""
        SELECT * FROM topics 
        WHERE name LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%'
    """)
    fun searchTopics(query: String): Flow<List<TopicEntity>>

    @Query("""
        SELECT * FROM lessons 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%'
    """)
    fun searchLessons(query: String): Flow<List<LessonEntity>>
}
