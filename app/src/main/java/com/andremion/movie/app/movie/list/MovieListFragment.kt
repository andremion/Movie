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

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andremion.movie.R
import com.andremion.movie.app.internal.util.lazyThreadSafetyNone
import com.andremion.movie.app.movie.list.databinding.MovieListAdapter
import com.andremion.movie.app.navigation.Navigator
import com.andremion.movie.databinding.FragmentMovieListBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MovieListFragment : DaggerFragment(), MovieListAdapter.Callback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navigator: Navigator

    private lateinit var binder: FragmentMovieListBinding

    private val viewModel by lazyThreadSafetyNone {
        activity?.let { ViewModelProviders.of(it, viewModelFactory).get(MovieListViewModel::class.java) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false)
        binder.viewModel = viewModel
        binder.listCallback = this
        return binder.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel?.findNowPlaying()
        }
    }

    override fun onItemClick(view: View, id: Long) {
        val cardView = view.findViewById<View>(R.id.cardview)
        val imageView = view.findViewById<View>(R.id.image_poster)
        val titleView = view.findViewById<View>(R.id.text_title)
        val releaseView = view.findViewById<View>(R.id.text_release)
        val overviewView = view.findViewById<View>(R.id.text_overview)
        val sharedViews = arrayOf(
                Pair(cardView, ViewCompat.getTransitionName(cardView)),
                Pair(imageView, ViewCompat.getTransitionName(imageView)),
                Pair(titleView, ViewCompat.getTransitionName(titleView)),
                Pair(releaseView, ViewCompat.getTransitionName(releaseView)),
                Pair(overviewView, ViewCompat.getTransitionName(overviewView)))
        activity?.let { navigator.navigateToMovie(it, id, sharedViews) }
    }

}