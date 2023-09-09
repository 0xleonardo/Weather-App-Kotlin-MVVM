package hr.tvz.lstavaljladisic.weatherapp.network

import hr.tvz.lstavaljladisic.weatherapp.model.forecast.ForecastHourInfoResponse
import hr.tvz.lstavaljladisic.weatherapp.model.weather.WeatherInfoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApiInterface {
    @GET("weather")
    fun callApiForWeatherInfo(@Query("id") cityId: Int): Call<WeatherInfoResponse>

    @GET("weather")
    fun callApiForWeatherInfoCoordinates(@Query("lat") latitude: Double, @Query("lon") longitude: Double): Call<WeatherInfoResponse>

    @GET("forecast")
    fun callApiForForecastInfo(@Query("id") cityId: Int): Call<ForecastHourInfoResponse>

    @GET("forecast")
    fun callApiForForecastInfoCoordinates(@Query("lat") latitude: Double, @Query("lon") longitude: Double): Call<ForecastHourInfoResponse>
}