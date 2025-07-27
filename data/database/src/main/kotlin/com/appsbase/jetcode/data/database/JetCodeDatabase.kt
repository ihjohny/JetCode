package com.appsbase.jetcode.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.appsbase.jetcode.data.database.dao.LearningDao
import com.appsbase.jetcode.data.database.dao.PracticeDao
import com.appsbase.jetcode.data.database.dao.ProgressDao
import com.appsbase.jetcode.data.database.entity.Converters
import com.appsbase.jetcode.data.database.entity.MaterialEntity
import com.appsbase.jetcode.data.database.entity.PracticeSetEntity
import com.appsbase.jetcode.data.database.entity.QuizEntity
import com.appsbase.jetcode.data.database.entity.SkillEntity
import com.appsbase.jetcode.data.database.entity.SkillProgressEntity
import com.appsbase.jetcode.data.database.entity.TopicEntity
import com.appsbase.jetcode.data.database.entity.TopicProgressEntity

@Database(
    entities = [
        SkillEntity::class,
        TopicEntity::class,
        MaterialEntity::class,
        PracticeSetEntity::class,
        QuizEntity::class,
        TopicProgressEntity::class,
        SkillProgressEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class JetCodeDatabase : RoomDatabase() {

    abstract fun learningDao(): LearningDao
    abstract fun practiceDao(): PracticeDao
    abstract fun progressDao(): ProgressDao

    companion object {
        const val DATABASE_NAME = "jetcode_database"

        fun create(context: Context): JetCodeDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                JetCodeDatabase::class.java,
                DATABASE_NAME,
            ).fallbackToDestructiveMigration(false).build()
        }
    }
}
