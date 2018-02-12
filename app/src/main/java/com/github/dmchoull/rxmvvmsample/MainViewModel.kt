package com.github.dmchoull.rxmvvmsample

import android.arch.lifecycle.ViewModel
import android.support.annotation.VisibleForTesting
import com.github.dmchoull.rxmvvmsample.actions.ApiActions
import com.github.dmchoull.rxmvvmsample.eventbus.EventBus
import com.github.dmchoull.rxmvvmsample.models.WeatherConditions
import com.github.dmchoull.rxmvvmsample.reducers.AppState
import com.yheriatovych.reductor.Actions
import com.yheriatovych.reductor.Dispatcher
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class MainViewModel(
    private val dispatcher: Dispatcher,
    private val stateChanges: Observable<AppState>,
    private val eventBus: EventBus
) : ViewModel() {

    val throwable: Subject<Throwable> = PublishSubject.create()
    val searchTerm: Subject<City> = BehaviorSubject.create()
    val cityList: Subject<CityList> = BehaviorSubject.create()
    val inCityView: Subject<Boolean> = BehaviorSubject.create()
    val currentConditions: Subject<WeatherConditions> = BehaviorSubject.create()
    val cities: Array<City> = arrayOf()

    @VisibleForTesting
    internal val disposables = CompositeDisposable()

    private val apiActions = Actions.from(ApiActions::class.java)

    fun init() {
        disposables.addAll(
            eventBus.errors
                .subscribe({ t -> throwable.onNext(t) }),

            stateChanges.distinctUntilChanged()
                .subscribe { appState ->
                    appState?.inCityView?.let { inCityView.onNext(it) }
                    appState?.searchTerm?.let { searchTerm.onNext(it) }
                    appState?.cityList?.let { cityList.onNext(it)}
                    appState?.currentConditions?.let {currentConditions.onNext(it) }
                }
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public override fun onCleared() {
        disposables.clear()
    }

    fun search(cityQuery: String) {
        dispatcher.dispatch(apiActions.lookupCurrentWeather(cityQuery))
    }

    fun updateSearchTerm(text: SearchTerm) {
        dispatcher.dispatch(apiActions.lookupCities(text))
    }
}
