package hr.tvz.lstavaljladisic.weatherapp.view

import ForecastDayAdapter
import ForecastHourAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import hr.tvz.lstavaljladisic.weatherapp.R
import hr.tvz.lstavaljladisic.weatherapp.common.WeatherWidgetProvider
import hr.tvz.lstavaljladisic.weatherapp.databinding.ActivityMainBinding
import hr.tvz.lstavaljladisic.weatherapp.model.WeatherInfoShowModel
import hr.tvz.lstavaljladisic.weatherapp.model.WeatherInfoShowModelImpl
import hr.tvz.lstavaljladisic.weatherapp.model.weather.City
import hr.tvz.lstavaljladisic.weatherapp.model.weather.WeatherData
import hr.tvz.lstavaljladisic.weatherapp.utils.toListOfCityNames
import hr.tvz.lstavaljladisic.weatherapp.utils.toListOfCityNamesLowercase
import hr.tvz.lstavaljladisic.weatherapp.viewmodel.WeatherInfoViewModel


class WeatherInfoFragment : Fragment() {

    private val PERMISSION_REQUEST_CODE = 1

    private lateinit var binding: ActivityMainBinding

    private lateinit var model: WeatherInfoShowModel
    private lateinit var viewModel: WeatherInfoViewModel
    private lateinit var mediaPlayer: MediaPlayer

