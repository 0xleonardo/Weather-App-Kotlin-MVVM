package hr.tvz.lstavaljladisic.weatherapp.model.forecast

import com.google.gson.annotations.SerializedName

data class ForecastRain (

    @SerializedName("3h")
    val value: Double = 0.0

)