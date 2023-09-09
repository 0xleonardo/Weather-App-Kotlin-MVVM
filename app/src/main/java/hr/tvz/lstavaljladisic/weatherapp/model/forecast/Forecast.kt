package hr.tvz.lstavaljladisic.weatherapp.model.forecast

import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("dt")
    val dt: Int = 0,
    @SerializedName("main")
    val main: ForecastMain = ForecastMain(),
    @SerializedName("weather")
    val weather: List<ForecastWeather> = listOf(),
    @SerializedName("clouds")
    val clouds: ForecastClouds = ForecastClouds(),
    @SerializedName("wind")
    val wind: ForecastWind = ForecastWind(),
    @SerializedName("visibility")
    val visibility: Int = 0,
    @SerializedName("pop")
    val pop: Double = 0.0,
    @SerializedName("rain")
    val rain: ForecastRain? = ForecastRain(),
    @SerializedName("sys")
    val sys: ForecastSys = ForecastSys(),
    @SerializedName("dt_txt")
    val dt_txt: String = ""
    )