package com.sdm.githubusersearch.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.sdm.githubusersearch.repository.User
import com.sdm.githubusersearch.repository.UserSearchResult

/**
 * Main database description.
 */
@Database(
    entities = [
        User::class,
        UserSearchResult::class],
    version = 3,
    exportSchema = false
)
abstract class GithubDb : RoomDatabase() {

    abstract fun userDao(): UserDao
}
