package com.assignment.zenithra.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.assignment.zenithra.models.MangaEntity

@Dao
interface MangaDao {

    @Insert
    suspend fun insertManga(manga:MangaEntity)

    @Query("DELETE FROM MangaEntity")
    suspend fun deleteAll()

    @Query("SELECT * FROM MangaEntity")
    fun pagingSource(): PagingSource<Int, MangaEntity>

    @Query("Select * from MangaEntity order by id desc limit 1")
    suspend fun getLastManga():MangaEntity?

}