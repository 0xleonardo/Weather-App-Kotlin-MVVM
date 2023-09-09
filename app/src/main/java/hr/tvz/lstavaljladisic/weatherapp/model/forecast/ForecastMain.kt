package hr.tvz.lstavaljladisic.weatherapp.model.forecast

import com.google.gson.annotations.SerializedName

data class ForecastMain (
        @SerializedName("temp")
        val temp: Double = 0.0,
        @SerializedName("feels_like")
        val feels_like: Double = 0.0,
        @SerializedName("temp_min")
        val temp_min: Double = 0.0,
        @SerializedName("temp_max")
        val temp_max: Double = 0.0,
        @SerializedName("pressure")
        val pressure: Int = 0,
        @SerializedName("sea_level")
        val sea_level: Int = 0,
        @SerializedName("grnd_level")
        val grnd_level: Int = 0,
        @SerializedName("humidity")
        val humidity: Int = 0,
        @SerializedName("temp_kf")
        val temp_kf: Double = 0.0
)