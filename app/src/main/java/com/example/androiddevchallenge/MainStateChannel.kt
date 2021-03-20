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

import kotlin.math.floor

sealed class MainUserIntents {
    object Loading : MainUserIntents()
    object GetWeatherZip : MainUserIntents()
    object GetWeatherCity : MainUserIntents()
    class UpdateZip(val zip: String) : MainUserIntents()
    class UpdateCity(val city: String) : MainUserIntents()
}

class MainStateChannel : StateChannel<MainViewState, MainUserIntents>(MainViewState()) {
    private val api = WeatherOvertureRepository()

    override suspend fun reducer(userIntent: MainUserIntents): MainViewState =
        when (userIntent) {
            MainUserIntents.GetWeatherCity -> {
                if (_state.value.inputText.isNotEmpty()) {
                    val weather = api.getWeatherCity(_state.value.inputText)
                    val forecast = api.getForecastCity(_state.value.inputText)
                    _state.value.copy(
                        city = weather.name,
                        temp = floor(weather.main.temp).toInt().toString(),
                        iconId = weather.weather.getOrNull(0)?.icon ?: 800.toString(),
                        loading = false,
                        forecast = forecast.list
                    )
                } else _state.value.copy(
                    loading = true,
                    city = "",
                    temp = "",
                    iconId = "",
                    forecast = emptyList()
                )
            }
            MainUserIntents.GetWeatherZip -> {
                if (_state.value.inputText.isNotEmpty()) {
                    val weather = api.getWeatherZip(_state.value.inputText)
                    val forecast = api.getForcastZip(_state.value.inputText)
                    _state.value.copy(
                        city = weather.name,
                        iconId = weather.weather.getOrNull(0)?.icon ?: 800.toString(),
                        temp = floor(weather.main.temp).toInt().toString(),
                        loading = false,
                        forecast = forecast.list
                    )
                } else _state.value.copy(
                    loading = true,
                    city = "",
                    temp = "",
                    iconId = "",
                    forecast = emptyList()
                )
            }
            MainUserIntents.Loading -> _state.value.copy(loading = true)
            is MainUserIntents.UpdateCity -> {
                _state.value.copy(inputText = userIntent.city, inputIsZip = false)
            }
            is MainUserIntents.UpdateZip -> {
                _state.value.copy(inputText = userIntent.zip, inputIsZip = true)
            }
        }
}
