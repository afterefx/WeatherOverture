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
package com.example.androiddevchallenge.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.R

val montserrat = FontFamily(Font(R.font.montserrat))
val m_bold = FontFamily(Font(R.font.montserrat_bold))

// Set of Material typography styles to start with
val typography = Typography(
    defaultFontFamily = montserrat,
    h1 = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Light,
        fontSize = 96.sp,
        letterSpacing = (-1.5).sp
    ),
    h2 = TextStyle(
        color = Color.White,
        fontFamily = m_bold,
//        fontWeight = FontWeight.Light,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 60.sp,
        letterSpacing = (-0.5).sp,
    ),
    h3 = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        letterSpacing = 0.sp,
    ),
    h4 = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        letterSpacing = 0.25.sp
    ),
    h5 = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        letterSpacing = 0.sp
    ),
    h6 = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle1 = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    body1 = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    body2 = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    button = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 1.25.sp
    ),
    caption = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp
    ),
    overline = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        letterSpacing = 1.5.sp
    )

    /* Other default text styles to override
button = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W500,
    fontSize = 14.sp
),
caption = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp
)
*/
)

@Preview(showBackground = true, device = Devices.PIXEL_4_XL, showSystemUi = true)
@Composable
fun typePreview() {
    MyTheme {
        Column(modifier = Modifier.background(MaterialTheme.colors.primary)) {
            Text("h1", style = MaterialTheme.typography.h1)
            Text("h2", style = MaterialTheme.typography.h2)
            Text("h3", style = MaterialTheme.typography.h3)
            Text("h4", style = MaterialTheme.typography.h4)
            Text("h5", style = MaterialTheme.typography.h5)
            Text("h6", style = MaterialTheme.typography.h6)
            Text("subtitle1", style = MaterialTheme.typography.subtitle1)
            Text("subtitle2", style = MaterialTheme.typography.subtitle2)
            Text("body1", style = MaterialTheme.typography.body1)
            Text("body2", style = MaterialTheme.typography.body2)
            Text("button", style = MaterialTheme.typography.button)
            Text("caption", style = MaterialTheme.typography.caption)
            Text("overline", style = MaterialTheme.typography.overline)
        }
    }
}
