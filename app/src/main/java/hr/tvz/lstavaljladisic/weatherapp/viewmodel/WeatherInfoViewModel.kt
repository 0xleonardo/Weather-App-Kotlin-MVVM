package hr.tvz.lstavaljladisic.weatherapp.viewmodel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.tvz.lstavaljladisic.weatherapp.network.RequestCompleteListener
import hr.tvz.lstavaljladisic.weatherapp.model.WeatherInfoShowModel
import hr.tvz.lstavaljladisic.weatherapp.model.forecast.ForecastDay
import hr.tvz.lstavaljladisic.weatherapp.model.forecast.ForecastHourInfoResponse
import hr.tvz.lstavaljladisic.weatherapp.model.forecast.ForecastHour
import hr.tvz.lstavaljladisic.weatherapp.model.weather.City
import hr.tvz.lstavaljladisic.weatherapp.model.weather.WeatherData
import hr.tvz.lstavaljladisic.weatherapp.model.weather.WeatherInfoResponse
import hr.tvz.lstavaljladisic.weatherapp.utils.*

class WeatherInfoViewModel : ViewModel() {

    val cityListLiveData = MutableLiveData<MutableList<City>>()
    val cityListFailureLiveData = MutableLiveData<String>()

    val weatherInfoLiveData = MutableLiveData<WeatherData>()
    val weatherInfoFailureLiveData = MutableLiveData<String>()

    val forecastInfoHourLiveData = MutableLiveData<MutableList<ForecastHour>>()
    val forecastInfoHourFailureLiveData = MutableLiveData<String>()

    val forecastInfoDailyLiveData = MutableLiveData<MutableList<ForecastDay>>()
    val forecastInfoDailyFailureLiveData = MutableLiveData<String>()

    val progressBarLiveData = MutableLiveData<Boolean>()

    fun getCityList(model: WeatherInfoShowModel) {

        model.getCityList(object :
            RequestCompleteListener<MutableList<City>> {
            override fun onRequestSuccess(data: MutableList<City>) {
                cityListLiveData.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String) {
                cityListFailureLiveData.postValue(errorMessage)
            }
        })
    }

    fun getWeatherInfo(cityId: Int, model: WeatherInfoShowModel) {

        progressBarLiveData.postValue(true)

        model.getWeatherInfo(cityId, object :
            RequestCompleteListener<WeatherInfoResponse> {
            override fun onRequestSuccess(data: WeatherInfoResponse) {

                val weatherData = WeatherData(
                    dateTime = data.dt.toDateTimeString(),
                    temperature = data.main.temp.kelvinToCelsius().toString(),
                    cityAndCountry = "${data.name}, ${data.sys.country}",
                    weatherConditionIconUrl = "http://openweathermap.org/img/wn/${data.weather[0].icon}.png",
                    weatherConditionIconDescription = data.weather[0].description,
                    humidity = "${data.main.humidity}%",
                    pressure = "${data.main.pressure} mBar",
                    visibility = "${data.visibility/1000.0} KM",
                    sunrise = data.sys.sunrise.toTimeString(),
                    sunset = data.sys.sunset.toTimeString(),
                    windSpeed = "${ data.wind.speed } km/h"
                )

                progressBarLiveData.postValue(false)
                weatherInfoLiveData.postValue(weatherData)
            }

            override fun onRequestFailed(errorMessage: String) {
                progressBarLiveData.postValue(false)
                weatherInfoFailureLiveData.postValue(errorMessage)
            }
        })
    }

    fun getWeatherInfoCoordinates(location: Location, model: WeatherInfoShowModel) {

        progressBarLiveData.postValue(true)

        model.getWeatherInfoCoordinates(location, object :
            RequestCompleteListener<WeatherInfoResponse> {
            override fun onRequestSuccess(data: WeatherInfoResponse) {

                val weatherData = WeatherData(
                    dateTime = data.dt.toDateTimeString(),
                    temperature = data.main.temp.kelvinToCelsius().toString(),
                    cityAndCountry = "${data.name}, ${data.sys.country}",
                    weatherConditionIconUrl = "http://openweathermap.org/img/wn/${data.weather[0].icon}.png",
                    weatherConditionIconDescription = data.weather[0].description,
                    humidity = "${data.main.humidity}%",
                    pressure = "${data.main.pressure} mBar",
                    visibility = "${data.visibility/1000.0} km",
                    windSpeed = "${ data.wind.speed } km/h",
                    sunrise = data.sys.sunrise.toTimeString(),
                    sunset = data.sys.sunset.toTimeString()
                )

                progressBarLiveData.postValue(false)
                weatherInfoLiveData.postValue(weatherData)
            }

            override fun onRequestFailed(errorMessage: String) {
                progressBarLiveData.postValue(false)
                weatherInfoFailureLiveData.postValue(errorMessage)
            }
        })
    }

