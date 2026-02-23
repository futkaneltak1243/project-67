package com.ghostbuilder.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ghostbuilder.data.Project
import com.ghostbuilder.data.ProjectDao

@Database(entities = [Project::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
}
