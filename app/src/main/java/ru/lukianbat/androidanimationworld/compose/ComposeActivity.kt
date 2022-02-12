package ru.lukianbat.androidanimationworld.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import ru.lukianbat.androidanimationworld.R

class ComposeActivity : ComponentActivity() {

    @OptIn(ExperimentalTransitionApi::class)
    @ExperimentalMaterialApi
    @ExperimentalMotionApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TargetBasedAnimationForPerfomanceTest()
            }
        }
    }
}

@Composable
fun MovingAnimatable1() {
    // TODO 1 Запуск анимации перемещения с помощью объекта Animatable
    val startValue = 0F
    val endValue = 300F
    val yOffsetAnimatable = remember {
        Animatable(
            startValue,
            Float.VectorConverter
        )
    }

    Image(
        painter = painterResource(R.drawable.ic_podlodka),
        contentDescription = null,
        modifier = Modifier
            .size(100F.dp, 100F.dp)
            .offset(y = yOffsetAnimatable.value.dp)
    )

    LaunchedEffect(true) {
        yOffsetAnimatable.animateTo(
            targetValue = endValue,
            animationSpec = tween(1500)
        )
    }
}

@Composable
fun AlphaAnimateAsStateTest2() {
    // TODO 2 Запуск анимации изменения alpha с помощью функции animateFloatAsState
    var imageEnabled by remember { mutableStateOf(false) }
    val alpha: Float by animateFloatAsState(
        targetValue = if (imageEnabled) 1f else 0f,
        animationSpec = tween(1500)
    )
    Image(
        painter = painterResource(R.drawable.ic_podlodka),
        contentDescription = null,
        modifier = Modifier
            .size(100F.dp, 100F.dp)
            .alpha(alpha)
    )

    Button(
        onClick = {
            imageEnabled = imageEnabled.not()
        },
        Modifier.offset(y = 300.dp)
    ) {
        Text("Change imageEnabled state")
    }
}

@Composable
fun AlphaAndMovingTransitionTest3() {
    // TODO 3 Запуск анимаций alpha и перемещения c помощью объекта Transition

    var startAnimation by remember { mutableStateOf(false) }
    val transition = updateTransition(startAnimation, label = "")

    val yOffset by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 3000) },
        label = "yOffsetAnimation"
    ) { if (it) 300F else 0F }

    val alpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 3000) },
        label = "alphaAnimation"
    ) { if (it) 1F else 0F }

    Image(
        painter = painterResource(R.drawable.ic_podlodka),
        contentDescription = "firstIcon",
        modifier = Modifier
            .size(100F.dp, 100F.dp)
            .offset(y = yOffset.dp)
            .alpha(alpha)
    )

    Button(
        onClick = {
            startAnimation = startAnimation.not()
        },
        Modifier.offset(y = 300.dp)
    ) {
        Text("Change startAnimation state")
    }
}

@Composable
fun MutableTransitionStateTest4() {
    // TODO 4 Запуск анимации перемещения во время компоновки с помощью MutableTransitionState

    var currentState = remember { MutableTransitionState(false) }
    currentState.targetState = true
    val transition = updateTransition(currentState, label = "")

    val yOffset by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1500) },
        label = ""
    ) { if (it) 300F else 0F }

    Image(
        painter = painterResource(R.drawable.ic_podlodka),
        contentDescription = "firstIcon",
        modifier = Modifier
            .size(100F.dp, 100F.dp)
            .offset(y = yOffset.dp)
    )
}

