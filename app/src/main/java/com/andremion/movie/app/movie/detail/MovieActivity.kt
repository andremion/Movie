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

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.andremion.movie.R
import com.andremion.movie.app.internal.util.lazyThreadSafetyNone
import com.andremion.movie.app.navigation.Navigator
import com.andremion.movie.databinding.ActivityMovieBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MovieActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navigator: Navigator

    private val binder by lazyThreadSafetyNone<ActivityMovieBinding> {
        DataBindingUtil.setContentView(this, R.layout.activity_movie)
    }

    private val viewModel by lazyThreadSafetyNone {
        ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportPostponeEnterTransition()

        setSupportActionBar(binder.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binder.viewModel = viewModel

        val movie = navigator.getMovie(this)
        viewModel.loadMovieDetail(movie)

        viewModel.data.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable, propertyId: Int) {
                viewModel.data.removeOnPropertyChangedCallback(this)
                startTransition()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startTransition() {
        (window.decorView as ViewGroup).viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        (window.decorView as ViewGroup).viewTreeObserver.removeOnPreDrawListener(this)
                        supportStartPostponedEnterTransition()
                        return true
                    }
                }
        )
    }

}
