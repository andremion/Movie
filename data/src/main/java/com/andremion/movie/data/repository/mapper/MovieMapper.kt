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

package com.andremion.movie.data.repository.mapper

import com.andremion.movie.data.entity.Movie
import com.andremion.movie.data.local.model.ConfigImageLocal
import com.andremion.movie.data.remote.model.MovieRemote
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MovieMapper {

    fun fromRemoteToEntity(configImageLocal: ConfigImageLocal, movies: List<MovieRemote>): List<Movie> {
        return movies.map { movieRemote ->
            fromRemoteToEntity(configImageLocal, movieRemote)
        }
    }

    fun fromRemoteToEntity(configImageLocal: ConfigImageLocal, movieRemote: MovieRemote): Movie {
        return with(configImageLocal) {
            with(movieRemote) {
                Movie(id = movieRemote.id,
                        title = title,
                        release = parseRelease(release_date),
                        poster = "$secureBaseUrl$posterSize/$poster_path",
                        backdrop = "$secureBaseUrl$backdropSize/$backdrop_path",
                        voteCount = vote_count.toString(),
                        voteAverage = parseAverage(vote_average),
                        overview = overview,
                        revenue = parseRevenue(revenue),
                        runtime = parseRuntime(runtime)
                )
            }
        }
    }

    private fun parseRelease(release: Date?): String? {
        return release?.let {
            SimpleDateFormat("yyyy", Locale.getDefault())
                    .format(release)
        }
    }

    private fun parseAverage(average: Float) = "%.0f%%".format(average * 10)

    private fun parseRevenue(revenue: Long?): String? {
        return revenue?.let { NumberFormat.getCurrencyInstance().format(it) }
    }

    private fun parseRuntime(runtime: Int?): String? {
        return runtime?.let {
            val hours = it / 60
            val minutes = it % 60
            "%dh %02dm".format(hours, minutes)
        }
    }
}