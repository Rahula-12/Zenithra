package com.assignment.zenithra.screens.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.zenithra.models.User
import com.assignment.zenithra.repository.CurrentUserRepository
import com.assignment.zenithra.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val currentUserRepository: CurrentUserRepository
):ViewModel() {

     val currentUser:StateFlow<String> = currentUserRepository.user.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ""
    )

    fun saveUser(email:String,password:String) {
        viewModelScope.launch(Dispatchers.IO) {
            currentUserRepository.saveUserEmail(email)
            userRepository.insertUser(User(email=email, password = password))
        }
    }

    suspend fun checkUser(email: String,password: String):Boolean {
        val findUser=userRepository.getUserByEmail(email)
        if(findUser.isEmpty() || findUser[0].password!=password)  return false
        return true
    }

}