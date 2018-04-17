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

package com.andremion.movie.data.remote.api

import com.andremion.movie.data.remote.api.response.ListResponse
import com.andremion.movie.data.remote.model.ConfigRemote
import com.andremion.movie.data.remote.model.MovieRemote
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("configuration")
    fun getConfiguration(): Single<ConfigRemote>

    @GET("movie/now_playing")
    fun findNowPlayingMovies(): Single<ListResponse<MovieRemote>>

    @GET("search/movie")
    fun findMoviesByText(@Query("query") text: String): Single<ListResponse<MovieRemote>>

    @GET("movie/{id}")
    fun getMovieById(@Path("id") id: Long): Single<MovieRemote>
}
