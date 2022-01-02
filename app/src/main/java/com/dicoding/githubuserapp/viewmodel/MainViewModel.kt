package com.dicoding.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.githubuserapp.model.ItemsItem
import com.dicoding.githubuserapp.model.User
import com.dicoding.githubuserapp.model.UserSearchResponse
import com.dicoding.githubuserapp.model.UsersResponseItem
import com.dicoding.githubuserapp.network.ApiConfig
import com.dicoding.githubuserapp.settings.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {
    private val _usersList = MutableLiveData<List<User>?>()
    val usersList: LiveData<List<User>?> = _usersList

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getUsersList()
    }

    private fun getUsersList() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers()
        client.enqueue(object : Callback<List<UsersResponseItem>> {
            override fun onResponse(
                call: Call<List<UsersResponseItem>>,
                response: Response<List<UsersResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _usersList.value = setUsersListData(responseBody)

                } else {

                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UsersResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    // Get user data list and get user's login and avatarUrl data to ArrayList<User>
    private fun setUsersListData(list: List<UsersResponseItem>?): ArrayList<User>? {
        val data = ArrayList<User>()
        return if(list!= null) {
            for(user in list) {
                val userData = User(user.login, user.avatarUrl)
                data.add(userData)
            }
            data
        } else {
            null
        }
    }

    // Get search result list and get user's login and avatarUrl data to ArrayList<User>
    private fun setSearchResultData(list: List<ItemsItem>?): ArrayList<User>? {
        val data = ArrayList<User>()
        return if(list!= null) {
            for(user in list) {
                val userData = User(user.login, user.avatarUrl)
                data.add(userData)
            }
            data
        } else {
            null
        }
    }

    // Get search by query result list
    fun searchUserQuery(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchResult(username)
        client.enqueue(object : Callback<UserSearchResponse> {
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val searchResponse = response.body()
                    _usersList.value = setSearchResultData(searchResponse?.items)
                    _searchQuery.value = username

                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun collapseSearchView(collapse: Boolean) {
        if(collapse) getUsersList()
    }

    fun saveSearchQuery(query: String) {
        _searchQuery.value = query
    }

    companion object{
        private const val TAG = "MainViewModel"
    }

//    fun getThemeSettings(): LiveData<Boolean> {
//        return pref.getThemeSetting().asLiveData()
//    }
//
//    fun saveThemeSetting(isDarkModeActive: Boolean) {
//        viewModelScope.launch {
//            pref.saveThemeSetting(isDarkModeActive)
//        }
//    }

}