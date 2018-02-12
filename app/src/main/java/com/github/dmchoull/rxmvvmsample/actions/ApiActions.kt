package com.github.dmchoull.rxmvvmsample.actions

import com.github.dmchoull.rxmvvmsample.City
import com.github.dmchoull.rxmvvmsample.CityList
import com.github.dmchoull.rxmvvmsample.SearchTerm
import com.github.dmchoull.rxmvvmsample.api.WeatherResponse
import com.yheriatovych.reductor.Action
import com.yheriatovych.reductor.annotations.ActionCreator

const val LOOKUP_CURRENT_WEATHER = "LOOKUP_CURRENT_WEATHER"
const val CURRENT_WEATHER_RESPONSE = "CURRENT_WEATHER_RESPONSE"
const val CURRENT_CITIES_RESPONSE = "CURRENT_CITIES_RESPONSE"
const val LOOKUP_CITIES = "LOOKUP_CITIES"

@ActionCreator
interface ApiActions {

    @ActionCreator.Action(value = LOOKUP_CITIES)
    fun lookupCities(searchTerm: SearchTerm): Action

    @ActionCreator.Action(value = LOOKUP_CURRENT_WEATHER)
    fun lookupCurrentWeather(city: City): Action

    @ActionCreator.Action(value = CURRENT_WEATHER_RESPONSE)
    fun currentWeatherResponse(response: WeatherResponse): Action
}
