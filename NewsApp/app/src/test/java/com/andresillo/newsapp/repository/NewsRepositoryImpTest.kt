package com.andresillo.newsapp.repository

import com.andresillo.newsapp.db.NewsDao
import com.andresillo.newsapp.db.NewsEntity
import com.andresillo.newsapp.model.News
import com.andresillo.newsapp.provider.NewsProvider
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsRepositoryImpTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var provider: NewsProvider
    private lateinit var fakeDao: FakeNewsDao
    private lateinit var repository: NewsRepositoryImp

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        provider = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsProvider::class.java)

        fakeDao = FakeNewsDao()
        repository = NewsRepositoryImp(provider, fakeDao)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getNews retorna articulos cuando la respuesta es exitosa`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(
                    """
                    {
                      "status": "ok",
                      "totalResults": 1,
                      "articles": [
                        {
                          "title": "Test Title",
                          "content": "Test content",
                          "author": "Test Author",
                          "url": "https://example.com",
                          "urlToImage": "https://example.com/image.jpg"
                        }
                      ]
                    }
                    """.trimIndent()
                )
        )

        val result = repository.getNews("us")

        assertEquals(1, result.size)
        assertEquals("Test Title", result[0].title)
    }

    @Test
    fun `getNews retorna cache cuando la respuesta falla`() = runTest {
        fakeDao.insertAll(
            listOf(
                NewsEntity(
                    title = "Cached Title",
                    content = null,
                    author = null,
                    url = "https://example.com",
                    urlToImage = ""
                )
            )
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(401)
                .setBody("""{"status":"error","code":"apiKeyMissing"}""")
        )

        val result = repository.getNews("us")

        assertEquals(1, result.size)
        assertEquals("Cached Title", result[0].title)
    }
}

class FakeNewsDao : NewsDao {
    private val storage = mutableListOf<NewsEntity>()

    override suspend fun insertAll(news: List<NewsEntity>) {
        storage.addAll(news)
    }

    override suspend fun getAll(): List<NewsEntity> = storage.toList()

    override fun getByTitle(title: String): NewsEntity? =
        storage.firstOrNull { it.title == title }
}
