package com.github.dmchoull.rxmvvmsample.reducers

import com.github.dmchoull.rxmvvmsample.City
import com.github.dmchoull.rxmvvmsample.CityList
import com.github.dmchoull.rxmvvmsample.SearchTerm
import com.github.dmchoull.rxmvvmsample.models.WeatherConditions

data class AppState(
    val searchTerm: SearchTerm?,
    val cityList: CityList?,
    val inCityView: Boolean?,
    val currentConditions: WeatherConditions?
)
