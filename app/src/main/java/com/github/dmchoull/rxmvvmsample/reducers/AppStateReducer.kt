package com.github.dmchoull.rxmvvmsample.reducers

import com.github.dmchoull.rxmvvmsample.SearchTerm
import com.github.dmchoull.rxmvvmsample.actions.ApiActions
import com.github.dmchoull.rxmvvmsample.actions.CURRENT_WEATHER_RESPONSE
import com.github.dmchoull.rxmvvmsample.actions.LOOKUP_CITIES
import com.github.dmchoull.rxmvvmsample.actions.LOOKUP_CURRENT_WEATHER
import com.github.dmchoull.rxmvvmsample.api.WeatherResponse
import com.github.dmchoull.rxmvvmsample.models.WeatherConditions
import com.yheriatovych.reductor.Action
import com.yheriatovych.reductor.Reducer
import com.yheriatovych.reductor.annotations.AutoReducer

/**
 * Created by davorskontra on 12/02/2018.
 */

@AutoReducer
abstract class AppStateReducer : Reducer<AppState> {

    @AutoReducer.InitialState
    fun initialState() = AppState(
        null,
        null,
        null,
        null
    )

    @AutoReducer.Action(value = LOOKUP_CITIES, from = ApiActions::class)
    fun lookupCities(state: AppState, searchTerm: SearchTerm) =
        state.copy(cityList = arrayListOf("Zadar", "Helsinki", "Tokyo", searchTerm), inCityView = false)

    @AutoReducer.Action(value = LOOKUP_CURRENT_WEATHER, from = ApiActions::class)
    fun lookupCurrentWeather(state: AppState, searchTerm: SearchTerm) =
        state.copy(searchTerm = searchTerm, inCityView = true)

    @AutoReducer.Action(value = CURRENT_WEATHER_RESPONSE, from = ApiActions::class)
    fun currentWeatherResponse(state: AppState, response: WeatherResponse): AppState =
        state.copy(searchTerm = response.name, currentConditions = WeatherConditions.build(response))

    companion object {
        fun create(): AppStateReducer = AppStateReducerImpl()
    }
}