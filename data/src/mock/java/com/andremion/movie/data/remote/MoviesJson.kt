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

import com.andremion.movie.data.remote.api.response.ListResponse
import com.andremion.movie.data.remote.model.MovieRemote
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MoviesJson {

    private const val NOW_PLAYING_MOVIES_JSON_FILE = "response/now_playing_movies.json"
    private const val MOVIES_BY_TEXT_JSON_FILE = "response/movies_by_text.json"
    private const val MOVIE_DETAILS_JSON_FILE = "response/movie_details.json"

    private val gson = Gson()

    fun getNowPlayingMoviesObjects(): ListResponse<MovieRemote> = getObjects(NOW_PLAYING_MOVIES_JSON_FILE)

    fun getMoviesByTextObjects(): ListResponse<MovieRemote> = getObjects(MOVIES_BY_TEXT_JSON_FILE)

    fun getMovieDetailsObject(): MovieRemote = getObject(MOVIE_DETAILS_JSON_FILE)

    private fun getObjects(file: String): ListResponse<MovieRemote> {
        val json = getJson(file)
        val type = object : TypeToken<ListResponse<MovieRemote>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun getObject(file: String): MovieRemote {
        val json = getJson(file)
        return gson.fromJson(json, MovieRemote::class.java)
    }

    fun getNowPlayingMoviesJson() = getJson(NOW_PLAYING_MOVIES_JSON_FILE)

    fun getMoviesByTextJson() = getJson(MOVIES_BY_TEXT_JSON_FILE)

    fun getMovieDetailsJson() = getJson(MOVIE_DETAILS_JSON_FILE)

    private fun getJson(file: String): String {
        val stream = javaClass.classLoader.getResourceAsStream(file)
        return stream.bufferedReader().use { it.readText() }
    }

    fun getEmptyObjects(): ListResponse<MovieRemote> {
        return ListResponse(1, 0, 1, listOf())
    }

    fun getEmptyJson(): String {
        val type = object : TypeToken<ListResponse<MovieRemote>>() {}.type
        return gson.toJson(getEmptyObjects(), type)
    }
}