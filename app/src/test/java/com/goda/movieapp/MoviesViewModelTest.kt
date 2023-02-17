package com.goda.movieapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class MoviesViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

/*
    lateinit var articleViewModel: ArticlesViewModel

    lateinit var getArticlesUseCase: GetArticlesUseCase

    lateinit var getSingleArticleUseCase: GetSingleArticleUseCase

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
    fun init(){
        getSingleArticleUseCase = mock ()
    }


    @Test
    fun testLoadingData() = mainCoroutineRule.runBlockingTest {

        getArticlesUseCase = mock {
            onBlocking { getArticles() } doReturn object : LiveData<Result<List<Article>>>() {
                init {
                    value = Result.loading()
                }
            }
        }


        articleViewModel = ArticlesViewModel(getArticlesUseCase , getSingleArticleUseCase)

        val result = articleViewModel.articleResult

        result.observeForever {}


        kotlinx.coroutines.delay(2000)
        assert(LiveDataTestUtil.getValue(articleViewModel.articleResult).status == Status.LOADING)

    }


    @Test
    fun testSuccessData() = mainCoroutineRule.runBlockingTest {

        getArticlesUseCase = mock {
            onBlocking { getArticles() } doReturn object : LiveData<Result<List<Article>>>() {
                init {
                    value = Result.success(fakeArticles)
                }
            }
        }

        articleViewModel = ArticlesViewModel(getArticlesUseCase , getSingleArticleUseCase)

        val result = articleViewModel.articleResult

        result.observeForever {}

        kotlinx.coroutines.delay(2000)


        assert(
            LiveDataTestUtil.getValue(result).status == Status.SUCCESS &&
                LiveDataTestUtil.getValue(result).data == fakeArticles)

    }


    @Test
    fun testErrorData() = mainCoroutineRule.runBlockingTest {

        getArticlesUseCase = mock {
            onBlocking { getArticles() } doReturn object : LiveData<Result<List<Article>>>() {
                init {
                    value = Result.error("error")
                }
            }
        }

        articleViewModel = ArticlesViewModel(getArticlesUseCase , getSingleArticleUseCase)

        val result = articleViewModel.articleResult

        result.observeForever {}

        kotlinx.coroutines.delay(2000)


        assert(
            LiveDataTestUtil.getValue(result).status == Status.ERROR &&
                LiveDataTestUtil.getValue(result).message == "error")

    }


    @Test
    fun testFetchDataData() = mainCoroutineRule.runBlockingTest {

        getArticlesUseCase = mock {
            onBlocking { getArticles(true) } doReturn object : LiveData<Result<List<Article>>>() {
                init {
                    value = Result.error("error")
                }
            }

            onBlocking { getArticles(false) } doReturn object : LiveData<Result<List<Article>>>() {
                init {
                    value = Result.success(fakeArticles)
                }
            }
        }

        articleViewModel = ArticlesViewModel(getArticlesUseCase , getSingleArticleUseCase)

        val result = articleViewModel.articleResult

        result.observeForever {}


        assert(
            LiveDataTestUtil.getValue(result).status == Status.SUCCESS &&
                LiveDataTestUtil.getValue(result).data == fakeArticles)

        kotlinx.coroutines.delay(2000)

        articleViewModel.loadArticles(true)


        kotlinx.coroutines.delay(2000)


        assert(
            LiveDataTestUtil.getValue(result).status == Status.ERROR &&
                LiveDataTestUtil.getValue(result).message == "error")

    }*/



}