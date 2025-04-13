package com.assignment.zenithra.screens.manga

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import com.assignment.zenithra.models.MangaEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(private val pager: Pager<Int, MangaEntity>):ViewModel() {

    val mangaEntityFlow = pager.flow.cachedIn(viewModelScope)

}