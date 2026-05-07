package com.andresillo.newsapp.viewmodel

import com.andresillo.newsapp.model.News
import com.andresillo.newsapp.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState pasa a Success cuando el repositorio devuelve noticias`() = runTest {
        val fakeRepo = object : NewsRepository {
            override suspend fun getNews(country: String) = listOf(
                News(
                    title = "Título de prueba",
                    content = "Contenido",
                    author = "Autor",
                    url = "https://example.com",
                    urlToImage = ""
                )
            )
            override fun getNew(title: String) = News(
                title = title, content = null, author = null, url = "", urlToImage = ""
            )
        }

        val viewModel = ListScreenViewModel(fakeRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is ListUiState.Success)
        assertEquals(1, (state as ListUiState.Success).news.size)
    }

    @Test
    fun `uiState pasa a Error cuando el repositorio lanza excepcion`() = runTest {
        val fakeRepo = object : NewsRepository {
            override suspend fun getNews(country: String): List<News> =
                throw RuntimeException("Sin red")
            override fun getNew(title: String) = News(
                title = title, content = null, author = null, url = "", urlToImage = ""
            )
        }

        val viewModel = ListScreenViewModel(fakeRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is ListUiState.Error)
        assertEquals("Sin red", (state as ListUiState.Error).message)
    }
}
