package com.sdm.githubusersearch.animation

import android.animation.AnimatorSet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimationFactory @Inject constructor() {

    fun createAnimatorSet() = AnimatorSet()
}
