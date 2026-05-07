package com.andresillo.newsapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andresillo.newsapp.repository.NewsRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ExampleHiltAndroidTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: NewsRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun hiltGraphBuildsCorrectly() {
        assert(::repository.isInitialized)
    }
}
