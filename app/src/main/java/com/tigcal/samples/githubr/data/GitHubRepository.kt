package com.tigcal.samples.githubr.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GitHubRepository(private val githubService: GitHubService) {

    fun searchUser(username: String): Flow<List<User>> {
        return flow {
            emit(githubService.searchUser(username).items)
        }.flowOn(Dispatchers.IO)
    }

    fun getProfile(username: String): Flow<User> {
        return flow {
            emit(githubService.getProfile(username))
        }.flowOn(Dispatchers.IO)
    }

    fun getFollowers(username: String): Flow<List<User>> {
        return flow {
            emit(githubService.getFollowers(username))
        }.flowOn(Dispatchers.IO)
    }

    fun getFollowing(username: String): Flow<List<User>> {
        return flow {
            emit(githubService.getFollowing(username))
        }.flowOn(Dispatchers.IO)
    }
}
