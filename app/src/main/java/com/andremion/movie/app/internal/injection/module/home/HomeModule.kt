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

package com.andremion.movie.app.internal.injection.module.home

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.andremion.movie.app.internal.injection.scope.HomeScope
import com.andremion.movie.app.movie.list.MovieListFragment
import com.andremion.movie.app.movie.list.MovieListViewModel
import com.andremion.movie.data.repository.MovieRepository
import com.andremion.movie.scheduler.AppSchedulers
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class HomeModule {

    @ContributesAndroidInjector
    internal abstract fun movieListFragment(): MovieListFragment

    @Module
    companion object {

        @HomeScope
        @Provides
        @JvmStatic
        internal fun provideViewModelFactory(appSchedulers: AppSchedulers,
                                             movieRepository: MovieRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return when {
                        modelClass.isAssignableFrom(MovieListViewModel::class.java) ->
                            MovieListViewModel(appSchedulers, movieRepository) as T

                        else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                    }
                }
            }
        }
    }
}
