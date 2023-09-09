package hr.tvz.lstavaljladisic.weatherapp.model.forecast

import com.google.gson.annotations.SerializedName

data class ForecastWeather (

    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("main")
    val main: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("icon")
    val icon: String = "",

)