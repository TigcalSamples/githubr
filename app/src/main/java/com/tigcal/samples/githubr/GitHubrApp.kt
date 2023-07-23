package com.tigcal.samples.githubr

import android.app.Application
import com.squareup.moshi.Moshi
import com.tigcal.samples.githubr.data.GitHubRepository
import com.tigcal.samples.githubr.data.GitHubService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GitHubrApp: Application() {
    lateinit var gitHubRepository: GitHubRepository

    private val token = "YOUR_PERSONAL_ACCESS_TOKEN"

    override fun onCreate() {
        super.onCreate()

        val moshi = Moshi.Builder()
            .build()

        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(
                Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder.header("Authorization", "Bearer $token")
                    return@Interceptor chain.proceed(builder.build())
                }
            )
        }.build()


        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
        val gitHubService = retrofit.create(GitHubService::class.java)
        gitHubRepository = GitHubRepository(gitHubService)
    }
}