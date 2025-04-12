package com.assignment.zenithra.repository

import com.assignment.zenithra.db.UserDao
import com.assignment.zenithra.models.User
import javax.inject.Inject

interface UserRepository {

    suspend fun getUserByEmail(email:String):List<User>

    suspend fun insertUser(user: User)
}

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao):UserRepository {
    override suspend fun getUserByEmail(email: String): List<User> {
        return userDao.getUserByEmail(email)
    }

    override suspend fun insertUser(user: User) {
         userDao.insertUser(user)
    }

}