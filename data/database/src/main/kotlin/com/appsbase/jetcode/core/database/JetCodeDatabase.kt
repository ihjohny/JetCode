package com.appsbase.jetcode.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.appsbase.jetcode.core.database.dao.LearningDao
import com.appsbase.jetcode.core.database.entity.Converters
import com.appsbase.jetcode.core.database.entity.MaterialEntity
import com.appsbase.jetcode.core.database.entity.PracticeEntity
import com.appsbase.jetcode.core.database.entity.SkillEntity
import com.appsbase.jetcode.core.database.entity.TopicEntity

@Database(
    entities = [
        SkillEntity::class,
        TopicEntity::class,
        MaterialEntity::class,
        PracticeEntity::class,
    ],
    version = 2, // Increment version due to schema changes
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class JetCodeDatabase : RoomDatabase() {

    abstract fun learningDao(): LearningDao

    companion object {
        const val DATABASE_NAME = "jetcode_database"

        fun create(context: Context): JetCodeDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                JetCodeDatabase::class.java,
                DATABASE_NAME,
            ).fallbackToDestructiveMigration().build()
        }
    }
}
