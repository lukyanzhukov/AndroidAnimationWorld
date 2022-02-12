package ru.lukianbat.macrobenchmark

import android.content.Intent
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AnimationPerfomanceBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun checkComposeAnimation() = benchmarkRule.measureRepeated(
        packageName = "ru.lukianbat.androidanimationworld",
        metrics = listOf(FrameTimingMetric()),
        startupMode = StartupMode.WARM,
        iterations = 10,
    ) {
        pressHome()
        val intent = Intent("$packageName.TEST_COMPOSE_ACTION")
        startActivityAndWait(intent)
        Thread.sleep(1000L)
    }

    @Test
    fun checkViewAnimation() = benchmarkRule.measureRepeated(
        packageName = "ru.lukianbat.androidanimationworld",
        metrics = listOf(FrameTimingMetric()),
        startupMode = StartupMode.WARM,
        iterations = 10,
    ) {
        pressHome()
        val intent = Intent("$packageName.TEST_VIEW_ACTION")
        startActivityAndWait(intent)
        Thread.sleep(1000L)
    }
}