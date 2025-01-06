package com.example.managementapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.managementapp.data.model.entity.ProjectEntity

@Database(entities = [ProjectEntity::class], version = 1)
abstract class ProjectDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
}