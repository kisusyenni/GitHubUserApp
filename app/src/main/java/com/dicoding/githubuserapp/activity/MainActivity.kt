package com.dicoding.githubuserapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.adapter.ListUserAdapter
import com.dicoding.githubuserapp.data.User

class MainActivity : AppCompatActivity() {

    private lateinit var rvUsers: RecyclerView

    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvUsers = findViewById(R.id.rv_users)
        rvUsers.setHasFixedSize(true)

        list.addAll(listUsers)
        showRecyclerList()
    }

    private val listUsers: ArrayList<User>
        @SuppressLint("Recycle")
        get() {
            // Get users data from resources
            val dataUsername = resources.getStringArray(R.array.username)
            val dataName = resources.getStringArray(R.array.name)
            val dataAvatar = resources.obtainTypedArray(R.array.avatar)
            val dataCompany = resources.getStringArray(R.array.company)
            val dataLocation = resources.getStringArray(R.array.location)
            val dataRepository = resources.getStringArray(R.array.repository)
            val dataFollower = resources.getStringArray(R.array.followers)
            val dataFollowing = resources.getStringArray(R.array.following)

            // Create users object and add to listUser
            val listUser = ArrayList<User>()
            for (i in dataName.indices) {
                val user = User(dataUsername[i], dataName[i], dataAvatar.getResourceId(i, -1), dataCompany[i], dataLocation[i], dataRepository[i], dataFollower[i], dataFollowing[i])
                listUser.add(user)
            }
            return listUser
        }

    private fun showRecyclerList() {

        // Config layout by screen orientation
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvUsers.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvUsers.layoutManager = LinearLayoutManager(this)
        }

        // Bind list data to ListUserAdapter
        val listUserAdapter = ListUserAdapter(list)
        rvUsers.adapter = listUserAdapter

        // Set user list item on click callback and intent to detail page
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val userDetailIntent = Intent(this@MainActivity, UserDetailActivity::class.java)
                userDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, user)
                startActivity(userDetailIntent)
            }
        })
    }

}