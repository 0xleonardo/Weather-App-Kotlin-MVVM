package hr.tvz.lstavaljladisic.weatherapp.model.forecast

import com.google.gson.annotations.SerializedName

data class ForecastClouds (
    @SerializedName("all")
    val all: Int = 0,
)



