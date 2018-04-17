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

package com.andremion.movie.app.internal.util

import android.widget.SearchView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

object ObservableSearchView {

    private const val threshold = 3
    private const val timeout = 500L

    fun fromView(searchView: SearchView): Observable<String> {

        val subject: Subject<String> = PublishSubject.create()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                subject.onComplete()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                subject.onNext(newText)
                return true
            }
        })

        return subject
                .filter { it.isEmpty() || it.length >= threshold }
                .debounce(timeout, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
    }
}