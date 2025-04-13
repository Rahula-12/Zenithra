package com.assignment.zenithra.network

import com.assignment.zenithra.BuildConfig
import com.assignment.zenithra.models.MangaRemoteData
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MangaApiService {

    @Headers(
        "x-rapidapi-key:${BuildConfig.RapidApiKey}",
        "x-rapidapi-host:${BuildConfig.RapidApiHost}"
    )
    @GET("manga/fetch")
    suspend fun getMangaList(@Query("page") pageNo:Int):MangaRemoteData

}