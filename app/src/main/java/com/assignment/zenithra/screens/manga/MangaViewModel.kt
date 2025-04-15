package com.assignment.zenithra.screens.manga

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import com.assignment.zenithra.models.MangaEntity
import com.assignment.zenithra.repository.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(private val mangaRepository: MangaRepository):ViewModel() {

    val mangaEntityFlow = mangaRepository.pager.flow.cachedIn(viewModelScope)

    fun removeCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            mangaRepository.removeCurrentUser()
        }
    }

}