@ExperimentalTransitionApi
@Composable
fun CreateChildTransitionTest5() {
    // TODO 5 Декомпозиция составных анимаций с помощью createChildTransition

    var parentState by remember { mutableStateOf(ParentState.InitialState) }
    val parentTransition = updateTransition(parentState, label = "")

    val firstAnimationTransition = parentTransition.createChildTransition {
        it != ParentState.InitialState
    }
    val secondAnimationTransition = parentTransition.createChildTransition {
        it == ParentState.SecondParentState
    }

    val alpha by firstAnimationTransition.animateFloat(
        transitionSpec = { tween(durationMillis = 3000) },
        label = ""
    ) { if (it) 1F else 0F }

    val yOffset by secondAnimationTransition.animateFloat(
        transitionSpec = { tween(durationMillis = 3000) },
        label = ""
    ) { if (it) 400F else 100F }

    Image(
        painter = painterResource(R.drawable.ic_podlodka),
        contentDescription = "firstIcon",
        modifier = Modifier
            .size(100F.dp, 100F.dp)
            .offset(y = yOffset.dp)
            .alpha(alpha)
    )

    LaunchedEffect(true) {
        parentState = ParentState.FirstParentState
        delay(3000)
        parentState = ParentState.SecondParentState
    }
}

enum class ParentState {
    InitialState,
    FirstParentState,
    SecondParentState
}

@Composable
fun AlphaInfiniteTransitionTest6() {
    // TODO 6 Бесконечная анимация альфы с помощью rememberInfiniteTransition

    val transition = rememberInfiniteTransition()
    val alpha by transition.animateFloat(
        initialValue = 0F,
        targetValue = 1F,
        animationSpec = InfiniteRepeatableSpec(
            tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        ),
    )

    Image(
        painter = painterResource(R.drawable.ic_podlodka),
        contentDescription = "secondIcon",
        modifier = Modifier
            .size(100F.dp, 100F.dp)
            .alpha(alpha)
    )
}

@Composable
fun RotationTargetBasedAnimationTest7() {
    // TODO 7 Анимация вращения с помощью TargetBasedAnimation

    var rotation by remember { mutableStateOf(0F) }
    var startAnimation by remember { mutableStateOf(false) }

    val anim = remember {
        TargetBasedAnimation(
            animationSpec = tween(3000),
            typeConverter = Float.VectorConverter,
            initialValue = 0f,
            targetValue = 360f
        )
    }

    LaunchedEffect(startAnimation) {
        var playTime: Long
        val startTime = withFrameNanos { it }
        do {
            playTime = withFrameNanos { it } - startTime
            rotation = anim.getValueFromNanos(playTime)
        } while (!anim.isFinishedFromNanos(playTime))
    }

    Image(
        painter = painterResource(R.drawable.ic_podlodka),
        contentDescription = "secondIcon",
        modifier = Modifier
            .size(100F.dp, 100F.dp)
            .rotate(rotation)
    )

    Button(
        onClick = {
            startAnimation = startAnimation.not()
        },
        Modifier.offset(y = 300.dp)
    ) {
        Text("Change startAnimation state")
    }
}

@Composable
fun DecayAnimationTest8() {
    // TODO 8 Анимация перемещения броском с помощью DecayAnimation
    var startAnimation by remember { mutableStateOf(false) }

    var offset by remember { mutableStateOf(0F) }

    val anim = remember {
        DecayAnimation(
            initialValue = 0F,
            animationSpec = FloatExponentialDecaySpec(),
            initialVelocity = 2350F
        )
    }
    var playTime by remember { mutableStateOf(0L) }

    LaunchedEffect(startAnimation) {
        val startTime = withFrameNanos { it }
        do {
            playTime = withFrameNanos { it } - startTime
            offset = anim.getValueFromNanos(playTime)
        } while (!anim.isFinishedFromNanos(playTime))
    }

    Image(
        painter = painterResource(R.drawable.ic_podlodka),
        contentDescription = "secondIcon",
        modifier = Modifier
            .size(100F.dp, 100F.dp)
            .offset(y = offset.dp)
    )

    Button(
        onClick = {
            startAnimation = startAnimation.not()
        },
        Modifier.offset(y = 600.dp)
    ) {
        Text("Change startAnimation state")
    }
}

