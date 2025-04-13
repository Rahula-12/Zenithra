package com.assignment.zenithra.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.assignment.zenithra.db.MangaDao
import com.assignment.zenithra.db.MangaDatabase
import com.assignment.zenithra.models.MangaEntity
import com.assignment.zenithra.models.MangaRemoteMediator
import com.assignment.zenithra.network.MangaApiService
import com.assignment.zenithra.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@InstallIn(SingletonComponent::class)
@Module
object MangaModule {

    @Singleton
    @Provides
    fun providesMangaDb(@ApplicationContext context: Context):MangaDatabase {
        return Room.databaseBuilder(context,MangaDatabase::class.java,"manga_db").fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesMangaDao(mangadb:MangaDatabase):MangaDao {
        return mangadb.getMangaDao()
    }

    @Singleton
    @Provides
    fun providesMangaApiService():MangaApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MangaApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesMangaPager(
        mangadb:MangaDatabase,
        mangaApiService: MangaApiService
    ): Pager<Int, MangaEntity> {
        return Pager(
            config = PagingConfig(pageSize = 12),
            remoteMediator = MangaRemoteMediator(
                mangadb,
                mangaApiService
            ),
            pagingSourceFactory = {
                mangadb.getMangaDao().pagingSource()
            }
        )
    }

}