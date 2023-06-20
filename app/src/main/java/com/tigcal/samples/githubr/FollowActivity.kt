package com.tigcal.samples.githubr

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tigcal.samples.githubr.data.User
import com.tigcal.samples.githubr.ui.FollowList
import com.tigcal.samples.githubr.ui.theme.GithubrTheme

class FollowActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val label = intent.getStringExtra(EXTRA_FOLLOW) ?: ""
        val userName = intent.getStringExtra(EXTRA_USER) ?: ""
        val isFollower = label == getString(R.string.followers)
        val clickAction = { user: User ->
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra(EXTRA_USER, user.username)
            startActivity(intent)
        }

        setContent {
            GithubrTheme {
                FollowScreen(label, userName, isFollower, clickAction)
            }
        }
    }

    companion object {
        const val EXTRA_FOLLOW = "com.tigcal.samples.githubr.follow"
        const val EXTRA_USER = "com.tigcal.samples.githubr.user"
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowScreen(
    label: String,
    userName: String,
    isFollower: Boolean,
    clickAction: (User) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "$userName : $label",
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                colors = topAppBarColors(MaterialTheme.colorScheme.primary)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 64.dp,
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 16.dp
                )
        ) {
            val repository =
                (LocalContext.current.applicationContext as GitHubrApp).gitHubRepository
            FollowList(
                userName = userName,
                isFollower = isFollower,
                viewModel = viewModel(factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return GitHubViewModel(repository) as T
                    }
                }),
                clickAction = clickAction
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FollowScreenPreview() {
    GithubrTheme {
        FollowScreen(label = "Label", userName = "user", isFollower = false, clickAction = {})
    }
}