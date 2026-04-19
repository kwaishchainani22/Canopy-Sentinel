package com.erc.canopysentinalg.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.erc.canopysentinalg.data.model.User
import com.erc.canopysentinalg.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository(application)
    
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    init {
        loadSavedUser()
    }
    
    fun signInAsGuest() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = authRepository.signInAsGuest()
                _currentUser.value = user
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun signInWithGoogle(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val signedInUser = authRepository.signInWithGoogle(user)
                _currentUser.value = signedInUser
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _currentUser.value = null
        }
    }
    
    private fun loadSavedUser() {
        viewModelScope.launch {
            authRepository.loadSavedUser()
            _currentUser.value = authRepository.getCurrentUser()
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}