    fun getForecastHourInfo(cityId: Int, model: WeatherInfoShowModel) {

        progressBarLiveData.postValue(true)

        model.getForecastInfo(cityId, object :
            RequestCompleteListener<ForecastHourInfoResponse> {
            override fun onRequestSuccess(data: ForecastHourInfoResponse) {
                val forecastForToday = mutableListOf<ForecastHour>()
                val forecastDay = mutableListOf<ForecastDay>()
                var dailyData = HashMap<String, ArrayList<ForecastDay>>()
                val hourData = HashMap<String, ArrayList<ForecastHour>>()
                val initialData = data.list[0]

                data.list.forEach {
                    if (it.dt.toDayString() == initialData.dt.toDayString() || it.dt.isTimestampInOffset(initialData.dt, 1)) {
                        forecastForToday.add(
                            ForecastHour(
                                dateTime = it.dt.toTimeString(),
                                temperature = it.main.temp.kelvinToCelsius().toString(),
                                weatherConditionIconUrl = "http://openweathermap.org/img/wn/${it.weather[0].icon}.png",
                                weatherConditionIconDescription = it.weather[0].description
                            )
                        )
                    }

                    val dateTime = it.dt_txt
                    val date = dateTime.substring(0, 10)

                    if (hourData.containsKey(date)) {
                        hourData[date]?.add(ForecastHour(
                            it.dt.toTimeString(),
                            it.main.temp.kelvinToCelsius().toString(),
                            "http://openweathermap.org/img/wn/${it.weather[0].icon}.png",
                            it.weather[0].description,
                        ))
                    } else {
                        hourData[date] = ArrayList(listOf(ForecastHour(
                            it.dt.toTimeString(),
                            it.main.temp.kelvinToCelsius().toString(),
                            "http://openweathermap.org/img/wn/${it.weather[0].icon}.png",
                            it.weather[0].description,
                        )))
                    }
                }

                data.list.forEach {
                    val dateTime = it.dt_txt
                    val date = dateTime.substring(0, 10)

                    if (dailyData.containsKey(date)) {
                        dailyData[date]?.add(ForecastDay(
                            it.dt,
                            it.main.temp.kelvinToCelsius().toString(),
                            "http://openweathermap.org/img/wn/${it.weather[0].icon}.png",
                            it.weather[0].description,
                            data.city.name + ", " + data.city.country,
                            "${it.visibility/1000.0} km",
                            "${it.main.pressure} mBar",
                            "${ it.wind.speed } km/h",
                            "${it.main.humidity}%",
                        ))
                    } else {
                        dailyData[date] = ArrayList(listOf(ForecastDay(
                            it.dt,
                            it.main.temp.kelvinToCelsius().toString(),
                            "http://openweathermap.org/img/wn/${it.weather[0].icon}.png",
                            it.weather[0].description,
                            data.city.name + ", " + data.city.country,
                            "${it.visibility/1000.0} km",
                            "${it.main.pressure} mBar",
                            "${ it.wind.speed } km/h",
                            "${it.main.humidity}%",
                        )))
                    }
                }

                dailyData = dailyData.toList()
                    .sortedBy { it.first }
                    .filter { it.first != data.list[0].dt_txt.substring(0, 10) }
                    .toMap(LinkedHashMap())

                dailyData.forEach {
                    val median = it.value[it.value.size/2]
                    median.forecastHour = hourData[it.key]!!.toList()
                    forecastDay.add(median)
                }

                progressBarLiveData.postValue(false)
                forecastInfoHourLiveData.postValue(forecastForToday)
                forecastInfoDailyLiveData.postValue(forecastDay)
            }

            override fun onRequestFailed(errorMessage: String) {
                progressBarLiveData.postValue(false)
                forecastInfoHourFailureLiveData.postValue(errorMessage)
                forecastInfoDailyFailureLiveData.postValue(errorMessage)
            }
        })
    }

