package com.tigcal.samples.githubr

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.tigcal.samples.githubr.data.GitHubRepository
import com.tigcal.samples.githubr.data.User
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
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

@RunWith(MockitoJUnitRunner::class)
class GitHubViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun searchUser() {
        val dispatcher = StandardTestDispatcher()
        val userName = "Utilisatrice"
        val user = User(username = userName)
        val users = listOf(User(username = userName), User(username = "Utilisateur" ))

        val gitHubRepository: GitHubRepository = mock {
            onBlocking { searchUser(anyString()) } doReturn flowOf(users)
            onBlocking { getProfile(anyString()) } doReturn flowOf(user)
        }

        val viewModel = GitHubViewModel(gitHubRepository, dispatcher)
        viewModel.searchUser(userName)
        runTest {
            dispatcher.scheduler.advanceUntilIdle()
            viewModel.user.test {
                assertEquals(user, awaitItem())
            }
        }
    }

    @Test
    fun searchUserError() {
        val exception = "Test Exception"
        val dispatcher = StandardTestDispatcher()
        val userName = "Utilisatrice"

        val gitHubRepository: GitHubRepository = mock {
            onBlocking { searchUser(anyString()) } doReturn flow { throw RuntimeException(exception) }
        }

        val viewModel = GitHubViewModel(gitHubRepository, dispatcher)
        viewModel.searchUser(userName)
        runTest {
            dispatcher.scheduler.advanceUntilIdle()
            viewModel.error.test {
                assertEquals(exception, awaitItem())
            }
        }
    }

    @Test
    fun getFollowers() {
        val dispatcher = StandardTestDispatcher()
        val userName = "Utilisatrice"
        val users = listOf(User(username = "Utilisateur" ))

        val gitHubRepository: GitHubRepository = mock {
            onBlocking { getFollowers(anyString()) } doReturn flowOf(users)
        }

        val viewModel = GitHubViewModel(gitHubRepository, dispatcher)
        viewModel.getFollowersOrFollowing(userName, true)
        runTest {
            dispatcher.scheduler.advanceUntilIdle()
            viewModel.followList.test {
                assertEquals(users, awaitItem())
            }
        }
    }

    @Test
    fun getFollowersError() {
        val exception = "Test Exception"
        val dispatcher = StandardTestDispatcher()

        val gitHubRepository: GitHubRepository = mock {
            onBlocking { getFollowers(anyString()) } doReturn flow { throw RuntimeException(exception) }
        }

        val viewModel = GitHubViewModel(gitHubRepository, dispatcher)
        viewModel.getFollowersOrFollowing("Utilisateur", true)
        runTest {
            dispatcher.scheduler.advanceUntilIdle()
            viewModel.error.test {
                assert(awaitItem().isNotEmpty())
            }
        }
    }

    @Test
    fun getFollowing() {
        val dispatcher = StandardTestDispatcher()
        val userName = "Utilisatrice"
        val users = listOf(User(username = "Utilisateur" ))

        val gitHubRepository: GitHubRepository = mock {
            onBlocking { getFollowing(anyString()) } doReturn flowOf(users)
        }

        val viewModel = GitHubViewModel(gitHubRepository, dispatcher)
        viewModel.getFollowersOrFollowing(userName, false)
        runTest {
            dispatcher.scheduler.advanceUntilIdle()
            viewModel.followList.test {
                assertEquals(users, awaitItem())
            }
        }
    }

    @Test
    fun getFollowingError() {
        val exception = "Test Exception"
        val dispatcher = StandardTestDispatcher()

        val gitHubRepository: GitHubRepository = mock {
            onBlocking { getFollowing(anyString()) } doReturn flow { throw RuntimeException(exception) }
        }

        val viewModel = GitHubViewModel(gitHubRepository, dispatcher)
        viewModel.getFollowersOrFollowing("Utilisateur", false)
        runTest {
            dispatcher.scheduler.advanceUntilIdle()
            viewModel.error.test {
                assert(awaitItem().isNotEmpty())
            }
        }
    }
}