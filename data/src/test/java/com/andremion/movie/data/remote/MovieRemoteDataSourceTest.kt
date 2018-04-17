/*
 * Copyright (c) 2018. André Mion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andremion.movie.data.remote

import com.andremion.movie.data.remote.api.MovieApi
import com.andremion.movie.data.remote.api.MovieService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@Suppress("RemoveRedundantBackticks")
@RunWith(JUnit4::class)
class MovieRemoteDataSourceTest {

    private lateinit var mockWebServer: MockWebServer

    private lateinit var movieService: MovieService

    private lateinit var movieRemoteDataSource: MovieRemoteDataSource

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        movieService = MovieApi(mockWebServer.url("/").toString())

        movieRemoteDataSource = MovieRemoteDataSource(movieService)
    }

    @After
    fun stopServer() {
        mockWebServer.shutdown()
    }

    @Test
    @Throws(Exception::class)
    fun `Given now playing movies json data, When we call find now playing movies, Should get those movies`() {

        // Given

        val movies = MoviesJson.getNowPlayingMoviesObjects()
        val response = MockResponse().setBody(MoviesJson.getNowPlayingMoviesJson())
        mockWebServer.enqueue(response)

        // When

        val testObserver = movieRemoteDataSource.findNowPlayingMovies()
                .test()

        // Then

        testObserver
                .assertComplete()
                .assertNoErrors()
                .assertValue(movies)
    }

    @Test
    @Throws(Exception::class)
    fun `Given movies by text json data, When we call find movies by text, Should get those movies`() {

        // Given

        val text = "iron"
        val movies = MoviesJson.getMoviesByTextObjects()
        val response = MockResponse().setBody(MoviesJson.getMoviesByTextJson())
        mockWebServer.enqueue(response)

        // When

        val testObserver = movieRemoteDataSource.findMoviesByText(text)
                .test()

        // Then

        testObserver
                .assertComplete()
                .assertNoErrors()
                .assertValue(movies)
    }

    @Test
    @Throws(Exception::class)
    fun `Given movie details json data, When we call get movie by id, Should get those data`() {

        // Given

        val id = 1726L
        val movie = MoviesJson.getMovieDetailsObject()
        val response = MockResponse().setBody(MoviesJson.getMovieDetailsJson())
        mockWebServer.enqueue(response)

        // When

        val testObserver = movieRemoteDataSource.getMovieById(id)
                .test()

        // Then

        testObserver
                .assertComplete()
                .assertNoErrors()
                .assertValue(movie)
    }

}