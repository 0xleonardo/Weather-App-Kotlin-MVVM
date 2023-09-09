package hr.tvz.lstavaljladisic.weatherapp.model

import android.content.Context
import android.location.Location
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.tvz.lstavaljladisic.weatherapp.network.RequestCompleteListener
import hr.tvz.lstavaljladisic.weatherapp.model.forecast.ForecastHourInfoResponse
import hr.tvz.lstavaljladisic.weatherapp.model.weather.City
import hr.tvz.lstavaljladisic.weatherapp.model.weather.WeatherInfoResponse
import hr.tvz.lstavaljladisic.weatherapp.network.OpenWeatherMapApiInterface
import hr.tvz.lstavaljladisic.weatherapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class WeatherInfoShowModelImpl(private val context: Context): WeatherInfoShowModel {

    override fun getCityList(callback: RequestCompleteListener<MutableList<City>>) {
        try {
            val inputStream = context.assets.open("city_list.json")

            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()

            val jsonString = String(buffer, charset("UTF-8"))

            val cityList = Gson().fromJson<List<City>>(jsonString, object : TypeToken<List<City>>() {}.type).toMutableList()

            callback.onRequestSuccess(cityList)

        } catch (e: IOException) {
            e.printStackTrace()
            callback.onRequestFailed(e.localizedMessage!!)
        }
    }

    override fun getWeatherInfo(cityId: Int, callback: RequestCompleteListener<WeatherInfoResponse>) {
        val apiInterface: OpenWeatherMapApiInterface = RetrofitClient.client.create(
            OpenWeatherMapApiInterface::class.java)
        val call: Call<WeatherInfoResponse> = apiInterface.callApiForWeatherInfo(cityId)

        call.enqueue(object : Callback<WeatherInfoResponse> {
            override fun onResponse(call: Call<WeatherInfoResponse>, response: Response<WeatherInfoResponse>) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }

            override fun onFailure(call: Call<WeatherInfoResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

    override fun getWeatherInfoCoordinates(location: Location, callback: RequestCompleteListener<WeatherInfoResponse>) {
        val apiInterface: OpenWeatherMapApiInterface = RetrofitClient.client.create(
            OpenWeatherMapApiInterface::class.java)
        val call: Call<WeatherInfoResponse> = apiInterface.callApiForWeatherInfoCoordinates(location.latitude, location.longitude)

        call.enqueue(object : Callback<WeatherInfoResponse> {
            override fun onResponse(call: Call<WeatherInfoResponse>, response: Response<WeatherInfoResponse>) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }

            override fun onFailure(call: Call<WeatherInfoResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

    override fun getForecastInfo(cityId: Int, callback: RequestCompleteListener<ForecastHourInfoResponse>) {
        val apiInterface: OpenWeatherMapApiInterface = RetrofitClient.client.create(
            OpenWeatherMapApiInterface::class.java)
        val call: Call<ForecastHourInfoResponse> = apiInterface.callApiForForecastInfo(cityId)

        call.enqueue(object : Callback<ForecastHourInfoResponse> {
            override fun onResponse(call: Call<ForecastHourInfoResponse>, response: Response<ForecastHourInfoResponse>) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }

            override fun onFailure(call: Call<ForecastHourInfoResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

    override fun getForecastInfoCoordinates(location: Location, callback: RequestCompleteListener<ForecastHourInfoResponse>) {
        val apiInterface: OpenWeatherMapApiInterface = RetrofitClient.client.create(
            OpenWeatherMapApiInterface::class.java)
        val call: Call<ForecastHourInfoResponse> = apiInterface.callApiForForecastInfoCoordinates(location.latitude, location.longitude)

        call.enqueue(object : Callback<ForecastHourInfoResponse> {
            override fun onResponse(call: Call<ForecastHourInfoResponse>, response: Response<ForecastHourInfoResponse>) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }

            override fun onFailure(call: Call<ForecastHourInfoResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }
}