package com.appsbase.jetcode.core.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.appsbase.jetcode.core.database.dao.LearningDao
import com.appsbase.jetcode.core.database.dao.UserProgressDao
import com.appsbase.jetcode.core.database.entity.*

@Database(
    entities = [
        SkillEntity::class,
        TopicEntity::class,
        LessonEntity::class,
        MaterialEntity::class,
        PracticeEntity::class,
        UserProgressEntity::class,
        LessonProgressEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class JetCodeDatabase : RoomDatabase() {

    abstract fun learningDao(): LearningDao
    abstract fun userProgressDao(): UserProgressDao

    companion object {
        const val DATABASE_NAME = "jetcode_database"

        fun create(context: Context): JetCodeDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                JetCodeDatabase::class.java,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
        }
    }
}
