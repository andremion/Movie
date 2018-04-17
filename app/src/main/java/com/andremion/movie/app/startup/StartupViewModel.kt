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

package com.andremion.movie.app.startup

import android.arch.lifecycle.LiveData
import com.andremion.movie.app.internal.DisposableViewModel
import com.andremion.movie.app.internal.util.SingleLiveData
import com.andremion.movie.app.internal.util.getErrorMessage
import com.andremion.movie.data.repository.MovieRepository
import com.andremion.movie.scheduler.AppSchedulers

class StartupViewModel(
        private val appSchedulers: AppSchedulers,
        private val movieRepository: MovieRepository) : DisposableViewModel() {

    private val _result = SingleLiveData<Boolean>()
    val result: LiveData<Boolean> = _result
    private val _error = SingleLiveData<String>()
    val error: LiveData<String> = _error

    fun startup() = addDisposable(
            movieRepository.initConfiguration()
                    .observeOn(appSchedulers.main)
                    .subscribe(success(), error())
    )

    private fun success(): (Boolean) -> Unit {
        return { d ->
            _result.value = d
        }
    }

    private fun error(): (Throwable) -> Unit {
        return { e ->
            _error.value = getErrorMessage(e)
        }
    }

}
