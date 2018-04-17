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

package com.andremion.movie.app.movie.list

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.andremion.movie.app.internal.DisposableViewModel
import com.andremion.movie.app.internal.util.getErrorMessage
import com.andremion.movie.data.entity.Movie
import com.andremion.movie.data.repository.MovieRepository
import com.andremion.movie.scheduler.AppSchedulers
import io.reactivex.Observable

class MovieListViewModel(
        private val appSchedulers: AppSchedulers,
        private val movieRepository: MovieRepository) : DisposableViewModel() {

    val loading = ObservableBoolean()
    val data = ObservableArrayList<Movie>()
    val empty = ObservableBoolean()
    val error = ObservableField<String>()

    fun findNowPlaying() {
        addDisposable(
                movieRepository.findNowPlayingMovies()
                        .observeOn(appSchedulers.main)
                        .doOnSubscribe { notifyLoading() }
                        .subscribe(onSuccess(), onError())
        )
    }

    fun findMovieByText(search: Observable<String>) {
        addDisposable(search
                .observeOn(appSchedulers.io)
                .switchMap(findMovies())
                .observeOn(appSchedulers.main)
                .subscribe(onSuccess(), onError())
        )
    }

    /**
     * If we have an empty text we are going to find now playing movies.
     * Otherwise, we will find movies by that text.
     */
    private fun findMovies(): (String) -> Observable<List<Movie>> {
        return { text ->
            val observable =
                    if (text.isEmpty()) {
                        movieRepository
                                .findNowPlayingMovies()
                                .toObservable()
                    } else {
                        movieRepository
                                .findByText(text)
                                .toObservable()
                    }
            observable
                    .doOnSubscribe { notifyLoading() }
                    .doOnError { notifyError(it) }
                    .onErrorReturn { listOf() } // On error we are going to clear the list
        }
    }

    private fun notifyLoading() {
        loading.set(true)
    }

    private fun onSuccess(): (List<Movie>) -> Unit {
        return { d ->
            loading.set(false)
            data.clear()
            data.addAll(d)
            empty.set(d.isEmpty())
        }
    }

    private fun onError(): (Throwable) -> Unit {
        return { e -> notifyError(e) }
    }

    private fun notifyError(e: Throwable) {
        loading.set(false)
        error.set(getErrorMessage(e))
    }

}
