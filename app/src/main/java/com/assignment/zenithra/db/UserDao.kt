package com.assignment.zenithra.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.assignment.zenithra.models.User

@Dao
interface UserDao {

    @Query("Select * from user where email=:email")
    suspend fun getUserByEmail(email:String): List<User>

    @Insert
    suspend fun insertUser(user: User)

}