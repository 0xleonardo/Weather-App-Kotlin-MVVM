package hr.tvz.lstavaljladisic.weatherapp.model

import android.location.Location
import hr.tvz.lstavaljladisic.weatherapp.network.RequestCompleteListener
import hr.tvz.lstavaljladisic.weatherapp.model.forecast.ForecastHourInfoResponse
import hr.tvz.lstavaljladisic.weatherapp.model.weather.City
import hr.tvz.lstavaljladisic.weatherapp.model.weather.WeatherInfoResponse

interface WeatherInfoShowModel {
    fun getCityList(callback: RequestCompleteListener<MutableList<City>>)
    fun getWeatherInfo(cityId: Int, callback: RequestCompleteListener<WeatherInfoResponse>)
    fun getWeatherInfoCoordinates(location: Location, callback: RequestCompleteListener<WeatherInfoResponse>)
    fun getForecastInfo(cityId: Int, callback: RequestCompleteListener<ForecastHourInfoResponse>)
    fun getForecastInfoCoordinates(location: Location, callback: RequestCompleteListener<ForecastHourInfoResponse>)
}