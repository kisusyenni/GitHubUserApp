package com.dicoding.githubuserapp.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapp.database.Favorite
import com.dicoding.githubuserapp.model.UserDetailResponse
import com.dicoding.githubuserapp.network.ApiConfig
import com.dicoding.githubuserapp.repository.FavoriteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val _userDetail = MutableLiveData<UserDetailResponse>()
    val userDetail: LiveData<UserDetailResponse> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "UserDetailViewModel"
    }

    // Get user detail data from Github API
    private fun getUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.body() != null) {
                    _userDetail.value = response.body()

                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun sendUsername(data: String) {
        getUserDetail(data)
    }

    fun checkUser(id: Int) = mFavoriteRepository.check(id)

    fun addToFavorite(username: String, id: Int, avatarUrl: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = Favorite(
                id,
                username,
                avatarUrl
            )
            mFavoriteRepository.insert(user)
        }
    }

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            mFavoriteRepository.delete(id)
        }
    }
}