package com.example.tuan6bai1.data.remote

import com.example.tuan6bai1.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL =
        "https://share.tryapps.store/mock/teach/"

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .header(
                    "Authorization",
                    "Bearer ${BuildConfig.MOCK_API_TOKEN}"
                )
                .build()

            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: TaskApi = retrofit.create(TaskApi::class.java)
}
