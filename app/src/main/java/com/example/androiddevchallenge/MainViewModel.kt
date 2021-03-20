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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.data.Day
import kotlinx.coroutines.launch

data class MainViewState(
    val inputText: String = "",
    val inputIsZip: Boolean = false,
    val loading: Boolean = true,
    val city: String = "",
    val temp: String = "",
    val iconId: String = "",
    val forecast: List<Day> = emptyList()
)

class MainViewModel : ViewModel() {

    private val mainStateChannel = MainStateChannel()

    // observe in view
    val state = mainStateChannel.state

    // send messages on this
    val userIntentChannel = mainStateChannel.userIntentChannel

    init {
        viewModelScope.launch {
            mainStateChannel.handleIntents()
        }
    }
}
