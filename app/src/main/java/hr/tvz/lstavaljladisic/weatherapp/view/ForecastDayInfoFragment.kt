package hr.tvz.lstavaljladisic.weatherapp.view

import ForecastHourAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import hr.tvz.lstavaljladisic.weatherapp.databinding.LayoutForecastDailyDetailBinding
import hr.tvz.lstavaljladisic.weatherapp.model.forecast.ForecastDay
import hr.tvz.lstavaljladisic.weatherapp.utils.toDateTimeString
import hr.tvz.lstavaljladisic.weatherapp.utils.toDayNameString

class ForecastDayInfoFragment : Fragment() {

    private lateinit var binding: LayoutForecastDailyDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LayoutForecastDailyDetailBinding.inflate(inflater,container,false);

        val forecastDay = requireArguments().getParcelable<ForecastDay>("forecastDay")

        binding.dayName.text = forecastDay!!.dateTime.toDayNameString()

        binding.basicInfoLayout.widgetCity.text = forecastDay.cityAndState
        binding.basicInfoLayout.widgetDatetime.text = forecastDay.dateTime.toDateTimeString()
        binding.basicInfoLayout.widgetTemperature.text = forecastDay.temperature
        Glide.with(this).load(forecastDay.weatherConditionIconUrl).into(binding.basicInfoLayout.widgetCondition)
        binding.basicInfoLayout.widgetConditionText.text = forecastDay.weatherConditionIconDescription

        binding.additionalInfoLayout.windValue.text = forecastDay.wind
        binding.additionalInfoLayout.pressureValue.text = forecastDay.pressure
        binding.additionalInfoLayout.visibilityValue.text = forecastDay.visibility
        binding.additionalInfoLayout.humidityValue.text = forecastDay.humidity

        val horizontalLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.forecastDayHour.forecastList.layoutManager = horizontalLayoutManager
        val adapter = ForecastHourAdapter(requireContext(), forecastDay.forecastHour)
        binding.forecastDayHour.forecastList.adapter = adapter

        return binding.root
    }
}