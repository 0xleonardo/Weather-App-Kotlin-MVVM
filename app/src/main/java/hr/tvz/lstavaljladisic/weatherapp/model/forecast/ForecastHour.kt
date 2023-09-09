package hr.tvz.lstavaljladisic.weatherapp.model.forecast

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ForecastHour(
    var dateTime: String = "",
    var temperature: String = "0",
    var weatherConditionIconUrl: String = "",
    var weatherConditionIconDescription: String = "",
) : Parcelable
