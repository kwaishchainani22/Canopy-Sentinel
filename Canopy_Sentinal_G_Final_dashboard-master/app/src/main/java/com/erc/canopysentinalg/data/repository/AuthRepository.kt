package com.erc.canopysentinalg.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.erc.canopysentinalg.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class AuthRepository(private val context: Context) {
    private val USER_ID_KEY = stringPreferencesKey("user_id")
    private val USER_NAME_KEY = stringPreferencesKey("user_name")
    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    private val USER_PHOTO_KEY = stringPreferencesKey("user_photo")
    private val IS_GUEST_KEY = stringPreferencesKey("is_guest")
    
    private var currentUser: User? = null
    
    suspend fun signInAsGuest(): User {
        val guestUser = User(
            id = "guest_${System.currentTimeMillis()}",
            name = "Guest User",
            email = null,
            photoUrl = null,
            isGuest = true
        )
        currentUser = guestUser
        saveUser(guestUser)
        return guestUser
    }
    
    suspend fun signInWithGoogle(user: User): User {
        currentUser = user.copy(isGuest = false)
        saveUser(user)
        return user
    }
    
    suspend fun signOut() {
        currentUser = null
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
    
    fun getCurrentUser(): User? = currentUser
    
    fun getCurrentUserFlow(): Flow<User?> {
        return context.dataStore.data.map { prefs ->
            val userId = prefs[USER_ID_KEY]
            if (userId != null) {
                User(
                    id = userId,
                    name = prefs[USER_NAME_KEY],
                    email = prefs[USER_EMAIL_KEY],
                    photoUrl = prefs[USER_PHOTO_KEY],
                    isGuest = prefs[IS_GUEST_KEY] == "true"
                )
            } else {
                null
            }
        }
    }
    
    private suspend fun saveUser(user: User) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID_KEY] = user.id
            prefs[USER_NAME_KEY] = user.name ?: ""
            prefs[USER_EMAIL_KEY] = user.email ?: ""
            prefs[USER_PHOTO_KEY] = user.photoUrl ?: ""
            prefs[IS_GUEST_KEY] = if (user.isGuest) "true" else "false"
        }
    }
    
    suspend fun loadSavedUser() {
        val prefs = context.dataStore.data
        prefs.collect { preferences ->
            val userId = preferences[USER_ID_KEY]
            if (userId != null) {
                currentUser = User(
                    id = userId,
                    name = preferences[USER_NAME_KEY],
                    email = preferences[USER_EMAIL_KEY],
                    photoUrl = preferences[USER_PHOTO_KEY],
                    isGuest = preferences[IS_GUEST_KEY] == "true"
                )
            }
        }
    }
}