    fun getForecastHourInfoCoordinates(location: Location, model: WeatherInfoShowModel) {

        progressBarLiveData.postValue(true)

        model.getForecastInfoCoordinates(location, object :
            RequestCompleteListener<ForecastHourInfoResponse> {
            override fun onRequestSuccess(data: ForecastHourInfoResponse) {
                val forecastHourForToday = mutableListOf<ForecastHour>()
                val forecastDay = mutableListOf<ForecastDay>()
                var dailyData = HashMap<String, ArrayList<ForecastDay>>()
                val hourData = HashMap<String, ArrayList<ForecastHour>>()
                val initialData = data.list[0]

                data.list.forEach {
                    if (it.dt.toDayString() == initialData.dt.toDayString() || it.dt.isTimestampInOffset(initialData.dt, 1)) {
                        forecastHourForToday.add(
                            ForecastHour(
                                dateTime = it.dt.toTimeString(),
                                temperature = it.main.temp.kelvinToCelsius().toString(),
                                weatherConditionIconUrl = "http://openweathermap.org/img/wn/${it.weather[0].icon}.png",
                                weatherConditionIconDescription = it.weather[0].description
                            )
                        )
                    }

                    val dateTime = it.dt_txt
                    val date = dateTime.substring(0, 10)

                    if (hourData.containsKey(date)) {
                        hourData[date]?.add(ForecastHour(
                            it.dt.toTimeString(),
                            it.main.temp.kelvinToCelsius().toString(),
                            "http://openweathermap.org/img/wn/${it.weather[0].icon}.png",
                            it.weather[0].description,
                        ))
                    } else {
                        hourData[date] = ArrayList(listOf(ForecastHour(
                            it.dt.toTimeString(),
                            it.main.temp.kelvinToCelsius().toString(),
                            "http://openweathermap.org/img/wn/${it.weather[0].icon}.png",
                            it.weather[0].description,
                        )))
                    }
                }

                data.list.forEach {
                    val dateTime = it.dt_txt
                    val date = dateTime.substring(0, 10)

                    if (dailyData.containsKey(date)) {
                        dailyData[date]?.add(ForecastDay(
                            it.dt,
                            it.main.temp.kelvinToCelsius().toString(),
                            "http://openweathermap.org/img/wn/${it.weather[0].icon}.png",
                            it.weather[0].description,
                            data.city.name + ", " + data.city.country,
                            "${it.visibility/1000.0} km",
                            "${it.main.pressure} mBar",
                            "${ it.wind.speed } km/h",
                            "${it.main.humidity}%",

                        ))
                    } else {
                        dailyData[date] = ArrayList(listOf(ForecastDay(
                            it.dt,
                            it.main.temp.kelvinToCelsius().toString(),
                            "http://openweathermap.org/img/wn/${it.weather[0].icon}.png",
                            it.weather[0].description,
                            data.city.name + ", " + data.city.country,
                            "${it.visibility/1000.0} km",
                            "${it.main.pressure} mBar",
                            "${ it.wind.speed } km/h",
                            "${it.main.humidity}%",
                        )))
                    }
                }

                dailyData = dailyData.toList()
                    .sortedBy { it.first }
                    .filter { it.first != data.list[0].dt_txt.substring(0, 10) }
                    .toMap(LinkedHashMap())

                dailyData.forEach {
                    val median = it.value[it.value.size/2]
                    median.forecastHour = hourData[it.key]!!.toList()
                    forecastDay.add(median)
                }

                progressBarLiveData.postValue(false)
                forecastInfoHourLiveData.postValue(forecastHourForToday)
                forecastInfoDailyLiveData.postValue(forecastDay)
            }

            override fun onRequestFailed(errorMessage: String) {
                progressBarLiveData.postValue(false)
                forecastInfoHourFailureLiveData.postValue(errorMessage)
                forecastInfoDailyFailureLiveData.postValue(errorMessage)
            }
        })
    }
}