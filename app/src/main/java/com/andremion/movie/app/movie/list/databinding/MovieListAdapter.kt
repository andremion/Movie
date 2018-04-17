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

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andremion.movie.BR
import com.andremion.movie.R
import com.andremion.movie.data.entity.Movie

class MovieListAdapter(private val callback: Callback? = null) : ListAdapter<Movie, MovieListAdapter.ViewHolder>(diffCallback) {

    interface Callback {
        fun onItemClick(view: View, id: Long)
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        val item = getItem(position)
        return item?.id ?: super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.fragment_movie_list_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent.context, viewType, parent, callback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let(holder::bindTo)
    }

    class ViewHolder(private val binding: ViewDataBinding, private val callback: Callback?) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener { callback?.onItemClick(it, itemId) }
        }

        companion object {
            fun create(context: Context, layout: Int, parent: ViewGroup, callback: Callback?): ViewHolder {
                val inflater = LayoutInflater.from(context)
                val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, layout, parent, false)
                return ViewHolder(binding, callback)
            }
        }

        fun bindTo(item: Movie?) {
            binding.setVariable(BR.movie, item)
            binding.executePendingBindings()
        }
    }

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie?, newItem: Movie?): Boolean {
                return oldItem?.id == newItem?.id
            }

            override fun areContentsTheSame(oldItem: Movie?, newItem: Movie?): Boolean {
                return oldItem?.equals(newItem) ?: false
            }
        }
    }
}
