package com.github.dmchoull.rxmvvmsample.actions

import com.yheriatovych.reductor.Action
import com.yheriatovych.reductor.annotations.ActionCreator

/**
 * Created by davorskontra on 13/02/2018.
 */

const val TOGGLE_WEATHER_VIEW = "TOGGLE_WEATHER_VIEW"

@ActionCreator
interface AnimationActions {

    @ActionCreator.Action(value = TOGGLE_WEATHER_VIEW)
    fun toggleWeather(show: Boolean): Action
}