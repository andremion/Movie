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

import com.andremion.movie.data.entity.Movie
import com.andremion.movie.data.local.ConfigLocalDataSource
import com.andremion.movie.data.local.model.ConfigImageLocal
import com.andremion.movie.data.remote.MovieRemoteDataSource
import com.andremion.movie.data.repository.mapper.ConfigMapper
import com.andremion.movie.data.repository.mapper.MovieMapper
import com.andremion.movie.scheduler.AppSchedulers
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class MovieRepository(
        private val appSchedulers: AppSchedulers,
        private val configLocalDataSource: ConfigLocalDataSource,
        private val movieRemoteDataSource: MovieRemoteDataSource,
        private val configMapper: ConfigMapper,
        private val movieMapper: MovieMapper) {

    private val configId = 1

    fun initConfiguration(): Single<Boolean> {
        return configLocalDataSource.getById(configId)
                .switchIfEmpty(
                        movieRemoteDataSource.getConfiguration()
                                .map { configMapper.fromRemoteToLocal(configId, it.images) }
                                .doOnSuccess { configLocalDataSource.save(it) }
                                .toMaybe()
                )
                .toSingle()
                .map { true }
                .subscribeOn(appSchedulers.io)
    }

    fun findNowPlayingMovies(): Single<List<Movie>> {

        return getConfigThen(movieRemoteDataSource.findNowPlayingMovies(),
                { config, movies ->
                    movieMapper.fromRemoteToEntity(config, movies.results)
                })
    }


    fun findByText(text: String): Single<List<Movie>> {

        return getConfigThen(movieRemoteDataSource.findMoviesByText(text),
                { config, result ->
                    movieMapper.fromRemoteToEntity(config, result.results)
                })
    }

    fun getById(id: Long): Single<Movie> {

        return getConfigThen(movieRemoteDataSource.getMovieById(id),
                { config, result ->
                    movieMapper.fromRemoteToEntity(config, result)
                })
    }

    private fun <T, R> getConfigThen(single: Single<T>, mapping: (ConfigImageLocal, T) -> R): Single<R> {
        return configLocalDataSource.getById(configId)
                .switchIfEmpty(Maybe.error(IllegalStateException("Configuration is empty")))
                .toSingle()
                .zipWith(single,
                        BiFunction<ConfigImageLocal, T, R> { config, result ->
                            mapping(config, result)
                        }
                )
                .subscribeOn(appSchedulers.io)
    }
}
