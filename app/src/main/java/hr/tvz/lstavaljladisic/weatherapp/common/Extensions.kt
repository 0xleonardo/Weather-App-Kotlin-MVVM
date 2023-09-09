package hr.tvz.lstavaljladisic.weatherapp.utils

import hr.tvz.lstavaljladisic.weatherapp.model.weather.City
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*

fun Int.toDateTimeString() : String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    val date = Date(this * 1000L)

    return dateFormat.format(date)
}

fun Int.isTimestampInOffset(unixTimestamp: Int, offset: Int) : Boolean {
    val date = Instant.ofEpochSecond(this.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
    val dateToCheck = Instant.ofEpochSecond(unixTimestamp.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

    return date.minusDays(offset.toLong()).isBefore(dateToCheck)
}

fun Int.toTimeString() : String {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val date = Date(this * 1000L)

    return dateFormat.format(date)
}

fun Int.toDayString() : String {
    val dateFormat = SimpleDateFormat("dd", Locale.getDefault())

    val date = Date(this * 1000L)

    return dateFormat.format(date)
}

fun Int.toDayNameString() : String {
    val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())

    val date = Date(this * 1000L)

    return dateFormat.format(date)
}

fun MutableList<City>.toListOfCityNames() : MutableList<String> {
    return this.map { city -> city.name }.toMutableList()
}

fun MutableList<City>.toListOfCityNamesLowercase() : MutableList<String> {
    return this.map { city -> city.name.lowercase() }.toMutableList()
}

fun Double.kelvinToCelsius() : Int {
    return  (this - 273.15).toInt()
}