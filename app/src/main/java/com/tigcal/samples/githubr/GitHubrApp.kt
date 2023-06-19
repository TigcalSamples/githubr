package com.tigcal.samples.githubr

import android.app.Application
import com.squareup.moshi.Moshi
import com.tigcal.samples.githubr.data.GitHubRepository
import com.tigcal.samples.githubr.data.GitHubService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GitHubrApp: Application() {
    lateinit var gitHubRepository: GitHubRepository

    override fun onCreate() {
        super.onCreate()

        val moshi = Moshi.Builder()
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        val gitHubService = retrofit.create(GitHubService::class.java)
        gitHubRepository = GitHubRepository(gitHubService)
    }
}