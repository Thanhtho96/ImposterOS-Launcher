package com.tt.imposteroslauncher

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.tt.imposteroslauncher.ui.theme.ImposterOSLauncherTheme
import com.tt.imposteroslauncher.util.iconSize
import com.tt.imposteroslauncher.util.screenHeightInPx
import com.tt.imposteroslauncher.util.screenWidthInPx
import com.tt.imposteroslauncher.util.toPx

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val center = Pair(
                first = screenWidthInPx() / 2 - (iconSize() / 2).toPx(),
                second = screenHeightInPx() / 2 - (iconSize() / 2).toPx()
            )

            val scaleX = screenWidthInPx() / iconSize().toPx()
            val scaleY = screenHeightInPx() / iconSize().toPx()

            val systemUiController = rememberSystemUiController()

            SideEffect {
                // Update all of the system bar colors to be transparent, and use
                // dark icons if we're in light theme
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = false
                )
            }

            ImposterOSLauncherTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { Spacer(Modifier.statusBarsPadding()) },
                    bottomBar = { Spacer(Modifier.navigationBarsPadding()) },
                    backgroundColor = Color.Transparent,
                    content = {
                        Column {
                            for (i in 0 until 8) {
                                Row {
                                    for (j in 0 until 5) {
                                        Greeting(
                                            scaleX, scaleY, center
                                        )
                                    }
                                }
                            }
                        }
                    })
            }
        }
    }
}

@Composable
fun Greeting(
    targetScaleX: Float = 0f,
    targetScaleY: Float = 0f,
    center: Pair<Float, Float> = Pair(0f, 0f)
) {
    val interactionSource = remember { MutableInteractionSource() }
    var currentState by remember { mutableStateOf(BoxState.Collapsed) }
    val transition = updateTransition(currentState, label = "")
    val context = LocalContext.current
    var offsetInRoot by remember { mutableStateOf(Offset.Zero) }

    val cornerRound by transition.animateFloat(transitionSpec = transition(), label = "") { state ->
        when (state) {
            BoxState.Collapsed -> 18.dp.toPx()
            BoxState.Expanded -> 0f
        }
    }

    val offset by transition.animateOffset(transitionSpec = transition(), label = "") { state ->
        when (state) {
            BoxState.Collapsed -> Offset(0f, 0f)
            BoxState.Expanded -> Offset(
                x = center.first - offsetInRoot.x,
                y = center.second - offsetInRoot.y
            )
        }
    }

    val scaleX by transition.animateFloat(transitionSpec = transition(), label = "") { state ->
        when (state) {
            BoxState.Collapsed -> 1f
            BoxState.Expanded -> targetScaleX
        }
    }

    val scaleY by transition.animateFloat(transitionSpec = transition(), label = "") { state ->
        when (state) {
            BoxState.Collapsed -> 1f
            BoxState.Expanded -> targetScaleY
        }
    }

    val zIndex by transition.animateFloat(transitionSpec = transition(), label = "") { state ->
        when (state) {
            BoxState.Collapsed -> 0f
            BoxState.Expanded -> 7f
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                currentState = when (currentState) {
                    BoxState.Collapsed -> BoxState.Expanded
                    BoxState.Expanded -> BoxState.Collapsed
                }

//                val launchIntent =
//                    context.packageManager.getLaunchIntentForPackage("com.ingenico.ingp.app.main")
//                context.startActivity(launchIntent!!)
            }
            .graphicsLayer {
                this.scaleX = scaleX
                this.scaleY = scaleY
                this.translationX = offset.x
                this.translationY = offset.y
            }
            .drawBehind {
                drawRoundRect(
                    color = Color.Cyan,
                    cornerRadius = CornerRadius(
                        x = cornerRound,
                        y = cornerRound
                    )
                )
            }
            .onGloballyPositioned { layoutCoordinates: LayoutCoordinates ->
                if (
                    layoutCoordinates.isAttached &&
                    transition.isRunning.not() &&
                    transition.currentState == BoxState.Collapsed
                ) {
                    offsetInRoot = layoutCoordinates.positionInRoot()
                }
            }
            .size(iconSize())
            .zIndex(zIndex)
    ) {
        Text(text = "CH", textAlign = TextAlign.Center)
    }
}

@Composable
private fun <T> transition(): @Composable (Transition.Segment<BoxState>.() -> FiniteAnimationSpec<T>) =
    {
        when {
            BoxState.Expanded isTransitioningTo BoxState.Collapsed ->
                spring(dampingRatio = 0.9f, stiffness = Spring.StiffnessLow)
            else ->
                spring(dampingRatio = 1f, stiffness = Spring.StiffnessLow)
        }
    }

enum class BoxState {
    Collapsed,
    Expanded
}