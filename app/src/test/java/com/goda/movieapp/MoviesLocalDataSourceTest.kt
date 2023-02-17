package com.goda.movieapp


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.goda.movieapp.util.Constants.Companion.API_KEY
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MoviesLocalDataSourceTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
/*

    lateinit var articleLocalDataSource: ArticleLocalDataSource


    @Mock
    lateinit var dao: ArticlesDao


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        articleLocalDataSource = ArticleLocalDataSourceImpl(dao, ArticleMapper(Gson()) , mainCoroutineRule.coroutineContext)
    }


    @Test
    fun testInvalidEntityArticles()=mainCoroutineRule.runBlockingTest{
        Mockito.`when`(dao.getArticles(API_KEY)).thenReturn(null)

        val result = articleLocalDataSource.getArticles()

        assert(result == null)
    }*/





}