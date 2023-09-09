package hr.tvz.lstavaljladisic.weatherapp.view

import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.tvz.lstavaljladisic.weatherapp.R
import hr.tvz.lstavaljladisic.weatherapp.common.WeatherWidgetProvider

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_fragment)

        if(savedInstanceState==null){
            registerReceiver(
                WeatherWidgetProvider(),
                IntentFilter(WeatherWidgetProvider().UPDATE_WIDGET)
            )

            supportFragmentManager
                .beginTransaction()
                .add(R.id.show_fragment, WeatherInfoFragment())
                .commit()
        }
    }
}
