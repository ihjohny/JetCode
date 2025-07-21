package com.appsbase.jetcode.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appsbase.jetcode.core.database.entity.SkillEntity
import com.appsbase.jetcode.core.database.entity.TopicEntity
import com.appsbase.jetcode.core.database.entity.MaterialEntity
import com.appsbase.jetcode.core.database.entity.PracticeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LearningDao {

    @Query("SELECT * FROM skills ORDER BY name ASC")
    fun getAllSkills(): Flow<List<SkillEntity>>

    @Query("SELECT * FROM skills WHERE id = :skillId")
    fun getSkillById(skillId: String): Flow<SkillEntity?>

    @Query("SELECT * FROM topics WHERE id = :topicId")
    fun getTopicById(topicId: String): Flow<TopicEntity?>

    @Query("SELECT * FROM topics WHERE id IN (:topicIds)")
    fun getTopicsByIds(topicIds: List<String>): Flow<List<TopicEntity>>

    @Query("SELECT * FROM materials WHERE id IN (:materialIds)")
    fun getMaterialsByIds(materialIds: List<String>): Flow<List<MaterialEntity>>

    @Query("SELECT * FROM practices WHERE id IN (:practiceIds)")
    fun getPracticesByIds(practiceIds: List<String>): Flow<List<PracticeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkills(skills: List<SkillEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopics(topics: List<TopicEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterials(materials: List<MaterialEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPractices(practices: List<PracticeEntity>)

    @Query("DELETE FROM skills")
    suspend fun clearSkills()

    @Query("DELETE FROM topics")
    suspend fun clearTopics()

    @Query("DELETE FROM materials")
    suspend fun clearMaterials()

    @Query("DELETE FROM practices")
    suspend fun clearPractices()

    @Query(
        """
        SELECT * FROM skills 
        WHERE name LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%'
    """
    )
    fun searchSkills(query: String): Flow<List<SkillEntity>>

    @Query(
        """
        SELECT * FROM topics 
        WHERE name LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%'
    """
    )
    fun searchTopics(query: String): Flow<List<TopicEntity>>

    @Query(
        """
        SELECT * FROM materials 
        WHERE title LIKE '%' || :query || '%' 
        OR content LIKE '%' || :query || '%'
    """
    )
    fun searchMaterials(query: String): Flow<List<MaterialEntity>>

    @Query(
        """
        SELECT * FROM practices 
        WHERE question LIKE '%' || :query || '%' 
        OR explanation LIKE '%' || :query || '%'
    """
    )
    fun searchPractices(query: String): Flow<List<PracticeEntity>>
}
