package hr.tvz.lstavaljladisic.weatherapp.common

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget
import hr.tvz.lstavaljladisic.weatherapp.R
import hr.tvz.lstavaljladisic.weatherapp.view.WeatherInfoFragment.Companion.weatherData

class WeatherWidgetProvider: AppWidgetProvider() {
    val UPDATE_WIDGET = "ClockWidgetProvider"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (UPDATE_WIDGET == intent!!.action) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisAppWidget = ComponentName(
                context!!.opPackageName,
                WeatherWidgetProvider::class.java.name
            )
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)
            refresh(context, appWidgetManager, appWidgetIds)
        }

        super.onReceive(context, intent)
    }

    private fun refresh(context: Context, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {

        for (appWidgetId in appWidgetIds!!) {
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)

            remoteViews.setTextViewText(R.id._widget_heading, weatherData.dateTime)
            remoteViews.setTextViewText(R.id._widget_condition, weatherData.weatherConditionIconDescription)
            remoteViews.setTextViewText(R.id._widget_city, weatherData.cityAndCountry)
            remoteViews.setTextViewText(R.id._widget_temp, weatherData.temperature)

            val appWidgetTarget = AppWidgetTarget(context, R.id._widget_image, remoteViews, appWidgetId)
            Glide.with(context.applicationContext)
                .asBitmap()
                .load(weatherData.weatherConditionIconUrl)
                .into(appWidgetTarget)

            appWidgetManager!!.updateAppWidget(appWidgetId, remoteViews)
        }
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        refresh(context!!, appWidgetManager, appWidgetIds)

        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}