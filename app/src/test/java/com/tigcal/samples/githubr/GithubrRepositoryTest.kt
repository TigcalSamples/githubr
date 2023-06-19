package com.tigcal.samples.githubr

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.tigcal.samples.githubr.data.GitHubRepository
import com.tigcal.samples.githubr.data.GitHubService
import com.tigcal.samples.githubr.data.User
import com.tigcal.samples.githubr.data.UserSearchResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import java.lang.RuntimeException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class GithubrRepositoryTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun searchUsers() {
        val users = listOf(User(id="1"), User(id="2"))
        val response = UserSearchResponse(users)

        val gitHubService: GitHubService = mock {
            onBlocking { searchUser(anyString()) } doReturn response
        }

        val gitHubRepository = GitHubRepository(gitHubService)

        runTest {
            gitHubRepository.searchUser("he").test {
                assertEquals(users, awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun searchUsersError() {
        val exception = "Test Exception"

        val gitHubService: GitHubService = mock {
            onBlocking { searchUser(anyString()) } doThrow  RuntimeException(exception)
        }

        val gitHubRepository = GitHubRepository(gitHubService)

        runTest {
            gitHubRepository.searchUser("him").test {
                assertEquals(exception, awaitError().message)
            }
        }
    }

    @Test
    fun getProfile() {
        val user = User(username = "she", name = "Her")

        val gitHubService: GitHubService = mock {
            onBlocking { getProfile(anyString()) } doReturn user
        }

        val gitHubRepository = GitHubRepository(gitHubService)

        runTest {
            gitHubRepository.getProfile("me").test {
                assertEquals(user, awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun getProfileError() {
        val exception = "Test Exception"

        val gitHubService: GitHubService = mock {
            onBlocking { getProfile(anyString()) } doThrow  RuntimeException(exception)
        }

        val gitHubRepository = GitHubRepository(gitHubService)

        runTest {
            gitHubRepository.getProfile("you").test {
                assertEquals(exception, awaitError().message)
            }
        }
    }

    @Test
    fun getFollowers() {
        val followers = listOf(User(id="3"), User(id="4"))

        val gitHubService: GitHubService = mock {
            onBlocking { getFollowers(anyString()) } doReturn followers
        }

        val gitHubRepository = GitHubRepository(gitHubService)

        runTest {
            gitHubRepository.getFollowers("he").test {
                assertEquals(followers, awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun getFollowersError() {
        val exception = "Test Exception"

        val gitHubService: GitHubService = mock {
            onBlocking { getFollowers(anyString()) } doThrow  RuntimeException(exception)
        }

        val gitHubRepository = GitHubRepository(gitHubService)

        runTest {
            gitHubRepository.getFollowers("she").test {
                assertEquals(exception, awaitError().message)
            }
        }
    }

    @Test
    fun getFollowing() {
        val followers = listOf(User(id="3"), User(id="4"))

        val gitHubService: GitHubService = mock {
            onBlocking { getFollowing(anyString()) } doReturn followers
        }

        val gitHubRepository = GitHubRepository(gitHubService)

        runTest {
            gitHubRepository.getFollowing("they").test {
                assertEquals(followers, awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun getFollowingError() {
        val exception = "Test Exception"

        val gitHubService: GitHubService = mock {
            onBlocking { getFollowing(anyString()) } doThrow  RuntimeException(exception)
        }

        val gitHubRepository = GitHubRepository(gitHubService)

        runTest {
            gitHubRepository.getFollowing("them").test {
                assertEquals(exception, awaitError().message)
            }
        }
    }
}