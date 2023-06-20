package com.tigcal.samples.githubr

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.tigcal.samples.githubr.data.GitHubRepository
import com.tigcal.samples.githubr.data.User
import kotlinx.coroutines.launch

private const val ZERO = 0

class ProfileActivity : AppCompatActivity() {
    private val progressBar: ProgressBar by lazy { findViewById(R.id.progress_bar) }
    private val imageView: ImageView by lazy { findViewById(R.id.avatar) }
    private val userNameText: TextView by lazy { findViewById(R.id.user_name_text) }
    private val nameText: TextView by lazy { findViewById(R.id.name_text) }
    private val descriptionText: TextView by lazy { findViewById(R.id.description_text) }
    private val followersText: TextView by lazy { findViewById(R.id.followers_text) }
    private val followingText: TextView by lazy { findViewById(R.id.following_text) }
    private val userGroup: Group by lazy { findViewById(R.id.user_group) }
    private val errorText: TextView by lazy { findViewById(R.id.error_text) }

    private lateinit var repository: GitHubRepository
    private lateinit var viewModel: GitHubViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        repository = (application as GitHubrApp).gitHubRepository
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GitHubViewModel(repository) as T
            }
        })[GitHubViewModel::class.java]

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.user.collect {
                        showUser(it)
                    }
                }
                launch {
                    viewModel.error.collect { message ->
                        if (message.isNotEmpty()) {
                            showErrorScreen()
                        }
                    }
                }
            }
        }

        followersText.setOnClickListener {
            if ((followersText.tag.toString().toIntOrNull() ?: ZERO) == ZERO) {
                displayErrorMessage(getString(R.string.followers_empty))
            } else {
                openFollowPage(userNameText.tag.toString(), getString(R.string.followers))
            }
        }
        followingText.setOnClickListener {
            if ((followingText.tag.toString().toIntOrNull() ?: ZERO) == ZERO) {
                displayErrorMessage(getString(R.string.following_empty))
            } else {
                openFollowPage(userNameText.tag.toString(), getString(R.string.following))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val manager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.action_search)?.actionView as? SearchView
        searchView?.let { searchVue ->
            searchVue.setSearchableInfo(manager.getSearchableInfo(componentName))
            searchVue.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    searchUser(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return true
                }
            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun searchUser(username: String) {
        progressBar.isVisible = true
        errorText.isVisible = false
        userGroup.isVisible = false

        viewModel.searchUser(username)
    }

    private fun showUser(user: User?) {
        user ?: return

        progressBar.isVisible = false
        errorText.isVisible = false
        userGroup.isVisible = true

        with(user) {
            Glide.with(this@ProfileActivity)
                .load(user.avatar)
                .placeholder(R.mipmap.ic_launcher)
                .fitCenter()
                .into(imageView)

            nameText.text = formatText(R.string.user_name,
                name ?: getString(R.string.user_name_empty)
            )
            userNameText.text = formatText(R.string.user_user_name,username)
            userNameText.tag = username
            descriptionText.text = formatText(R.string.user_description,
                description ?: getString(R.string.user_description_empty)
            )
            followersText.text = formatText(R.string.user_followers, followers.toString())
            followersText.tag = followers.toString()
            followingText.text = formatText(R.string.user_following, following.toString())
            followingText.tag = following.toString()
        }
    }

    private fun formatText(@StringRes stringId: Int, value: String) =
        Html.fromHtml(getString(stringId,value), Html.FROM_HTML_MODE_COMPACT)

    private fun showErrorScreen() {
        progressBar.isVisible = false
        errorText.isVisible = true
        userGroup.isVisible = false
    }

    private fun displayErrorMessage(message: String) {
       Snackbar.make(descriptionText, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun openFollowPage(username: String, followLabel: String) {
        val intent = Intent(this, FollowActivity::class.java)
        intent.putExtra(FollowActivity.EXTRA_FOLLOW, followLabel)
        intent.putExtra(FollowActivity.EXTRA_USER, username)
        startActivity(intent)
    }
}