package com.dicoding.githubuserapp.ui.detail

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.adapter.ListUserAdapter
import com.dicoding.githubuserapp.model.User
import com.dicoding.githubuserapp.model.UserFollowersResponseItem
import com.dicoding.githubuserapp.model.UserFollowingResponseItem
import com.dicoding.githubuserapp.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowFragment : Fragment() {
    private lateinit var username: String
    private var rvFollowFragment: RecyclerView? = null
    private val followDataList = ArrayList<User>()
    private var errorMessage: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFollowFragment = view.findViewById(R.id.rv_followFragment)
        rvFollowFragment?.setHasFixedSize(true)

        errorMessage = view.findViewById(R.id.errorMessage)
        errorMessage?.visibility = View.GONE

        // Config layout by screen orientation
        if (activity?.applicationContext?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvFollowFragment?.layoutManager = GridLayoutManager(activity, 3)
        } else {
            rvFollowFragment?.layoutManager = GridLayoutManager(activity, 2)
        }

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        username= arguments?.getString(USERNAME).toString()
        when(index) {
            1 -> getUserFollowers()
            else -> getUserFollowing()
        }
    }

    private fun showFragmentRecycler(list: ArrayList<User>) {
        // Bind list data to ListUserAdapter
        val listUserAdapter = ListUserAdapter(list)
        rvFollowFragment?.adapter = listUserAdapter

        // Set user list item on click callback and intent to detail page
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(username: String) {
                val userDetailIntent = Intent(activity, UserDetailActivity::class.java)
                userDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, username)
                startActivity(userDetailIntent)
            }
        })
    }

    private fun getUserFollowers(){
        showLoading(true)
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<UserFollowersResponseItem>> {
            override fun onResponse(
                call: Call<List<UserFollowersResponseItem>>,
                response: Response<List<UserFollowersResponseItem>>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (responseBody != null && responseBody.lastIndex > 0) {
                    for(user in responseBody) {
                        val userData = User(user.login, user.avatarUrl)
                        followDataList.add(userData)
                    }
                    showFragmentRecycler(followDataList)
                } else {
                    errorMessage?.text = resources.getString(R.string.follow_fragment_empty_msg, "a follower")
                    errorMessage?.visibility = View.VISIBLE
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserFollowersResponseItem>>, t: Throwable) {
                showLoading(false)
                errorMessage?.text = resources.getString(R.string.follow_fragment_error_msg)
                errorMessage?.visibility = View.VISIBLE
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun getUserFollowing(){
        showLoading(true)
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<UserFollowingResponseItem>> {
            override fun onResponse(
                call: Call<List<UserFollowingResponseItem>>,
                response: Response<List<UserFollowingResponseItem>>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (responseBody != null && responseBody.lastIndex > 0) {
                    for(user in responseBody) {
                        val userData = User(user.login, user.avatarUrl)
                        followDataList.add(userData)
                    }
                    showFragmentRecycler(followDataList)
                } else {
                    errorMessage?.text = resources.getString(R.string.follow_fragment_empty_msg, "follow anyone")
                    errorMessage?.visibility = View.VISIBLE
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserFollowingResponseItem>>, t: Throwable) {
                showLoading(false)
                errorMessage?.text = resources.getString(R.string.follow_fragment_error_msg)
                errorMessage?.visibility = View.VISIBLE
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        val progressBar: View? = view?.findViewById(R.id.fragmentProgressBar)
        if (isLoading) {
            progressBar?.visibility = View.VISIBLE
        } else {
            progressBar?.visibility = View.GONE
        }
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        const val USERNAME = "username"
        const val TAG = "FollowFragment"
        @JvmStatic
        fun newInstance(index: Int, username: String) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString(USERNAME, username)
                }
            }
    }
}