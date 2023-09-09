package hr.tvz.lstavaljladisic.weatherapp.model.forecast

import com.google.gson.annotations.SerializedName

data class ForecastWind(
    @SerializedName("speed")
    val speed: Double = 0.0,
    @SerializedName("deg")
    val deg: Int = 0,
    @SerializedName("gust")
    val gust: Double = 0.0
)
