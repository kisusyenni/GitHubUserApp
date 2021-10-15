package com.dicoding.githubuserapp.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.adapter.ListUserAdapter
import com.dicoding.githubuserapp.databinding.ActivityMainBinding
import com.dicoding.githubuserapp.model.User
import com.dicoding.githubuserapp.model.UserSearchResponse
import com.dicoding.githubuserapp.model.UsersResponseItem
import com.dicoding.githubuserapp.network.ApiConfig
import com.dicoding.githubuserapp.ui.detail.UserDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var rvUsers: RecyclerView
    val listUsers = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        rvUsers = activityMainBinding.rvUsers
        rvUsers.setHasFixedSize(true)

        // Config layout by screen orientation
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvUsers.layoutManager = GridLayoutManager(this, 3)
        } else {
            rvUsers.layoutManager = GridLayoutManager(this, 2)
        }

        // Get users list
        getUsersList()

    }

    // Get user list data from Github API
    private fun getUsersList() {
        showLoading(true)
        val client = ApiConfig.getApiService().getUsers()
        client.enqueue(object : Callback<List<UsersResponseItem>> {
            override fun onResponse(
                call: Call<List<UsersResponseItem>>,
                response: Response<List<UsersResponseItem>>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (responseBody != null) {

                    for(user in responseBody) {
                        val userData = User(user.login, user.avatarUrl)
                        listUsers.add(userData)
                    }

                    showRecycler(listUsers)

                } else {

                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UsersResponseItem>>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showRecycler(list: ArrayList<User>) {
        // Bind list data to ListUserAdapter
        val listUserAdapter = ListUserAdapter(list)
        rvUsers.adapter = listUserAdapter

        // Set user list item on click callback and intent to detail page
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(username: String) {
                val userDetailIntent = Intent(this@MainActivity, UserDetailActivity::class.java)
                userDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, username)
                startActivity(userDetailIntent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        // Add search functionality to action bar
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchUserQuery(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        //Expand collapse listener of searchView on action bar
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                showRecycler(listUsers)
                return true
            }

            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }
        })
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        val progressBar: View = activityMainBinding.progressBar
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    // Get search by query result list
    private fun searchUserQuery(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getSearchResult(username)
        client.enqueue(object : Callback<UserSearchResponse> {
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (responseBody != null) {
                    val results = ArrayList<User>()
                    for(user in responseBody.items) {
                        val userData = User(user.login, user.avatarUrl)
                        results.add(userData)
                    }
                    showRecycler(results)

                } else {
                    activityMainBinding.notFoundText.text = resources.getString(R.string.main_not_found_msg, username)
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}