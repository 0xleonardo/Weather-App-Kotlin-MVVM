package hr.tvz.lstavaljladisic.weatherapp.model.forecast

import com.google.gson.annotations.SerializedName

data class ForecastCity (
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("coord")
    val coord: ForecastCoord = ForecastCoord(),
    @SerializedName("country")
    val country: String = "",
    @SerializedName("population")
    val population: Int = 0,
    @SerializedName("timezone")
    val timezone: Int = 0,
    @SerializedName("sunrise")
    val sunrise: Int = 0,
    @SerializedName("sunset")
    val sunset: Int = 0
)