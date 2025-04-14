package com.assignment.zenithra.repository

import androidx.paging.Pager
import com.assignment.zenithra.models.MangaEntity
import javax.inject.Inject

interface MangaRepository {
    val pager: Pager<Int, MangaEntity>
}

class MangaRepositoryImpl @Inject constructor(override val pager: Pager<Int,MangaEntity>):MangaRepository