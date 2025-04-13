package com.assignment.zenithra.models

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.assignment.zenithra.db.MangaDatabase
import com.assignment.zenithra.network.MangaApiService
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class MangaRemoteMediator(
    private val mangaDb: MangaDatabase,
    private val mangaApi: MangaApiService
) : RemoteMediator<Int, MangaEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MangaEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }

            val mangaList = mangaApi.getMangaList(
                pageNo = loadKey
            )

            mangaDb.withTransaction {
                var currId=1
                if (loadType == LoadType.REFRESH) {
                    mangaDb.getMangaDao().deleteAll()
                }
                else {
                    currId=(mangaDb.getMangaDao().getLastManga()?.id?:0)+1
                }
                for(mangaItem in mangaList.`data`) {
                    mangaDb.getMangaDao().insertManga(MangaEntity(currId++, title = mangaItem.title, thumb = mangaItem.thumb,summary=mangaItem.summary))
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = mangaList.`data`.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

}
