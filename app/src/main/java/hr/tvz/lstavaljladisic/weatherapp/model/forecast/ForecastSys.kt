package hr.tvz.lstavaljladisic.weatherapp.model.forecast

import com.google.gson.annotations.SerializedName

data class ForecastSys(
    @SerializedName("pod")
    val pod: String = "",
)
