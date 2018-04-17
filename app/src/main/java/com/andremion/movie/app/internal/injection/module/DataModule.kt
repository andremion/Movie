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

package com.andremion.movie.app.internal.injection.module

import android.content.Context
import com.andremion.movie.data.BuildConfig
import com.andremion.movie.data.local.ConfigLocalDataSource
import com.andremion.movie.data.local.database.LocalDatabase
import com.andremion.movie.data.remote.MovieRemoteDataSource
import com.andremion.movie.data.remote.api.MovieApi
import com.andremion.movie.data.remote.api.MovieService
import com.andremion.movie.data.repository.MovieRepository
import com.andremion.movie.data.repository.mapper.ConfigMapper
import com.andremion.movie.data.repository.mapper.MovieMapper
import com.andremion.movie.scheduler.AppSchedulers
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class DataModule {

    @Provides
    @Singleton
    internal fun provideMovieService(): MovieService = MovieApi(BuildConfig.API_URL)

    @Provides
    @Singleton
    internal fun provideLocalDatabase(context: Context): LocalDatabase {
        return LocalDatabase.newInstance(context)
    }

    @Provides
    @Singleton
    internal fun provideConfigLocalDataSource(localDatabase: LocalDatabase): ConfigLocalDataSource {
        return ConfigLocalDataSource(localDatabase)
    }

    @Provides
    @Singleton
    internal fun provideMovieRemoteDataSource(movieService: MovieService): MovieRemoteDataSource {
        return MovieRemoteDataSource(movieService)
    }

    @Provides
    @Singleton
    internal fun provideMovieRepository(appSchedulers: AppSchedulers,
                                        configLocalDataSource: ConfigLocalDataSource,
                                        movieRemoteDataSource: MovieRemoteDataSource): MovieRepository {
        return MovieRepository(appSchedulers, configLocalDataSource, movieRemoteDataSource, ConfigMapper(), MovieMapper())
    }

}
