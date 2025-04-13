package com.assignment.zenithra.di

import android.content.Context
import androidx.room.Room
import com.assignment.zenithra.db.UserDao
import com.assignment.zenithra.db.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UserModule {

    @Singleton
    @Provides
    fun providesUserdb(@ApplicationContext context: Context):UserDatabase {
        return Room.databaseBuilder(context,UserDatabase::class.java,"user_db").fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesUserDao(userDatabase: UserDatabase):UserDao {
        return userDatabase.getUserDao()
    }

}