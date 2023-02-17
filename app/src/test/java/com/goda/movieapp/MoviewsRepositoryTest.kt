package com.goda.movieapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MoviewsRepositoryTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

/*
    lateinit var repository: ArticleRepository

    @Mock
    lateinit var localDataSource: ArticleLocalDataSource

    @Mock
    lateinit var remoteDataSource: ArticleRemoteDataSource


    val fakeArticles = listOf(
        Article(
            123344,
            "Hello ",
            123L,
            "shika",
            "hhhh",
            "",
            "12/2/3333",
            "4",
            "",
            "",
            "",
            "",
            "",
            1,
            emptyList()
        )
    )


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        repository = ArticleRepositoryImpl(localDataSource, remoteDataSource)


    }

    @Test
    fun testGetArticlesFromAPI() = mainCoroutineRule.runBlockingTest {

        Mockito.`when`(localDataSource.getArticles()).thenReturn(null)
        Mockito.`when`(remoteDataSource.getArticles()).thenReturn(fakeArticles)


        val result = repository.getArticles(false)


        assert(LiveDataTestUtil.getValue(result).status == Status.LOADING)

        assert(LiveDataTestUtil.getValue(result).status == Status.SUCCESS)
        assert(LiveDataTestUtil.getValue(result).data == fakeArticles)


    }

    @Test
    fun testGetArticlesFromDb() = mainCoroutineRule.runBlockingTest {

        Mockito.`when`(localDataSource.getArticles()).thenReturn(fakeArticles)


        val result = repository.getArticles(false)


        assert(LiveDataTestUtil.getValue(result).status == Status.LOADING)

        kotlinx.coroutines.delay(2000)
        assert(LiveDataTestUtil.getValue(result).status == Status.SUCCESS)
        assert(LiveDataTestUtil.getValue(result).data == fakeArticles)


    }

    @Test(expected = Exception::class)
    fun testGetArticlesThrowException() = mainCoroutineRule.runBlockingTest {

        Mockito.`when`(localDataSource.getArticles()).thenReturn(null)
        Mockito.doThrow(Exception("error")).`when`(remoteDataSource.getArticles())

        repository.getArticles(false)


    }*/


}
