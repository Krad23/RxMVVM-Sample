package com.github.dmchoull.rxmvvmsample.adapters

import android.content.Context
import android.widget.ArrayAdapter
import com.github.dmchoull.rxmvvmsample.City
import com.github.dmchoull.rxmvvmsample.R

class CityAdapter(ctx: Context, list: Array<City>): ArrayAdapter<City>(ctx, R.layout.city_button, list)