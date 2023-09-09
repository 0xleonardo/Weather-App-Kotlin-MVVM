package hr.tvz.lstavaljladisic.weatherapp.model.weather

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Country(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("dial_code")
    val dialCode: String = "",
    @SerializedName("code")
    val code: String = ""
): Serializable