package com.zhigaras.binrequest.repository

import com.zhigaras.binrequest.model.BinReplyModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject


private const val BASE_URL = "https://lookup.binlist.net/"

interface BinRequestInterface {
    
    @GET("{number}")
    suspend fun getBinInfo(@Path("number") number: String): Response<BinReplyModel>
    
}

class RemoteRepository @Inject constructor() {
    
    private val retrofit = Retrofit
        .Builder()
        .client(
            OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY
            }).build()
        )
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    
    val binRequestApi: BinRequestInterface = retrofit.create(BinRequestInterface::class.java)
    
}