@Composable
fun TweenAnimationSpecTest9() {
    // TODO 9 Анимация перемещения с прямолинейной скоростью с помощью TweenAnimationSpec и LinearEasing

    var startAnimation by remember { mutableStateOf(false) }

    val yOffsetAnimation: Float by animateFloatAsState(
        targetValue = if (startAnimation) 500f else 0f,
        animationSpec = tween(
            delayMillis = 300,
            durationMillis = 3000,
            easing = LinearEasing
        )
    )

    Image(
        painter = painterResource(R.drawable.ic_podlodka),
        contentDescription = null,
        modifier = Modifier
            .size(100F.dp, 100F.dp)
            .offset(y = yOffsetAnimation.dp)
    )

    Button(onClick = {
        startAnimation = startAnimation.not()
    }, modifier = Modifier.offset(y = 600.dp)) {
        Text("Change startAnimation state")
    }
}

@Composable
fun SpringAnimationSpecTest10() {
    // TODO 10 Анимация перемещения с физикой пружины с помощью SpringAnimationSpec

    var startAnimation by remember { mutableStateOf(false) }
    val yOffsetAnimatable: Float by animateFloatAsState(
        targetValue = if (startAnimation) 500f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Image(
        painter = painterResource(R.drawable.ic_podlodka),
        contentDescription = null,
        modifier = Modifier
            .size(100F.dp, 100F.dp)
            .offset(y = yOffsetAnimatable.dp)
    )

    Button(onClick = {
        startAnimation = startAnimation.not()
    }, modifier = Modifier.offset(y = 600.dp)) {
        Text("Change startAnimation state")
    }
}

@Composable
fun KeyframesAnimationSpecTest11() {
    // TODO 11 Анимация перемещения с покадровой интерполяцией
    var startAnimation by remember { mutableStateOf(false) }

    val yOffsetAnimatable: Float by animateFloatAsState(
        targetValue = if (startAnimation) 500f else 0f,
        animationSpec = keyframes {
            durationMillis = 3000
            0f at 0 with LinearOutSlowInEasing
            0.5f at 1500 with FastOutSlowInEasing
        }
    )

    Image(
        painter = painterResource(R.drawable.ic_podlodka),
        contentDescription = null,
        modifier = Modifier
            .size(100F.dp, 100F.dp)
            .offset(y = yOffsetAnimatable.dp)
    )

    Button(onClick = {
        startAnimation = startAnimation.not()
    }, modifier = Modifier.offset(y = 600.dp)) {
        Text("Change startAnimation state")
    }
}

@Composable
fun RepeatableAnimationSpecTest12() {
    // TODO 12 Анимация перемещения с 5-кратным повтором
    var startAnimation by remember { mutableStateOf(false) }
    val yOffsetAnimatable: Float by animateFloatAsState(
        targetValue = if (startAnimation) 500f else 0f,
        animationSpec = repeatable(
            iterations = 5,
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        )
    )

    Image(
        painter = painterResource(R.drawable.ic_podlodka),
        contentDescription = null,
        modifier = Modifier
            .size(100F.dp, 100F.dp)
            .offset(y = yOffsetAnimatable.dp)
    )

    Button(onClick = {
        startAnimation = startAnimation.not()
    }, modifier = Modifier.offset(y = 600.dp)) {
        Text("Change startAnimation state")
    }
}

@Composable
fun AnimationVectorTest13() {
    // TODO 13 Анимация изменения кастомного типа с двумя параметрами MySize с помощью AnimationVector2D

    var startAnimation by remember { mutableStateOf(false) }

    val animSize: MySize by animateValueAsState(
        targetValue = if (startAnimation) MySize(300.dp, 600.dp) else MySize(100.dp, 50.dp),
        typeConverter = TwoWayConverter(
            convertToVector = { size: MySize ->
                AnimationVector2D(size.width.value, size.height.value)
            },
            convertFromVector = { vector: AnimationVector2D ->
                MySize(vector.v1.dp, vector.v2.dp)
            }
        ),
        animationSpec = tween(durationMillis = 3000, delayMillis = 1000)
    )

    Image(
        painter = painterResource(R.drawable.ic_podlodka),
        contentDescription = null,
        modifier = Modifier
            .size(animSize.width, animSize.height)
    )

    Button(onClick = {
        startAnimation = startAnimation.not()
    }, modifier = Modifier.offset(y = 600.dp)) {
        Text("Change startAnimation state")
    }
}

data class MySize(
    val width: Dp,
    val height: Dp
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedVisibilityTest14() {
    // TODO 14 Анимация появления контента с помощью AnimatedVisibility

    var visible by remember { mutableStateOf(true) }
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_podlodka),
                contentDescription = null,
                modifier = Modifier
                    .size(200F.dp, 200F.dp)
            )
            Text(
                "Hello Podlodka!!!",
                fontSize = 48.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .wrapContentWidth()
            )
        }
    }

    Button(
        onClick = {
            visible = visible.not()
        },
        modifier = Modifier.offset(y = 28.dp),
    ) {
        Text(
            "Запустить анимацию",
            fontSize = 21.sp,
            modifier = Modifier
                .wrapContentWidth()
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedVisibilityTest15() {
    // TODO 15 Анимация появления контента с комбинацией и кастомизацией enter/exit Transition

    var visible by remember { mutableStateOf(true) }
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically() + fadeIn(animationSpec = tween(2000)),
        exit = slideOutVertically() + fadeOut(animationSpec = tween(2000))
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_podlodka),
                contentDescription = null,
                modifier = Modifier
                    .size(200F.dp, 200F.dp)
            )
            Text(
                "Hello Podlodka!!!",
                fontSize = 48.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .wrapContentWidth()
            )
        }
    }

    Button(
        onClick = {
            visible = visible.not()
        },
        modifier = Modifier.offset(y = 28.dp),
    ) {
        Text(
            "Запустить анимацию",
            fontSize = 21.sp,
            modifier = Modifier
                .wrapContentWidth()
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedVisibilityTest16() {
    // TODO 16 Анимация появления контента с помощью AnimatedVisibility и animateEnterExit

    var visible by remember { mutableStateOf(true) }
    AnimatedVisibility(visible = visible) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_podlodka),
                contentDescription = null,
                modifier = Modifier
                    .size(200F.dp, 200F.dp)
                    .animateEnterExit(
                        enter = slideInVertically(),
                        exit = slideOutVertically()
                    )
            )
            Text(
                "Hello Podlodka!!!",
                fontSize = 48.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .wrapContentWidth()
                    .animateEnterExit(
                        enter = fadeIn(),
                        exit = fadeOut()
                    )
            )
        }
    }

    Button(
        onClick = {
            visible = visible.not()
        },
        modifier = Modifier.offset(y = 28.dp),
    ) {
        Text(
            "Запустить анимацию",
            fontSize = 21.sp,
            modifier = Modifier
                .wrapContentWidth()
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedVisibilityTest17() {
    // TODO 17 Анимация появления контента с дополнительными анимациями Transition

    var visible by remember { mutableStateOf(true) }
    AnimatedVisibility(
        visible = visible,
        enter = slideIn(
            animationSpec = tween(2000),
            initialOffset = { IntOffset(0, -it.height) }
        ),
        exit = slideOut(
            animationSpec = tween(2000),
            targetOffset = { IntOffset(0, -it.width) }
        ),
    ) {
        val background by transition.animateColor(
            label = "",
            transitionSpec = { tween(2000) }
        ) { state ->
            if (state == EnterExitState.Visible) Color.Green else Color.Red
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(background)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_podlodka),
                contentDescription = null,
                modifier = Modifier
                    .size(200F.dp, 200F.dp)
            )
            Text(
                "Hello Podlodka!!!",
                fontSize = 48.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .wrapContentWidth()
            )
        }
    }

    Button(
        onClick = {
            visible = visible.not()
        },
        modifier = Modifier.offset(y = 28.dp),
    ) {
        Text(
            "Запустить анимацию",
            fontSize = 21.sp,
            modifier = Modifier
                .wrapContentWidth()
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedContentTest18() {
    // TODO 18 Анимация изменения контента (увеличение числа) с помощью AnimatedContent
    var count by remember { mutableStateOf(0) }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedContent(
            targetState = count,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInVertically { height -> height } with
                            slideOutVertically { height -> -height }
                } else {
                    slideInVertically { height -> -height } with
                            slideOutVertically { height -> height }
                }
            }
        ) { targetCount ->
            val background by transition.animateColor(
                label = "",
                transitionSpec = { tween(2000) }
            ) { state ->
                if (state == EnterExitState.Visible) Color.Green else Color.Red
            }
            // Важно использовать targetCount
            Text(
                fontSize = 48.sp,
                color = background,
                text = "$targetCount"
            )
        }

        Button(
            onClick = { count++ },
            Modifier.offset(y = 28.dp)
        ) {
            Text(
                text = "Plus",
                fontSize = 21.sp,
            )
        }
        Button(
            onClick = { count-- },
            Modifier.offset(y = 28.dp)
        ) {
            Text(
                text = "Minus",
                fontSize = 21.sp,
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun AnimatedContentTransformSizeTest19() {
    // TODO 19 Анимация изменения размера контента (раскрытия) с помощью AnimatedContent и SizeTransform
    var expanded by remember { mutableStateOf(false) }
    Surface(
        color = MaterialTheme.colors.primary,
        onClick = { expanded = !expanded },
        modifier = Modifier.offset(16.dp, 16.dp)
    ) {
        AnimatedContent(
            targetState = expanded,
            transitionSpec = {
                fadeIn(animationSpec = tween(150, 150)) with
                        fadeOut(animationSpec = tween(150)) using
                        SizeTransform { initialSize, targetSize ->
                            if (targetState) {
                                keyframes {
                                    // Expand horizontally first.
                                    IntSize(targetSize.width, initialSize.height) at 150
                                    durationMillis = 300
                                }
                            } else {
                                keyframes {
                                    // Shrink vertically first.
                                    IntSize(initialSize.width, targetSize.height) at 150
                                    durationMillis = 300
                                }
                            }
                        }
            }
        ) { targetExpanded ->
            if (targetExpanded) {
                Text(
                    text = "Привет, подлодка! Привет, подлодка! Привет, подлодка! \n" +
                            " Привет, подлодка! Привет, подлодка! Привет, подлодка! \n" +
                            "Привет, подлодка! Привет, подлодка! Привет, подлодка! \n",
                    fontSize = 16.sp
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_podlodka),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100F.dp, 100F.dp)
                )
            }
        }
    }
}

@Composable
fun AnimateContentSizeTest20() {
    // TODO 20 Анимация изменения размера контента с помощью animateContentSize
    var podlodkaSize by remember { mutableStateOf(100) }
    Image(
        painter = painterResource(R.drawable.ic_podlodka),
        contentDescription = null,
        modifier = Modifier
            .size(podlodkaSize.dp, podlodkaSize.dp)
            .clickable { podlodkaSize += 50 }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy
                )
            ),
    )
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AnimatedVectorResourceTest21() {
    // TODO 21 запуск анимаций из xml ресурсов с помощью animatedVectorResource
    val animationImage = AnimatedImageVector.animatedVectorResource(R.drawable.animated_smile)
    var startAnimation by remember { mutableStateOf(false) }
    Image(
        painter = rememberAnimatedVectorPainter(
            animatedImageVector = animationImage,
            atEnd = startAnimation
        ),
        contentDescription = null,
        modifier = Modifier
            .size(400.dp, 400.dp)
            .clickable { startAnimation = startAnimation.not() }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy
                )
            ),
    )
}

