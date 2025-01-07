package com.top.movies.presentation.splash_detail

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.top.movies.R
import com.top.movies.ui.theme.PurpleGrey40
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onNavigateToMain: () -> Unit,width:Int, height:Int) {



    val compositionPopcorn by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.popcorn))
    val progressPopcorn by animateLottieCompositionAsState(
        composition = compositionPopcorn,
        speed = 0.5f
    )
    val loadingAnimation = remember{ Animatable(0f) }
    LaunchedEffect(Unit){
        loadingAnimation.animateTo(1f, tween(5000))
    }

    var text by remember { mutableStateOf("Загрузка...") }
    val sentences = listOf(
        "Loading movies.",
        "Putting things in order.",
        "Loading databases."
    )

    LaunchedEffect(Unit) {
        launch { var currentText = ""
            for (sentence in sentences) {
                currentText = sentence
                text = currentText
                delay(1500L)
            } }
        launch {
            delay(5500)
            onNavigateToMain.invoke()
        }

    }
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0D1B2A)))
    {

        Column(modifier = Modifier.align(Alignment.TopCenter).padding(top = (height*0.1).dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.movie_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(width = (width*0.38).dp, height = (height*0.2).dp)
                    .padding(bottom = 24.dp)
            )
            Spacer(modifier = Modifier.height((height*0.1   ).dp))
            AnimatedTextForSplash("MovieZone",
                modifier = Modifier)


        }

        Column(modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally) {



                LinearProgressIndicator(
                    progress = { loadingAnimation.value },
                    modifier = Modifier
                        .height((height * 0.023).dp)
                        .width((width * 0.8).dp)
                        .clip(shape = RoundedCornerShape(10.dp)),
                    color = Green,
                    trackColor = Color.White,
                )

            Spacer(modifier = Modifier.height((height*0.05).dp))

            Text(text, color = PurpleGrey40, fontSize = 15.sp)

            Spacer(modifier = Modifier.height((height*0.05).dp))

            lottieAnimation(modifier = Modifier
                .size(width = (width * 0.3).dp, height = (height * 0.23).dp)

                .padding(bottom = (height*0.1).dp),
                composition = compositionPopcorn,
                progress =  progressPopcorn )

        }





    }


}
@Composable
fun AnimatedTextForSplash(text: String, modifier: Modifier=Modifier) {
    val animatedCharacters = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(500)
        animatedCharacters.animateTo(
            targetValue = text.length.toFloat(),
            animationSpec = tween(
                durationMillis = 2000,
                easing = LinearEasing
            )
        )
    }

    TextwShadow(text.substring(0, animatedCharacters.value.toInt()),color = White,modifier = modifier, fontsize = 40)


}
@Composable
internal fun TextwShadow(text:String, modifier: Modifier=Modifier, color:Color, fontsize: Int)
{

    Box(modifier = modifier)
    {
        Text(
            text = text,
            style = TextStyle(
                fontSize = fontsize.sp,
                color = Color.Black
            ),
            modifier = Modifier.offset((-1).dp, (-1).dp)

        )
        Text(
            text = text,
            style = TextStyle(
                fontSize = fontsize.sp,
                color = color
            ),
            modifier = Modifier.offset((1).dp, (1).dp)

        )
    }
}
@Composable
fun lottieAnimation(modifier: Modifier=Modifier, composition: LottieComposition?, progress:Float)
{
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { progress }
    )
}