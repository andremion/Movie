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

package com.andremion.movie.app.movie.list.databinding

import android.databinding.BindingAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.andremion.movie.R
import com.andremion.movie.data.entity.Movie
import java.util.*

object ViewBindingAdapters {

    @JvmStatic
    @BindingAdapter("listAdapter", "listCallback", requireAll = false)
    fun setMovieAdapter(recyclerView: RecyclerView, items: List<Movie>?, listCallback: MovieListAdapter.Callback?) {
        val adapter: MovieListAdapter
        if (recyclerView.adapter == null) {

            setupRecyclerView(recyclerView)

            adapter = MovieListAdapter(listCallback)
            recyclerView.adapter = adapter
        } else {
            adapter = recyclerView.adapter as MovieListAdapter
        }
        items?.let {
            adapter.submitList(
                    // Due to ListAdapter,
                    // this ensure the comparison occurs after the empty list have been set.
                    // So we call toList().
                    it.toList()
            )
        }
    }

    @JvmStatic
    @BindingAdapter("textColor")
    fun setTextColor(textView: TextView, release: String?) {
        if (release?.toInt() == Calendar.getInstance().get(Calendar.YEAR)) {
            val colorRelease = ContextCompat.getColor(textView.context, R.color.colorRelease)
            textView.setTextColor(colorRelease)
        } else {
            val colorAccent = ContextCompat.getColor(textView.context, R.color.colorAccent)
            textView.setTextColor(colorAccent)
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val numColumns = recyclerView.resources.getInteger(R.integer.grid_column_count)
        recyclerView.layoutManager = GridLayoutManager(recyclerView.context, numColumns)
        recyclerView.setHasFixedSize(true)
    }
}
