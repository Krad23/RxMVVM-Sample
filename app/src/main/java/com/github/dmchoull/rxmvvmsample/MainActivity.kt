package com.github.dmchoull.rxmvvmsample

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.github.dmchoull.rxmvvmsample.adapters.CityAdapter
import com.github.dmchoull.rxmvvmsample.animations.AnimationPair
import com.github.dmchoull.rxmvvmsample.models.WeatherConditions
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.android.KodeinAppCompatActivity
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : KodeinAppCompatActivity() {
    internal val viewModel: MainViewModel by with(this as Activity).instance()

    @VisibleForTesting
    internal val disposables = CompositeDisposable()

    private val dateFormat = SimpleDateFormat("h:mm a z", Locale.US)
    private val cityAdapter: CityAdapter by with(this as Activity).instance()
    private val animationPairFactory: AnimationPair.Factory by with(this as Activity).instance()

    private val weatherAnimationPair by lazy {
        animationPairFactory.create(mainActivityRoot, R.layout.activity_main_weather)
    }

    override fun provideOverridingModule() = Kodein.Module {
        bind<MainActivity>() with instance(this@MainActivity)
    }

    @SuppressLint("MissingSuperCall") // FIXME: why does it think super call is missing?
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.init()
    }

    override fun onStart() {
        super.onStart()
        disposables.addAll(
            RxTextView.textChanges(cityQuery)
                .subscribe { text ->
                    viewModel.updateSearchTerm(text.toString())
                },

            viewModel.inCityView
                .observeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .subscribe { showWeather ->
                    Timber.d("--> show weather: $showWeather")
                    weatherAnimationPair.animate(showWeather)
                },

            viewModel.searchTerm
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { city ->
                    currentConditionsTitle.visibility = View.VISIBLE
                    currentConditionsTitle.text = getString(R.string.current_conditions, city)
                },

            viewModel.currentConditions
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { _ -> onRequestCompleted() }
                .subscribe(this::updateConditions),

            viewModel.throwable
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { t -> Timber.e(t) }
                .doOnNext { _ -> onRequestCompleted() }
                .subscribe(this::showError)
        )
    }


    override fun onStop() {
        disposables.clear()
        super.onStop()
    }

    private fun onRequestStarted() {
        cityQuery.setText("")
        loadingIndicator.visibility = View.VISIBLE
    }

    private fun onRequestCompleted() {
        loadingIndicator.visibility = View.GONE
    }

    private fun updateConditions(conditions: WeatherConditions) {
        displayField(
            currentTempLabel,
            currentTemperature,
            getString(R.string.temperature_c, conditions.temp)
        )
        displayField(pressureLabel, pressure, conditions.pressure.toString())
        displayField(humidityLabel, humidity, conditions.humidity.toString())
        displayField(sunriseLabel, sunrise, dateFormat.format(conditions.sunrise))
        displayField(sunsetLabel, sunset, dateFormat.format(conditions.sunset))
    }

    private fun displayField(label: TextView, textField: TextView, value: String) {
        label.visibility = View.VISIBLE
        textField.visibility = View.VISIBLE
        textField.text = value
    }

    private fun showError(throwable: Throwable) {
        val message = when (throwable) {
            is HttpException -> if (throwable.code() == 404) R.string.city_not_found else R.string.bad_request
            is IOException -> R.string.connection_failed
            else -> R.string.something_went_wrong
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val LIST_ANIMATION_KEY = "LIST_ANIMATION_KEY"
        const val ROOT_LAYOUT_KEY = "ROOT_LAYOUT_KEY"
        const val WEATHER_LAYOUT_KEY = "WEATHER_LAYOUT_KEY"
    }

}
