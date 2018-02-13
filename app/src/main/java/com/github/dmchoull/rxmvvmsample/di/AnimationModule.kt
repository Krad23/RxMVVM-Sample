package com.github.dmchoull.rxmvvmsample.di

import android.support.constraint.ConstraintLayout
import com.github.dmchoull.rxmvvmsample.MainActivity
import com.github.dmchoull.rxmvvmsample.MainActivity.Companion.LIST_ANIMATION_KEY
import com.github.dmchoull.rxmvvmsample.MainActivity.Companion.ROOT_LAYOUT_KEY
import com.github.dmchoull.rxmvvmsample.MainActivity.Companion.WEATHER_LAYOUT_KEY
import com.github.dmchoull.rxmvvmsample.R
import com.github.dmchoull.rxmvvmsample.animations.AnimationPair
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.androidActivityScope


val animationModule = Kodein.Module {

    bind<AnimationPair.Factory>() with scopedSingleton(androidActivityScope) {
        AnimationPair.Factory(it)
    }

}
