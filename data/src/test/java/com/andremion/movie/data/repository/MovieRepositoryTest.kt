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

package com.andremion.movie.data.repository

import com.andremion.movie.MockitoKotlinSolver
import com.andremion.movie.data.local.ConfigLocalDataSource
import com.andremion.movie.data.remote.MovieRemoteDataSource
import com.andremion.movie.data.remote.model.ConfigImageRemote
import com.andremion.movie.data.remote.model.ConfigRemote
import com.andremion.movie.data.repository.mapper.ConfigMapper
import com.andremion.movie.data.repository.mapper.MovieMapper
import com.andremion.movie.scheduler.TestAppSchedulers
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@Suppress("RemoveRedundantBackticks")
@RunWith(MockitoJUnitRunner::class)
class MovieRepositoryTest {

    @Mock
    private lateinit var configLocalDataSource: ConfigLocalDataSource
    @Mock
    private lateinit var movieRemoteDataSource: MovieRemoteDataSource

    private lateinit var movieRepository: MovieRepository

    @Before
    fun setUp() {
        movieRepository = MovieRepository(TestAppSchedulers(),
                configLocalDataSource, movieRemoteDataSource,
                ConfigMapper(), MovieMapper())
    }

    @Test
    @Throws(Exception::class)
    fun `Given empty local data and remote data, When call init configuration, Should have configuration saved locally`() {

        // Given

        // Empty local data
        val id = 1
        `when`(configLocalDataSource.getById(id)).thenReturn(Maybe.empty())
        // Remote data
        val configImageRemote = ConfigImageRemote("url", "secure_url",
                listOf("200", "original"), listOf("400", "original"))
        val configRemote = ConfigRemote(configImageRemote)
        `when`(movieRemoteDataSource.getConfiguration()).thenReturn(Single.just(configRemote))

        // When

        val testObserver = movieRepository.initConfiguration()
                .test()

        // Then

        testObserver
                .assertComplete()
                .assertNoErrors()
                .assertValue(true)

        verify(configLocalDataSource).save(MockitoKotlinSolver.any())
    }

    @Test
    @Throws(Exception::class)
    fun `Given empty local data and error on remote data source, When call init configuration, Should get error`() {

        // Given

        // Empty local data
        val id = 1
        `when`(configLocalDataSource.getById(id)).thenReturn(Maybe.empty())
        // Remote error
        val error = IOException()
        `when`(movieRemoteDataSource.getConfiguration()).thenReturn(Single.error(error))

        // When

        val testObserver = movieRepository.initConfiguration()
                .test()

        // Then

        testObserver.assertError(error)

        verify(configLocalDataSource, never()).save(MockitoKotlinSolver.any())
    }

}