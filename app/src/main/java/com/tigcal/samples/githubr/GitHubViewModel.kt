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

    private val _followers = MutableStateFlow(emptyList<User>())
    val followers: StateFlow<List<User>> = _followers

    private val _following = MutableStateFlow(emptyList<User>())
    val following: StateFlow<List<User>> = _following

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private fun searchUser(username: String) {
        viewModelScope.launch {
            try {
                val result = gitHubRepository.searchUser(username).first()
                gitHubRepository.getProfile(username).collect {
                    _user.value = it
                }
            } catch (exception: Exception) {
                _error.value = "User Not found"
            }
        }
    }

    private fun getFollowers(username: String) {
        viewModelScope.launch {
            gitHubRepository.getFollowers(username).catch {
                    _error.value = "No followers to display"
                }.collect {
                    _followers.value = it
                }
        }
    }

    private fun getFollowing(username: String) {
        viewModelScope.launch {
            gitHubRepository.getFollowing(username).catch {
                _error.value = "User is not following anyone"
            }.collect {
                _following.value = it
            }
        }
    }

}