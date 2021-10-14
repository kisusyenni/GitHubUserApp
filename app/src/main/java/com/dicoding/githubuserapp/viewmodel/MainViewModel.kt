package com.dicoding.githubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapp.model.UsersResponseItem

class MainViewModel: ViewModel() {
    private val _users = MutableLiveData<List<UsersResponseItem>>()
    val users: LiveData<List<UsersResponseItem>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "MainViewModel"
    }

    init {
//        getUserList()
    }
}