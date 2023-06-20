package com.tigcal.samples.githubr.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.tigcal.samples.githubr.GitHubViewModel
import com.tigcal.samples.githubr.R
import com.tigcal.samples.githubr.ui.theme.GithubrTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FollowList(
    userName: String,
    isFollower: Boolean,
    viewModel: GitHubViewModel,
    modifier: Modifier = Modifier
) {
    viewModel.getFollowersOrFollowing(userName, isFollower)

    val users by viewModel.followList.collectAsStateWithLifecycle()
    val refreshing by viewModel.refreshing.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(refreshing, {
        viewModel.getFollowersOrFollowing(userName, isFollower)
    })

    Box(
        modifier = modifier.fillMaxWidth().pullRefresh(pullRefreshState)
    ) {
        LazyColumn {
            items(users) { user ->
                Row(
                    modifier = Modifier.padding(8.dp)
                ) {
                    AsyncImage(
                        model = user.avatar,
                        contentDescription = stringResource(id = R.string.user_avatar),
                        modifier = Modifier.height(80.dp).clip(CircleShape)
                    )
                    Text(
                        text = user.username,
                        modifier = Modifier.padding(start = 16.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun FollowListPreview() {
    GithubrTheme {
        FollowList(userName = "User", isFollower = true, viewModel())
    }
}