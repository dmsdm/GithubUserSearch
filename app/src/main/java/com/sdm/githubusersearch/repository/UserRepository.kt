package com.sdm.githubusersearch.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.sdm.githubusersearch.util.AbsentLiveData
import com.sdm.githubusersearch.AppExecutors
import com.sdm.githubusersearch.api.ApiSuccessResponse
import com.sdm.githubusersearch.api.GithubService
import com.sdm.githubusersearch.api.UserSearchResponse
import com.sdm.githubusersearch.db.GithubDb
import com.sdm.githubusersearch.db.UserDao
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles User objects.
 */
@Singleton
class UserRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: GithubDb,
    private val userDao: UserDao,
    private val githubService: GithubService
) {

    fun loadUser(login: String): LiveData<Resource<User>> {
        return object : NetworkBoundResource<User, User>(appExecutors) {
            override fun saveCallResult(item: User) {
                userDao.insert(item)
            }

            override fun shouldFetch(data: User?) = data == null

            override fun loadFromDb() = userDao.findByLogin(login)

            override fun createCall() = githubService.getUser(login)
        }.asLiveData()
    }

    fun searchNextPage(query: String): LiveData<Resource<Boolean>> {
        val fetchNextSearchPageTask = FetchNextSearchPageTask(
                query = query,
                githubService = githubService,
                db = db
        )
        appExecutors.networkIO().execute(fetchNextSearchPageTask)
        return fetchNextSearchPageTask.liveData
    }

    fun search(query: String): LiveData<Resource<List<User>>> {
        return object : NetworkBoundResource<List<User>, UserSearchResponse>(appExecutors) {

            override fun saveCallResult(item: UserSearchResponse) {
                val userLogins = item.items.map { it.login }
                val userSearchResult = UserSearchResult(
                        query = query,
                        logins = userLogins,
                        totalCount = item.total,
                        next = item.nextPage
                )
                db.runInTransaction {
                    userDao.insertUsers(item.items)
                    userDao.insert(userSearchResult)
                }
            }

            override fun shouldFetch(data: List<User>?) = data == null

            override fun loadFromDb(): LiveData<List<User>> {
                return userDao.search(query).switchMap { searchData ->
                    if (searchData == null) {
                        AbsentLiveData.create()
                    } else {
                        userDao.loadByLogin(searchData.logins)
                    }
                }
            }

            override fun createCall() = githubService.searchUsers(query)

            override fun processResponse(response: ApiSuccessResponse<UserSearchResponse>)
                    : UserSearchResponse {
                val body = response.body
                body.nextPage = response.nextPage
                return body
            }
        }.asLiveData()
    }
}
