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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.data.Day
import com.example.androiddevchallenge.data.FeelsLike
import com.example.androiddevchallenge.data.Temp
import com.example.androiddevchallenge.data.WeatherX
import com.example.androiddevchallenge.ui.theme.MyTheme
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()
        setContent {
            val vm: MainViewModel = viewModel()
            val viewState by vm.state.collectAsState()
            ProvideWindowInsets {
                MyTheme {
                    val keyboardController = LocalSoftwareKeyboardController.current
                    MyApp(
                        viewState = viewState,
                        modifier = Modifier.statusBarsPadding(),
                        onDone = {
                            val isCity = viewState.inputText.toIntOrNull() == null
                            if (isCity)
                                action(UiEvent.RetrieveWeatherCity, vm)
                            keyboardController?.hideSoftwareKeyboard()
                        }
                    ) {
                        val isZip = it.toIntOrNull() != null
                        if (it.length <= 5 && isZip)
                            action(UiEvent.TypeZip(it), vm)
                        else
                            action(UiEvent.TypeCity(it), vm)
                        if (it.length == 5 && isZip)
                            action(UiEvent.RetrieveWeatherZip, vm)
                    }
                }
            }
        }
    }
}

// Start building your app here!
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MyApp(
    viewState: MainViewState,
    modifier: Modifier = Modifier,
    onDone: KeyboardActionScope.() -> Unit = {},
    onValueChange: (String) -> Unit = {}
) {
    val brush = Brush.radialGradient(
        listOf(
            MaterialTheme.colors.primaryVariant,
            MaterialTheme.colors.primary,
        ),
        center = Offset(-800f, -800f),
        radius = 2900f
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = brush
            )
    ) {
        ConstraintLayout(
            modifier = modifier.fillMaxSize(),
        ) {
            val (loading, zipInput, city, temp, degree, icon, forecast) = createRefs()

            SearchInput(
                viewState.inputText,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .constrainAs(zipInput) {
                        top.linkTo(parent.top, 16.dp)
                    },
                onValueChange = onValueChange,
                onDone = onDone
            )

            androidx.compose.animation.AnimatedVisibility(
                viewState.loading,
                modifier = Modifier
                    .constrainAs(loading) {
                        top.linkTo(zipInput.bottom, 90.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxSize(),
                exit = fadeOut(),
                enter = fadeIn()
            ) {
                LoadingScreen()
            }

            AnimatedVisibility(
                visible = viewState.city.isNotEmpty(),
                modifier = Modifier
                    .constrainAs(city) {
                        top.linkTo(zipInput.bottom, 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            ) {
                Text(
                    text = viewState.city,
                    style = MaterialTheme.typography.h5
                )
            }

            AnimatedVisibility(
                visible = viewState.temp.isNotEmpty(),
                modifier = Modifier
                    .constrainAs(temp) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(city.bottom)
                    },
            ) {
                Text(
                    viewState.temp,
                    style = MaterialTheme.typography.h2,
                    textAlign = TextAlign.Center
                )
            }

            AnimatedVisibility(
                visible = viewState.temp.isNotEmpty(),
                modifier = Modifier
                    .constrainAs(degree) {
                        start.linkTo(temp.end)
                        top.linkTo(temp.top)
                    },
            ) {
                Text(
                    "°",
                    style = MaterialTheme.typography.h2,
                    textAlign = TextAlign.Center
                )
            }

            AnimatedVisibility(
                visible = viewState.temp.isNotEmpty(),
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(parent.top, 200.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                WeatherIcon(
                    weather = viewState.iconId.toWeatherIcon(),
                    modifier = Modifier.requiredSize(200.dp)
                )
            }

            if (viewState.loading.not()) {
                Card(
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    modifier = Modifier
                        .height(300.dp)
                        .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colors.secondary,
                                    MaterialTheme.colors.surface
                                )
                            )
                        )
                        .fillMaxWidth()
                        .constrainAs(forecast) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                ) {
                    val scrollState = rememberLazyListState()
                    val scope = rememberCoroutineScope()
                    LazyRow(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        state = scrollState
                    ) {
                        item {
                            Spacer(modifier = Modifier.width(20.dp))
                        }
                        items(viewState.forecast) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                var detailsVisible by remember { mutableStateOf(false) }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    val calendar = Calendar.getInstance()
                                    calendar.timeInMillis = (it.dt * 1000)
                                    Text(calendar.dayOfWeek)
                                    Card(
                                        shape = CircleShape,
                                        modifier = Modifier
                                            .size(90.dp)
                                            .clickable {
                                                detailsVisible = !detailsVisible
                                                scope.launch {
                                                    scrollState.animateScrollToItem(
                                                        viewState.forecast.indexOf(
                                                            it
                                                        ),
                                                        200
                                                    )
                                                }
                                            }
                                            .clip(CircleShape)
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            WeatherIcon(
                                                weather = it.weather.getOrNull(0)?.icon?.toWeatherIcon()
                                                    ?: Weather.Sunny,
                                                modifier = Modifier
                                                    .padding(top = 4.dp)
                                                    .requiredSize(50.dp)
                                            )
                                            Text(
                                                "${it.temp.min.toInt()}°/ ${it.temp.max.toInt()}°",
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }

                                androidx.compose.animation.AnimatedVisibility(
                                    visible = detailsVisible,
                                    enter = fadeIn() + expandHorizontally(
                                        animationSpec = tween(
                                            easing = FastOutSlowInEasing
                                        )
                                    ),
                                    exit = fadeOut() + shrinkHorizontally(
                                        animationSpec = tween(
                                            easing = FastOutSlowInEasing
                                        )
                                    )
                                ) {
                                    Column(Modifier.padding(start = 8.dp)) {
                                        Text("Rain probability: ${it.pop * 100}%")
                                        Text("Humidity: ${it.humidity}%")
                                        Text("Feels like: ${it.feels_like.day}°")
                                    }
                                }

                                Spacer(Modifier.width(10.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
//    var movement by remember  { mutableStateOf(0L)}
//    LaunchedEffect(key1 = Unit) {
//        while (isActive) {
//            withInfiniteAnimationFrameMillis {
//                movement = (System.currentTimeMillis() % 15020)
//            }
//        }
//    }

    Box(modifier = modifier) {
        Cloud(300f, 5f, 1f, 7500, 18000)
        Cloud(500f, 3f, 1f, 2000, 28000)
        Cloud(550f, 3f, 1f, 9000, 24000)
        Cloud(600f, 2.8f, 1f, 7000, 40000)
        Cloud(800f, 1.5f, 1f, 0, 80000)
        Cloud(820f, 1.3f, 1f, 15000, 70000)
        Cloud(760f, 1f, 1f, 0, 100000)
    }
}

@Composable
fun Cloud(
    offsetY: Float = 0f,
    sizeScale: Float = 1f,
    cloudAlpha: Float = 1f,
    delay: Int = 0,
    duration: Int = 0
) {
    val infiniteTransition = rememberInfiniteTransition()
    val translate by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 1400f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = LinearEasing,
                delayMillis = delay
            ),
            repeatMode = RepeatMode.Restart
        )
    )
    Icon(
        imageVector = Icons.Default.Cloud,
        modifier = Modifier
            .graphicsLayer {
                scaleX = sizeScale
                scaleY = sizeScale
                alpha = cloudAlpha
                translationX = translate
                translationY = offsetY
            },
        contentDescription = null,
        tint = Color.White
    )
}

private fun String.toWeatherIcon(): Weather =
    when (this) {
//        01d.png 	01n.png 	clear sky
//        02d.png 	02n.png 	few clouds
//        03d.png 	03n.png 	scattered clouds
//        04d.png 	04n.png 	broken clouds
//        09d.png 	09n.png 	shower rain
//        10d.png 	10n.png 	rain
//            11d.png 	11n.png 	thunderstorm
//        13d.png 	13n.png 	snow
//        50d.png 	50n.png 	mist
        "01d" -> Weather.Sunny
        "01n" -> Weather.Night
        "02d", "03d", "04d" -> Weather.PartlyCloudy
        "02n", "03n", "04n" -> Weather.CloudyNight
        "09d", "10d" -> Weather.RainDay
        "09nd", "10n" -> Weather.RainNight
        "11d" -> Weather.ThunderStormDay
        "11n" -> Weather.Thunderstorms
        "13d" -> Weather.SnowSunny
        "13n" -> Weather.SnowNight
        "50d", "50n" -> Weather.Mist
        else -> Weather.Sunny
    }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedVisibility(visible: Boolean, modifier: Modifier, content: @Composable () -> Unit) =
    androidx.compose.animation.AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier,
        content = content
    )

@Composable
private fun SearchInput(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    onDone: KeyboardActionScope.() -> Unit,
) {
    TextField(
        value = value,
        label = { Text("Zip/City") },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = onDone),
        singleLine = true,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.White,
            focusedIndicatorColor = Color.White,
        )
    )
}

private val Calendar.dayOfWeek: String
    get() {
        return when (this.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> ""
        }
    }

sealed class UiEvent {
    object RetrieveWeatherZip : UiEvent()
    object RetrieveWeatherCity : UiEvent()
    class TypeZip(val zip: String) : UiEvent()
    class TypeCity(val city: String) : UiEvent()
}

private fun action(event: UiEvent, vm: MainViewModel) {
    val channel = vm.userIntentChannel
    when (event) {
        UiEvent.RetrieveWeatherZip -> {
            channel.offer(MainUserIntents.Loading)
            channel.offer(MainUserIntents.GetWeatherZip)
        }
        UiEvent.RetrieveWeatherCity -> {
            channel.offer(MainUserIntents.Loading)
            channel.offer(MainUserIntents.GetWeatherCity)
        }
        is UiEvent.TypeZip -> channel.offer(MainUserIntents.UpdateZip(event.zip))
        is UiEvent.TypeCity -> channel.offer(MainUserIntents.UpdateCity(event.city))
    }
}

@Preview(group = "loading")
@Composable
fun LoadingPreview() {
    MyTheme {
        MyApp(MainViewState(loading = true))
    }
}

@Preview(name = "Dark Theme", widthDp = 360, heightDp = 640, showBackground = true)
@Preview(name = "Dark Theme", showBackground = true)
@Composable
fun DarkPreview() {
    MyTheme {
        MyApp(
            MainViewState(
                inputText = "78787",
                loading = false,
                city = "New York",
                temp = "74",
                forecast = listOf(
                    Day(
                        0,
                        0,
                        0,
                        FeelsLike(0.0, 0.0, 0.0, 0.0),
                        0,
                        0.0,
                        0.0,
                        0.0,
                        0,
                        0,
                        Temp(0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                        listOf(WeatherX("Cloudy", "04n", 0, ""))
                    ),
                    Day(
                        0,
                        0,
                        0,
                        FeelsLike(0.0, 0.0, 0.0, 0.0),
                        0,
                        0.0,
                        0.0,
                        0.0,
                        0,
                        0,
                        Temp(0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                        listOf(WeatherX("Cloudy", "04n", 0, ""))
                    ),
                    Day(
                        0,
                        0,
                        0,
                        FeelsLike(0.0, 0.0, 0.0, 0.0),
                        0,
                        0.0,
                        0.0,
                        0.0,
                        0,
                        0,
                        Temp(0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                        listOf(WeatherX("Cloudy", "04n", 0, ""))
                    ),
                    Day(
                        0,
                        0,
                        0,
                        FeelsLike(0.0, 0.0, 0.0, 0.0),
                        0,
                        0.0,
                        0.0,
                        0.0,
                        0,
                        0,
                        Temp(0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                        listOf(WeatherX("Cloudy", "04n", 0, ""))
                    ),
                )
            )
        )
    }
}
