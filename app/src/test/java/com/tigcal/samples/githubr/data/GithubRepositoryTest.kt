package com.tigcal.samples.githubr.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import java.lang.RuntimeException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class GithubRepositoryTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun searchUsers() {
        val users = listOf(User(id = 1L), User(id = 2L))
        val response = UserSearchResponse(items = users)

        val gitHubService: GitHubService = mock {
            onBlocking { searchUser(ArgumentMatchers.anyString()) } doReturn response
        }

        val gitHubRepository = GitHubRepository(gitHubService)

        runTest {
            gitHubRepository.searchUser("he").test {
                Assert.assertEquals(users, awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun searchUsersError() {
        val exception = "Test Exception"

        val gitHubService: GitHubService = mock {
            onBlocking { searchUser(ArgumentMatchers.anyString()) } doThrow RuntimeException(
                exception
            )
        }

        val gitHubRepository = GitHubRepository(gitHubService)

        runTest {
            gitHubRepository.searchUser("him").test {
                Assert.assertEquals(exception, awaitError().message)
            }
        }
    }

    @Test
    fun getProfile() {
        val user = User(username = "she", name = "Her")

        val gitHubService: GitHubService = mock {
            onBlocking { getProfile(ArgumentMatchers.anyString()) } doReturn user
        }

        val gitHubRepository = GitHubRepository(gitHubService)

        runTest {
            gitHubRepository.getProfile("me").test {
                Assert.assertEquals(user, awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun getProfileError() {
        val exception = "Test Exception"

        val gitHubService: GitHubService = mock {
            onBlocking { getProfile(ArgumentMatchers.anyString()) } doThrow RuntimeException(
                exception
            )
        }

        val gitHubRepository = GitHubRepository(gitHubService)

        runTest {
            gitHubRepository.getProfile("you").test {
                Assert.assertEquals(exception, awaitError().message)
            }
        }
    }

    @Test
    fun getFollowers() {
        val followers = listOf(User(id = 3L), User(id = 4L))

        val gitHubService: GitHubService = mock {
            onBlocking { getFollowers(ArgumentMatchers.anyString()) } doReturn followers
        }

        val gitHubRepository = GitHubRepository(gitHubService)

        runTest {
            gitHubRepository.getFollowers("he").test {
                Assert.assertEquals(followers, awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun getFollowersError() {
        val exception = "Test Exception"

        val gitHubService: GitHubService = mock {
            onBlocking { getFollowers(ArgumentMatchers.anyString()) } doThrow RuntimeException(
                exception
            )
        }

        val gitHubRepository = GitHubRepository(gitHubService)

        runTest {
            gitHubRepository.getFollowers("she").test {
                Assert.assertEquals(exception, awaitError().message)
            }
        }
    }

    @Test
    fun getFollowing() {
        val followers = listOf(User(id = 5L), User(id = 6L))

        val gitHubService: GitHubService = mock {
            onBlocking { getFollowing(ArgumentMatchers.anyString()) } doReturn followers
        }

        val gitHubRepository = GitHubRepository(gitHubService)

        runTest {
            gitHubRepository.getFollowing("they").test {
                Assert.assertEquals(followers, awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun getFollowingError() {
        val exception = "Test Exception"

        val gitHubService: GitHubService = mock {
            onBlocking { getFollowing(ArgumentMatchers.anyString()) } doThrow RuntimeException(
                exception
            )
        }

        val gitHubRepository = GitHubRepository(gitHubService)

        runTest {
            gitHubRepository.getFollowing("them").test {
                Assert.assertEquals(exception, awaitError().message)
            }
        }
    }
}