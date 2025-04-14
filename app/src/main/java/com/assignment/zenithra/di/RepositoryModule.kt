package com.assignment.zenithra.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.paging.Pager
import com.assignment.zenithra.db.UserDao
import com.assignment.zenithra.models.MangaEntity
import com.assignment.zenithra.repository.CurrentUserRepository
import com.assignment.zenithra.repository.CurrentUserRepositoryImpl
import com.assignment.zenithra.repository.MangaRepository
import com.assignment.zenithra.repository.MangaRepositoryImpl
import com.assignment.zenithra.repository.UserRepository
import com.assignment.zenithra.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "name"
)
@InstallIn(ViewModelComponent::class)
@Module
object RepositoryModule {

    @Provides
     fun bindUserRepo(userDao: UserDao):UserRepository {
         return UserRepositoryImpl(userDao)
     }

    @Provides
     fun bindCurrentUserRepo(dataStore:DataStore<Preferences> ):CurrentUserRepository {
         return CurrentUserRepositoryImpl(dataStore)
     }

    @Provides
    fun providesDataStore(@ApplicationContext context: Context):DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    fun providesMangaRepo(pager: Pager<Int, MangaEntity>):MangaRepository {
        return MangaRepositoryImpl(pager)
    }

}