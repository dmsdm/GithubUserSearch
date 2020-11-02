package com.sdm.githubusersearch.api

import androidx.lifecycle.LiveData
import com.sdm.githubusersearch.repository.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * REST API access points
 */
interface GithubService {
    @GET("users/{login}")
    fun getUser(@Path("login") login: String): LiveData<ApiResponse<User>>

    @GET("search/users")
    fun searchUsers(@Query("q") query: String): LiveData<ApiResponse<UserSearchResponse>>

    @GET("search/users")
    fun searchUsers(@Query("q") query: String, @Query("page") page: Int): Call<UserSearchResponse>
}
