package com.assignment.zenithra.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MangaEntity(
    @PrimaryKey
    val id:Int,
    val title:String,
    val thumb:String,
    val summary:String
)
