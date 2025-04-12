package com.assignment.zenithra.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val id:String=UUID.randomUUID().toString(),
    val email:String,
    val password:String
)
