package com.assignment.zenithra.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.assignment.zenithra.models.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase:RoomDatabase() {

    abstract fun getUserDao():UserDao

}