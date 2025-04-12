package com.assignment.zenithra.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.assignment.zenithra.db.UserDao
import com.assignment.zenithra.repository.CurrentUserRepository
import com.assignment.zenithra.repository.CurrentUserRepositoryImpl
import com.assignment.zenithra.repository.UserRepository
import com.assignment.zenithra.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "name"
)
@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
     fun bindUserRepo(userDao: UserDao):UserRepository {
         return UserRepositoryImpl(userDao)
     }

    @Singleton
    @Provides
     fun bindCurrentUserRepo(dataStore:DataStore<Preferences> ):CurrentUserRepository {
         return CurrentUserRepositoryImpl(dataStore)
     }

    @Singleton
    @Provides
    fun providesDataStore(@ApplicationContext context: Context):DataStore<Preferences> {
        return context.dataStore
    }

}