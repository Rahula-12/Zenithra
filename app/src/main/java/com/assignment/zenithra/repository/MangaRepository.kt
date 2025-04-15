package com.assignment.zenithra.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.paging.Pager
import com.assignment.zenithra.models.MangaEntity
import com.assignment.zenithra.repository.CurrentUserRepositoryImpl.Companion.userKey
import javax.inject.Inject

interface MangaRepository {
    val pager: Pager<Int, MangaEntity>

    suspend fun removeCurrentUser()
}

class MangaRepositoryImpl @Inject constructor(override val pager: Pager<Int,MangaEntity>,val dataStore: DataStore<Preferences>):MangaRepository {
    override suspend fun removeCurrentUser() {
        dataStore.edit {preferences->
            preferences[userKey]=""
        }
    }


}