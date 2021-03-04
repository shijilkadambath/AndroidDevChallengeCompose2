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

import androidx.activity.compose.setContent
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var execiseStop by mutableStateOf(true)
var execise by mutableStateOf(false)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colors.background)

    ) {
        Toolbar()
        Text(

            text = "Exercise and Rest",
            style = MaterialTheme.typography.h4.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground
            ),
            modifier = Modifier
                .padding(
                    top = 50.dp
                )
                .align(alignment = Alignment.CenterHorizontally)
        )
        Content()
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}


@Composable
fun Toolbar() {

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart,
    ) {
        TopAppBar(
            title = {
                Text(text = "")
            },
            backgroundColor = MaterialTheme.colors.primaryVariant,
            contentColor = Color.White,
            elevation = 12.dp
        )

        Row(
            modifier = Modifier
                .padding(8.dp, 0.dp, 8.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.baseline_fitness_center_24),
                contentDescription = null,
                tint = Color.White,
            )
            Spacer(modifier = Modifier.width(15.dp))

            Text(
                text = "Exercise Timer",
                color = Color.White,
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
        }
    }
}

var isPause by mutableStateOf(true)
var progress by mutableStateOf(362f)

@Composable
fun Content() {
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
    ) {


        ProgressContent()

        Card(

            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
                    elevation = 8.dp,
            backgroundColor =MaterialTheme.colors.onSurface
        ) {
            if (execiseStop) {
                ShowStart(coroutineScope)
            } else {
                ShowBottom(coroutineScope)
            }
        }
    }
}

@Composable
fun ProgressContent() {


    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .size(220.dp)
            .padding(top = 20.dp)
    ) {
        val (box, circle, text) = createRefs()
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .constrainAs(box) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        AnimatedCircle(
            Modifier
                .height(190.dp)
                .fillMaxWidth()
                .constrainAs(circle) {
                    top.linkTo(box.top, margin = 5.dp)
                    start.linkTo(box.start)
                    end.linkTo(box.end)
                },
            progress
        )
        var seconds = (((progress - 2) % 120) / 2).toInt()
        Text(
            text = "${((progress - 2) / 120).toInt()}:${if (seconds < 10) "0$seconds" else seconds}",
            style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = MaterialTheme.colors.onBackground,

            modifier = Modifier
                .constrainAs(text) {
                    top.linkTo(circle.top, margin = 4.dp)
                    start.linkTo(circle.start, margin = 4.dp)
                    end.linkTo(circle.end, margin = 4.dp)
                    bottom.linkTo(circle.bottom, margin = 4.dp)
                }
        )

    }
}

@Composable
fun ShowStart(coroutineScope: CoroutineScope) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)

    ) {

        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .align(Alignment.Center)
                .size(60.dp)
                .border(
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colors.background
                    ),
                    shape = CircleShape,
                ),

            onClick = {
                execiseStop = false
                execise = true
                coroutineScope.launch {
                    setAnimation()
                }
            }
        ) {

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = purple900,

                )
        }
    }
}

@Composable
fun ShowBottom(coroutineScope: CoroutineScope) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        /*Button(
            onClick = {
                execise = !isPause
                coroutineScope.launch {
                    setAnimation()
                }
                isPause = !isPause
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier
                .clip(CircleShape)
                .size(80.dp)
        ) {
            Text(
                text = if (isPause) "Pause" else "Resume",
                color = purple700,
                style = MaterialTheme.typography.caption
            )
        }*/
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .align(Alignment.Center)
                .size(60.dp)
                .border(
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colors.background
                    ),
                    shape = CircleShape,
                ),

            onClick = {
                execise = !isPause
                coroutineScope.launch {
                    setAnimation()
                }
                isPause = !isPause
            }
        ) {

            Icon(
                imageVector = if (isPause) Icons.Default.Pause else Icons.Default.PlayArrow  ,
                contentDescription = null,
                tint = purple900,

                )
        }

        /*Spacer(
            modifier = Modifier
                .weight(1f)
        )*/
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .size(60.dp)
                .align(Alignment.BottomEnd)
                .border(
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colors.background
                    ),
                    shape = CircleShape,
                ),

            onClick = {
                execiseStop = true
                execise = false
                isPause = true
                progress = 362f
            }
        ) {

            Icon(
                imageVector = Icons.Default.Stop ,
                contentDescription = null,
                tint = purple900,

                )
        }
    }
}

private const val DividerLengthInDegrees = 1.8f

@Composable
fun AnimatedCircle(
    modifier: Modifier = Modifier,
    sweep: Float
) {
    val stroke = with(LocalDensity.current) { Stroke(15.dp.toPx()) }
    Canvas(modifier) {
        val innerRadius = (size.minDimension - stroke.width) / 2
        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.width - innerRadius,
            halfSize.height - innerRadius
        )
        val size = Size(innerRadius * 2, innerRadius * 2)
        val startAngle = -90f
        drawArc(
            color = purple900,
            startAngle = startAngle + DividerLengthInDegrees / 2,
            sweepAngle = sweep - DividerLengthInDegrees,
            topLeft = topLeft,
            size = size,
            useCenter = false,
            style = stroke
        )
    }
}

suspend fun setAnimation() {
    while (execise) {
        progress -= 1
        if (progress <= 2) {
            progress = 362f
            execise = false
            execiseStop = true
        }
        delay(500)
    }
}