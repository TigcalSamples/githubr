package com.tigcal.samples.githubr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tigcal.samples.githubr.data.GitHubRepository
import com.tigcal.samples.githubr.data.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GitHubViewModel(
    private val gitHubRepository: GitHubRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user: StateFlow<User?> = _user

    private val _followList = MutableStateFlow(emptyList<User>())
    val followList: StateFlow<List<User>> = _followList

    private val _refreshing = MutableStateFlow(false)
    val refreshing: StateFlow<Boolean> = _refreshing

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun searchUser(username: String) {
        _error.value = ""

        viewModelScope.launch(dispatcher) {
            try {
                val result = gitHubRepository.searchUser(username).first()
                gitHubRepository.getProfile(username).collect {
                    _user.value = it
                }
            } catch (exception: Exception) {
                _error.value = "User Not Found"
            }
        }
    }

    fun getFollowersOrFollowing(userName: String, isFollower: Boolean) {
        if (isFollower) {
            getFollowers(userName)
        } else {
            getFollowing(userName)
        }
    }

    private fun getFollowers(username: String) {
        viewModelScope.launch(dispatcher) {
            gitHubRepository.getFollowers(username).catch {
                _error.value = "No followers to display"
            }.collect {
                _followList.value = it
                _refreshing.value = false
            }
        }
    }

    private fun getFollowing(username: String) {
        viewModelScope.launch(dispatcher) {
            gitHubRepository.getFollowing(username).catch {
                _error.value = "User is not following anyone"
            }.collect {
                _followList.value = it
                _refreshing.value = false
            }
        }
    }

}