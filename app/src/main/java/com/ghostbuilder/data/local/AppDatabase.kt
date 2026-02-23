package com.ghostbuilder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ghostbuilder.data.local.converter.ProjectStageConverter
import com.ghostbuilder.data.local.dao.ProjectDao
import com.ghostbuilder.data.local.model.ProjectEntity

@Database(entities = [ProjectEntity::class], version = 1)
@TypeConverters(ProjectStageConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao

    companion object {
        const val DATABASE_NAME = "ghostbuilder_db"
    }
}
