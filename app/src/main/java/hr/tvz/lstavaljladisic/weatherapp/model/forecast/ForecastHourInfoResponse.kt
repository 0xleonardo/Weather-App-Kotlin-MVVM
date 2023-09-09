package hr.tvz.lstavaljladisic.weatherapp.model.forecast

import com.google.gson.annotations.SerializedName

data class ForecastHourInfoResponse(

    @SerializedName("cod")
    val cod: String = "",
    @SerializedName("message")
    val message: Int = 0,
    @SerializedName("cnt")
    val cnt: Int = 0,
    @SerializedName("list")
    val list: List<Forecast> = listOf(),
    @SerializedName("city")
    val city: ForecastCity = ForecastCity()
    )