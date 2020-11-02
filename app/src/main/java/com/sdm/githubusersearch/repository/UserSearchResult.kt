package com.sdm.githubusersearch.repository

import androidx.room.Entity
import androidx.room.TypeConverters
import com.sdm.githubusersearch.db.GithubTypeConverters

@Entity(primaryKeys = ["query"])
@TypeConverters(GithubTypeConverters::class)
data class UserSearchResult(
    val query: String,
    val logins: List<String>,
    val totalCount: Int,
    val next: Int?
)