@Composable
fun LottieCompositionTest22() {
    // TODO 22 запуск Lottie анимаций
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.dog_anim))
    LottieAnimation(
        composition = composition,
        isPlaying = true,
        iterations = 20,
        modifier = Modifier
            .size(500.dp)
            .clickable {}
    )
}

private fun startConstraintSet() = ConstraintSet {
    val podlodkaImage = createRefFor("podlodkaImage")
    val title = createRefFor("title")

    constrain(podlodkaImage) {
        width = Dimension.fillToConstraints
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        top.linkTo(parent.top)
    }

    constrain(title) {
        start.linkTo(parent.start, 16.dp)
        top.linkTo(podlodkaImage.bottom, 16.dp)
    }
}

private fun endConstraintSet() = ConstraintSet {
    val podlodkaImage = createRefFor("podlodkaImage")
    val title = createRefFor("title")

    constrain(podlodkaImage) {
        width = Dimension.fillToConstraints
        height = Dimension.value(56.dp)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        top.linkTo(parent.top)
    }

    constrain(title) {
        start.linkTo(parent.start)
        top.linkTo(parent.top, 8.dp)
        end.linkTo(parent.end)
        bottom.linkTo(podlodkaImage.bottom)
    }
}


@ExperimentalMaterialApi
@ExperimentalMotionApi
@Composable
fun MotionLayoutComposeTest23() {
    // TODO 23 MotionLayout

    val screenHeight = LocalConfiguration.current.screenHeightDp.toFloat()

    val swipingState = rememberSwipeableState(initialValue = SwipingStates.EXPANDED)

    val animateMotionLayoutProgress by animateFloatAsState(
        targetValue = if (swipingState.progress.to == SwipingStates.COLLAPSED) {
            swipingState.progress.fraction
        } else {
            1f - swipingState.progress.fraction
        },
        animationSpec = spring(Spring.DampingRatioHighBouncy)
    )

    MotionLayout(
        start = startConstraintSet(),
        end = endConstraintSet(),
        progress = animateMotionLayoutProgress,
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight.dp)
            .swipeable(
                state = swipingState,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Vertical,
                anchors = mapOf(
                    0f to SwipingStates.COLLAPSED,
                    screenHeight to SwipingStates.EXPANDED,
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_podlodka),
            contentDescription = "",
            modifier = Modifier
                .layoutId("podlodkaImage")
                .background(MaterialTheme.colors.primary)
                .alpha(alpha = 1f - animateMotionLayoutProgress),
            contentScale = ContentScale.FillWidth
        )
        Text(
            text = "Hi, podlodka!",
            modifier = Modifier
                .layoutId("title")
                .wrapContentHeight(),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
    }
}

enum class SwipingStates {
    EXPANDED,
    COLLAPSED
}

@OptIn(ExperimentalTransitionApi::class)
@ExperimentalMaterialApi
@ExperimentalMotionApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MotionLayoutComposeTest23()
    }
}

@Composable
fun TargetBasedAnimationForPerfomanceTest() {
    var offset by remember { mutableStateOf(0F) }
    var startAnimation by remember { mutableStateOf(false) }

    val anim = remember {
        TargetBasedAnimation(
            animationSpec = tween(
                durationMillis = 3000,
                easing = LinearEasing
            ),
            typeConverter = Float.VectorConverter,
            initialValue = 0f,
            targetValue = 300f
        )
    }

    LaunchedEffect(startAnimation) {
        var playTime: Long
        val startTime = withFrameNanos { it }
        do {
            playTime = withFrameNanos { it } - startTime
            offset = anim.getValueFromNanos(playTime)
        } while (!anim.isFinishedFromNanos(playTime))
    }

    Image(
        painter = painterResource(R.drawable.ic_podlodka),
        contentDescription = "",
        modifier = Modifier
            .size(100F.dp, 100F.dp)
            .offset(y = offset.dp)
    )
}
