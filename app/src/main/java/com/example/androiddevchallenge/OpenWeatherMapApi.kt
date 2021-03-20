/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import com.example.androiddevchallenge.data.CurrentWeather
import com.example.androiddevchallenge.data.Forecast
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApi {
    @GET("weather?units=imperial")
    suspend fun getCurrentWeatherZip(@Query("zip") zip: String): CurrentWeather

    @GET("weather?units=imperial")
    suspend fun getCurrentWeatherCity(@Query("q") city: String): CurrentWeather

    @GET("forecast/daily?units=imperial")
    suspend fun getForcastCity(@Query("q") city: String): Forecast

    @GET("forecast/daily?units=imperial")
    suspend fun getForcastZip(@Query("zip") zip: String): Forecast
}

object NetworkModule {
    val api by lazy {
        val apikey = "e7db55f48e740b9c0a2045a2e2713e32"

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()

        okHttpClient.addInterceptor(logging)

        okHttpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            requestBuilder.url(original.url.toString() + "&appid=$apikey")
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .build().create(OpenWeatherMapApi::class.java)
    }
}
