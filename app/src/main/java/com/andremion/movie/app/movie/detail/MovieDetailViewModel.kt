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

package com.andremion.movie.app.movie.detail

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.andremion.movie.app.internal.DisposableViewModel
import com.andremion.movie.data.entity.Movie
import com.andremion.movie.data.repository.MovieRepository
import com.andremion.movie.scheduler.AppSchedulers


class MovieDetailViewModel(
        private val appSchedulers: AppSchedulers,
        private val movieRepository: MovieRepository) : DisposableViewModel() {

    val loading = ObservableBoolean()
    val data = ObservableField<Movie>()
    val error = ObservableField<String>()

    fun loadMovieDetail(id: Long) {
        addDisposable(
                movieRepository.getById(id)
                        .observeOn(appSchedulers.main)
                        .doOnSubscribe { loading() }
                        .subscribe(success(), error()))
    }

    private fun loading() {
        loading.set(true)
    }

    private fun success(): (Movie) -> Unit {
        return { d ->
            loading.set(false)
            data.set(d)
        }
    }

    private fun error(): (Throwable) -> Unit {
        return { e ->
            loading.set(false)
            error.set(e.message)
        }
    }

}
