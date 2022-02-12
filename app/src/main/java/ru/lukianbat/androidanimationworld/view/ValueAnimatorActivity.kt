package ru.lukianbat.androidanimationworld.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.dynamicanimation.animation.FloatPropertyCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import ru.lukianbat.androidanimationworld.R

class ValueAnimatorActivity : Activity() {

    lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        image = findViewById(R.id.podlodkaImage)
        valueAnimatorTest()
    }

    private fun valueAnimatorTest() {
        // TODO 1 анимация с помощью ValueAnimator
        val startValue = 0F
        val endValue = resources.getDimensionPixelSize(R.dimen.test_offset).toFloat()

        ValueAnimator.ofFloat(startValue, endValue)
            .setDuration(3000)
            .apply {
                addUpdateListener {
                    val translation = it.animatedValue as Float
                    image.translationY = translation
                }
            }
            .start()
    }

    private fun objectAnimator() {
        // TODO 2 анимация с помощью ObjectAnimator
        ObjectAnimator.ofFloat(image, View.ALPHA, 0f, 1f)
            .setDuration(3000)
            .start()
    }

    private fun animatorSetTest() {
        // TODO 3 запуск нескольких анимаций с помощью AnimatorSet
        val startValue = 0F
        val endValue = resources.getDimensionPixelSize(R.dimen.test_offset).toFloat()

        val translationAnimator = ValueAnimator.ofFloat(startValue, endValue)
            .setDuration(3000)
            .apply {
                addUpdateListener {
                    val translation = it.animatedValue as Float
                    image.translationY = translation
                }
            }

        val alphaAnimator = ObjectAnimator.ofFloat(image, View.ALPHA, 0f, 1f)
            .setDuration(3000)

        AnimatorSet()
            .apply {
                playTogether(alphaAnimator, translationAnimator)
            }
            .start()

    }

    private fun springAnimationTest() {
        // TODO 4 запуск нескольких анимаций с физикой пружины с помощью SpringAnimation

        val mSpringTranslationXAnimation = SpringAnimation(image,
            object : FloatPropertyCompat<View>("translationX") {
                override fun getValue(view: View): Float {
                    return view.translationY
                }

                override fun setValue(view: View, value: Float) {
                    view.translationY = value
                }
            })
        val springForceY = SpringForce()
        springForceY.stiffness = SpringForce.STIFFNESS_MEDIUM
        springForceY.dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        mSpringTranslationXAnimation.spring = springForceY
        mSpringTranslationXAnimation.animateToFinalPosition(600F)
    }

}