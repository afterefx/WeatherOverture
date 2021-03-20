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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import com.example.androiddevchallenge.Weather.CloudyNight
import com.example.androiddevchallenge.Weather.Mist
import com.example.androiddevchallenge.Weather.Night
import com.example.androiddevchallenge.Weather.PartlyCloudy
import com.example.androiddevchallenge.Weather.RainDay
import com.example.androiddevchallenge.Weather.RainNight
import com.example.androiddevchallenge.Weather.Snow
import com.example.androiddevchallenge.Weather.SnowNight
import com.example.androiddevchallenge.Weather.SnowSunny
import com.example.androiddevchallenge.Weather.Sunny
import com.example.androiddevchallenge.Weather.Thunder
import com.example.androiddevchallenge.Weather.ThunderStormDay
import com.example.androiddevchallenge.Weather.Thunderstorms
import com.example.androiddevchallenge.Weather.Windy

enum class Weather {
    CloudyNight,
    Mist,
    Night,
    PartlyCloudy,
    RainDay,
    RainNight,
    Snow,
    SnowNight,
    SnowSunny,
    Sunny,
    Thunder,
    ThunderStormDay,
    Thunderstorms,
    Windy,
}

/**
 * Thanks to https://lottiefiles.com/MarsG for the public
 * lottie assets
 */
@Composable
fun WeatherIcon(weather: Weather, modifier: Modifier = Modifier) {
    val url = when (weather) {
        CloudyNight -> "https://assets10.lottiefiles.com/temp/lf20_Jj2Qzq.json"
        Mist -> "https://assets10.lottiefiles.com/temp/lf20_kOfPKE.json"
        Night -> "https://assets10.lottiefiles.com/temp/lf20_y6mY2A.json"
        PartlyCloudy -> "https://assets10.lottiefiles.com/temp/lf20_dgjK9i.json"
        RainDay -> "https://assets10.lottiefiles.com/temp/lf20_rpC1Rd.json"
        RainNight -> "https://assets10.lottiefiles.com/temp/lf20_I5XMi9.json"
        Snow -> "https://assets10.lottiefiles.com/temp/lf20_WtPCZs.json"
        SnowNight -> "https://assets10.lottiefiles.com/temp/lf20_RHbbn6.json"
        SnowSunny -> "https://assets10.lottiefiles.com/temp/lf20_BSVgyt.json"
        Sunny -> "https://assets10.lottiefiles.com/temp/lf20_Stdaec.json"
        Thunder -> "https://assets10.lottiefiles.com/temp/lf20_Kuot2e.json"
        ThunderStormDay -> "https://assets10.lottiefiles.com/temp/lf20_JA7Fsb.json"
        Thunderstorms -> "https://assets10.lottiefiles.com/temp/lf20_XkF78Y.json"
        Windy -> "https://assets10.lottiefiles.com/temp/lf20_VAmWRg.json"
    }
    val animationState = rememberLottieAnimationState(
        autoPlay = true,
        repeatCount = Integer.MAX_VALUE
    )
    LottieAnimation(
        spec = remember(url) { LottieAnimationSpec.Url(url = url) },
        animationState = animationState,
        modifier = modifier
    )
}
