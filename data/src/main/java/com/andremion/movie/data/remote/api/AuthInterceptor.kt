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

package com.andremion.movie.data.remote.api

import com.andremion.movie.data.BuildConfig
import io.reactivex.exceptions.Exceptions
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val currentRequest = chain.request()
        val currentUrl = currentRequest.url()

        val url = currentUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .build()

        val newRequest = currentRequest.newBuilder()
                .url(url)
                .build()

        // Error handling in RxJava
        // http://blog.danlew.net/2015/12/08/error-handling-in-rxjava/
        try {
            return chain.proceed(newRequest)
        } catch (e: IOException) {
            // Transform checked Exception in Unchecked Exception
            throw Exceptions.propagate(e)
        }
    }
}