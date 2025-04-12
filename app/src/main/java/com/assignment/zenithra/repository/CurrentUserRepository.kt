package com.assignment.zenithra.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

interface CurrentUserRepository {

    suspend fun saveUserEmail(email:String)

    val user:Flow<String>
}

class CurrentUserRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>):CurrentUserRepository {

    companion object {
        val userKey = stringPreferencesKey("user")
    }

    override val user: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e("PreferenceError", "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[userKey] ?: ""
        }

    override suspend fun saveUserEmail(email: String) {
        dataStore.edit {preferences->
            preferences[userKey]=email
        }
    }

}