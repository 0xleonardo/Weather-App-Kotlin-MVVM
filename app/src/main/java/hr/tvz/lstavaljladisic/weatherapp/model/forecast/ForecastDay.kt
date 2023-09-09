package hr.tvz.lstavaljladisic.weatherapp.model.forecast

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ForecastDay(
    var dateTime: Int = 0,
    var temperature: String = "0",
    var weatherConditionIconUrl: String = "",
    var weatherConditionIconDescription: String = "",
    var cityAndState : String = "",
    var visibility : String = "",
    var pressure : String = "",
    var wind : String = "",
    var humidity : String = "",
    var forecastHour : List<ForecastHour> = listOf()
) : Parcelable