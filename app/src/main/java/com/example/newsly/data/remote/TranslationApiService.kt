package com.example.newsly.data.remote

import com.example.newsly.data.model.MyMemoryTranslationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslationApiService {

    @GET("get")
    suspend fun translate(
        @Query("q") query: String,
        @Query("langpair") langPair: String // e.g. "en|ur" or "ur|en"
    ): Response<MyMemoryTranslationResponse>
}