    private var cityList: MutableList<City> = mutableListOf()
    private var canSearchWeather = MutableLiveData(false)

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        lateinit var weatherData: WeatherData
    }

    enum class WeatherType {
        OK,
        RAIN,
        SNOW,
        THUNDER
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityMainBinding.inflate(inflater,container,false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        model = WeatherInfoShowModelImpl(requireContext())
        viewModel = ViewModelProviders.of(this).get(WeatherInfoViewModel::class.java)

        setLiveDataListeners()
        setViewClickListener()

        tryGettingLocationData()
        viewModel.getCityList(model)

        return binding.root
    }

    private fun setViewClickListener() {
        binding.layoutInput.btnViewWeather.setOnClickListener {

            if (binding.layoutInput.cityDropdown.error == null) {
                val cityName = binding.layoutInput.cityDropdown.text.toString()
                val cityToSearch = cityList.firstOrNull { city -> city.name.lowercase() == cityName.lowercase() }

                if (cityToSearch != null) {
                    viewModel.getWeatherInfo(cityToSearch.id, model)
                    viewModel.getForecastHourInfo(cityToSearch.id, model)
                }
            }
        }

        binding.layoutInput.btnViewWeather2.setOnClickListener {
            tryGettingLocationData()
        }

        binding.layoutInput.cityDropdown.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!cityList.toListOfCityNamesLowercase().contains(s.toString().lowercase())) {
                    binding.layoutInput.cityDropdown.error = "No city found"
                    canSearchWeather.postValue(false)
                }
                else {
                    binding.layoutInput.cityDropdown.error = null
                    canSearchWeather.postValue(true)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun setLiveDataListeners() {
        viewModel.cityListLiveData.observe(this) { cities -> setCityListDropdown(cities) }

        viewModel.cityListFailureLiveData.observe(this) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
        }

        canSearchWeather.observe(this) { searchEnabled ->
            binding.layoutInput.btnViewWeather.isEnabled = searchEnabled
        }

        viewModel.progressBarLiveData.observe(this) { isShowLoader ->
            if (isShowLoader)
                binding.progressBar.visibility = View.VISIBLE
            else
                binding.progressBar.visibility = View.GONE
        }

        viewModel.weatherInfoLiveData.observe(this) { weather ->
            weatherData = weather
            setWeatherInfo(weather)
            playAudioBasedOnWeatherDescription(weather.weatherConditionIconDescription)
            requireContext().sendBroadcast(Intent(WeatherWidgetProvider().UPDATE_WIDGET))
        }

        viewModel.forecastInfoHourLiveData.observe(this) { forecast_list ->
            val horizontalLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.layoutForecast.forecastList.layoutManager = horizontalLayoutManager
            val adapter = ForecastHourAdapter(requireContext(), forecast_list)
            binding.layoutForecast.forecastList.adapter = adapter
        }

        viewModel.forecastInfoDailyLiveData.observe(this) { forecast_list ->
            val horizontalLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.layoutForecastDaily.forecastDailyList.layoutManager = horizontalLayoutManager
            val adapter = ForecastDayAdapter(requireContext(), forecast_list)

            adapter.setClickListener(object : ForecastDayAdapter.ItemClickListener {
                override fun onItemClick(view: View?, position: Int) {
                    val bundle = Bundle()
                    bundle.putParcelable("forecastDay", forecast_list[position])
                    val fragment = ForecastDayInfoFragment()
                    fragment.arguments = bundle

                    fragmentManager!!
                        .beginTransaction()
                        .add(R.id.show_fragment, fragment)
                        .addToBackStack("forecastDayInfo")
                        .commit()
                }
            })

            binding.layoutForecastDaily.forecastDailyList.adapter = adapter
        }

        viewModel.weatherInfoFailureLiveData.observe(this) { errorMessage ->
            binding.outputGroup.visibility = View.GONE
            binding.tvErrorMessage.visibility = View.VISIBLE
            binding.tvErrorMessage.text = errorMessage
        }
    }


    private fun setCityListDropdown(cityList: MutableList<City>) {
        this.cityList = cityList

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, this.cityList.toListOfCityNames())
        binding.layoutInput.cityDropdown.setAdapter(adapter)
    }

    private fun setWeatherInfo(weatherData: WeatherData) {
        binding.outputGroup.visibility = View.VISIBLE
        binding.tvErrorMessage.visibility = View.GONE

        binding.layoutWeatherBasic.widgetDatetime.text = weatherData.dateTime
        binding.layoutWeatherBasic.widgetTemperature.text = weatherData.temperature
        binding.layoutWeatherBasic.widgetCity.text = weatherData.cityAndCountry
        Glide.with(this).load(weatherData.weatherConditionIconUrl).into(binding.layoutWeatherBasic.widgetCondition)
        binding.layoutWeatherBasic.widgetConditionText.text = weatherData.weatherConditionIconDescription

        binding.layoutWeatherAdditionalIno.humidityValue.text = weatherData.humidity
        binding.layoutWeatherAdditionalIno.pressureValue.text = weatherData.pressure
        binding.layoutWeatherAdditionalIno.visibilityValue.text = weatherData.visibility
        binding.layoutWeatherAdditionalIno.windValue.text = weatherData.windSpeed

        binding.layoutSunsetSunrise.tvSunriseTime.text = weatherData.sunrise
        binding.layoutSunsetSunrise.tvSunsetTime.text = weatherData.sunset
    }

    private fun tryGettingLocationData() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
        }
        else {
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
                override fun isCancellationRequested() = false
            })
                .addOnSuccessListener { location: Location? ->
                    if (location == null)
                        Toast.makeText(requireContext(), "Cannot get location.", Toast.LENGTH_SHORT).show()
                    else {
                        viewModel.getWeatherInfoCoordinates(location, model)
                        viewModel.getForecastHourInfoCoordinates(location, model)
                    }

                }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    tryGettingLocationData()
                } else {
                    Toast.makeText(requireContext(), "We cannot load weather for your current location without permission to use your location!", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    private fun playAudioBasedOnWeatherDescription(weatherDescription: String) {
        when (weatherDescription.lowercase()) {
            "clear sky" -> playAudio(WeatherType.OK)
            "overcast clouds" -> playAudio(WeatherType.OK)
            "scattered clouds" -> playAudio(WeatherType.OK)
            "broken clouds" -> playAudio(WeatherType.OK)
            "few clouds" -> playAudio(WeatherType.OK)
            "shower rain" -> playAudio(WeatherType.RAIN)
            "rain" -> playAudio(WeatherType.RAIN)
            "thunderstorm" -> playAudio(WeatherType.THUNDER)
            "snow" -> playAudio(WeatherType.RAIN)
            "mist" -> playAudio(WeatherType.OK)
            else -> {
                playAudio(WeatherType.OK)
            }
        }
    }

    private fun playAudio(type: WeatherType) {
        when (type) {
            WeatherType.SNOW -> {
                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.weather_snow)
                mediaPlayer.start()
            }
            WeatherType.RAIN -> {
                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.weather_rain)
                mediaPlayer.start()
            }
            WeatherType.THUNDER -> {
                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.weather_thunder)
                mediaPlayer.start()
            }
            else -> {
                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.weather_ok)
                mediaPlayer.start()
            }
        }
    }
}