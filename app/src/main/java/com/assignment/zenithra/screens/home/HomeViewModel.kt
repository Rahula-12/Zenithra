package com.assignment.zenithra.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.assignment.zenithra.repository.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val mangaRepository: MangaRepository):ViewModel() {

    val mangaEntityFlow = mangaRepository.pager.flow.cachedIn(viewModelScope)

    fun removeCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            mangaRepository.removeCurrentUser()
        }
    }

}