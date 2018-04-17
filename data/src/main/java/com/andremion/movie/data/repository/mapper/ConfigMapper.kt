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

package com.andremion.movie.data.repository.mapper

import com.andremion.movie.data.local.model.ConfigImageLocal
import com.andremion.movie.data.remote.model.ConfigImageRemote

class ConfigMapper {

    fun fromRemoteToLocal(id: Int, configImageRemote: ConfigImageRemote): ConfigImageLocal {
        with(configImageRemote) {
            return ConfigImageLocal(id = id,
                    baseUrl = base_url,
                    secureBaseUrl = secure_base_url,
                    posterSize = poster_sizes[2], // TODO: Get poster size according to client device
                    backdropSize = backdrop_sizes[2] // TODO: Get backdrop size according to client device
            )
        }
    }
}