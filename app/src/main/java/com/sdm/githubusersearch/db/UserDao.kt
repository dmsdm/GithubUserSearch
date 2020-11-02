package com.sdm.githubusersearch.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sdm.githubusersearch.repository.User
import com.sdm.githubusersearch.repository.UserSearchResult

/**
 * Interface for database access for User related operations.
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserSearchResult)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<User>)

    @Query("SELECT * FROM user WHERE login = :login")
    fun findByLogin(login: String): LiveData<User>

    @Query("SELECT * FROM UserSearchResult WHERE `query` = :query")
    fun search(query: String): LiveData<UserSearchResult?>

    @Query("SELECT * FROM User WHERE login in (:logins)")
    fun loadByLogin(logins: List<String>): LiveData<List<User>>

    @Query("SELECT * FROM UserSearchResult WHERE `query` = :query")
    fun findSearchResult(query: String): UserSearchResult?
}
