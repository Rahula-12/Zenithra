package com.assignment.zenithra.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.assignment.zenithra.models.MangaEntity

@Database(entities = [MangaEntity::class], version = 2, exportSchema = false)
abstract class MangaDatabase:RoomDatabase() {

    abstract fun getMangaDao():MangaDao

}