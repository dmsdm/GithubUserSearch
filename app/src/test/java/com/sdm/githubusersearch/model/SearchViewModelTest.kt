package com.sdm.githubusersearch.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.sdm.githubusersearch.mock
import com.sdm.githubusersearch.repository.Resource
import com.sdm.githubusersearch.repository.User
import com.sdm.githubusersearch.repository.UserRepository
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class SearchViewModelTest {
    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()
    private val repository = mock(UserRepository::class.java)
    private lateinit var viewModel: SearchViewModel

    @Before
    fun init() {
        // need to init after instant executor rule is established.
        viewModel = SearchViewModel(repository)
    }

    @Test
    fun empty() {
        val result = mock<Observer<Resource<List<User>>>>()
        viewModel.results.observeForever(result)
        viewModel.loadNextPage()
        Mockito.verifyNoMoreInteractions(repository)
    }

    @Test
    fun basic() {
        val result = mock<Observer<Resource<List<User>>>>()
        viewModel.results.observeForever(result)
        viewModel.setQuery("foo")
        Mockito.verify(repository).search("foo")
        Mockito.verify(repository, Mockito.never()).searchNextPage("foo")
    }

    @Test
    fun noObserverNoQuery() {
        Mockito.`when`(repository.searchNextPage("foo")).thenReturn(mock())
        viewModel.setQuery("foo")
        Mockito.verify(repository, Mockito.never()).search("foo")
        // next page is user interaction and even if loading state is not observed, we query
        // would be better to avoid that if main search query is not observed
        viewModel.loadNextPage()
        Mockito.verify(repository).searchNextPage("foo")
    }

    @Test
    fun swap() {
        val nextPage = MutableLiveData<Resource<Boolean>>()
        Mockito.`when`(repository.searchNextPage("foo")).thenReturn(nextPage)

        val result = mock<Observer<Resource<List<User>>>>()
        viewModel.results.observeForever(result)
        Mockito.verifyNoMoreInteractions(repository)
        viewModel.setQuery("foo")
        Mockito.verify(repository).search("foo")
        viewModel.loadNextPage()

        viewModel.loadMoreStatus.observeForever(mock())
        Mockito.verify(repository).searchNextPage("foo")
        MatcherAssert.assertThat(nextPage.hasActiveObservers(), CoreMatchers.`is`(true))
        viewModel.setQuery("bar")
        MatcherAssert.assertThat(nextPage.hasActiveObservers(), CoreMatchers.`is`(false))
        Mockito.verify(repository).search("bar")
        Mockito.verify(repository, Mockito.never()).searchNextPage("bar")
    }

    @Test
    fun resetSameQuery() {
        viewModel.results.observeForever(mock())
        viewModel.setQuery("foo")
        Mockito.verify(repository).search("foo")
        Mockito.reset(repository)
        viewModel.setQuery("FOO")
        Mockito.verifyNoMoreInteractions(repository)
        viewModel.setQuery("bar")
        Mockito.verify(repository).search("bar")
    }
}
