package com.sdm.githubusersearch.api

import com.sdm.githubusersearch.repository.User
import com.google.gson.annotations.SerializedName

data class UserSearchResponse(
        @SerializedName("total_count")
        val total: Int = 0,
        @SerializedName("items")
        val items: List<User>
) {
    var nextPage: Int? = null
}
