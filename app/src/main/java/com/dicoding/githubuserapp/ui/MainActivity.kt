package com.dicoding.githubuserapp.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.adapter.ListUserAdapter
import com.dicoding.githubuserapp.databinding.ActivityMainBinding
import com.dicoding.githubuserapp.model.User
import com.dicoding.githubuserapp.ui.detail.UserDetailActivity
import com.dicoding.githubuserapp.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var rvUsers: RecyclerView
    private lateinit var message : TextView
    private lateinit var mainViewModel : MainViewModel
    private var query: String? = null
    private var listUsers = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        rvUsers = activityMainBinding.rvUsers
        rvUsers.setHasFixedSize(true)

        // Bind message text view
        message = activityMainBinding.mainMessage

        // Config layout by screen orientation
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvUsers.layoutManager = GridLayoutManager(this, 3)
        } else {
            rvUsers.layoutManager = GridLayoutManager(this, 2)
        }

        message.visibility = View.GONE

        // Initialize mainViewModel
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        // Observe users list
        mainViewModel.usersList.observe(this, { users ->
            users?.let { listUsers = it as ArrayList<User> }
            showRecycler(listUsers)
        })

        // Observe progress bar loading
        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        // Observe search query
        mainViewModel.searchQuery.observe(this, {
            query = it
        })

    }

    private fun showRecycler(list: ArrayList<User>) {
        if(list.size > 0) {
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
            message.visibility = View.GONE
        } else {
            message.visibility = View.VISIBLE
            message.text = resources.getString(R.string.main_empty_msg)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        // Add search functionality to action bar
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        // Observes search query
        searchView.setQuery(query, false)
        searchView.setIconifiedByDefault(query !== "")

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.searchUserQuery(query)
                if(query != "") mainViewModel.saveSearchQuery(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                if(newText != "") mainViewModel.saveSearchQuery(newText)
                return false
            }
        })

        //Expand collapse listener of searchView on action bar
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                mainViewModel.collapseSearchView(true)
                return true
            }

            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }
        })
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        activityMainBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}