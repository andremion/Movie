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

import com.andremion.movie.data.BuildConfig
import com.andremion.movie.data.remote.api.adapter.DateDeserializer
import com.andremion.movie.data.remote.api.response.ListResponse
import com.andremion.movie.data.remote.model.ConfigRemote
import com.andremion.movie.data.remote.model.MovieRemote
import com.google.gson.GsonBuilder
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class MovieApi(baseUrl: String) : MovieService {
    private val service: MovieService

    init {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) BODY else BASIC

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(AuthInterceptor())

        val client = httpClient.build()

        val gson = GsonBuilder()
                .registerTypeAdapter(Date::class.java, DateDeserializer())
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()

        service = retrofit.create(MovieService::class.java)
    }

    override fun getConfiguration(): Single<ConfigRemote> = service.getConfiguration()

    override fun findNowPlayingMovies(): Single<ListResponse<MovieRemote>> = service.findNowPlayingMovies()

    override fun findMoviesByText(text: String): Single<ListResponse<MovieRemote>> = service.findMoviesByText(text)

    override fun getMovieById(id: Long): Single<MovieRemote> = service.getMovieById(id)
}
