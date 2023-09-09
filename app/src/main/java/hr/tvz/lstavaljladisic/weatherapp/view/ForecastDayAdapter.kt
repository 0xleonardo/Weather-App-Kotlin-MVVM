import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hr.tvz.lstavaljladisic.weatherapp.R
import hr.tvz.lstavaljladisic.weatherapp.model.forecast.ForecastDay
import hr.tvz.lstavaljladisic.weatherapp.utils.toDayNameString

class ForecastDayAdapter internal constructor(context: Context?, weatherData: List<ForecastDay>) : RecyclerView.Adapter<ForecastDayAdapter.ViewHolder>() {
    private val mForecastData: List<ForecastDay>
    private val mInflater: LayoutInflater
    private var mClickListener: ItemClickListener? = null

    init {
        mInflater = LayoutInflater.from(context)
        mForecastData = weatherData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.forecast_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = mForecastData[position]
        Glide.with(holder.myImage.context).load(forecast.weatherConditionIconUrl).into(holder.myImage)
        holder.myTempDay.text = forecast.temperature
        holder.myTime.text = forecast.dateTime.toDayNameString()
    }

    override fun getItemCount(): Int {
        return mForecastData.size
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var myImage: ImageView
        var myTempDay: TextView
        var myTime: TextView

        init {
            myImage = itemView.findViewById(R.id.card_image)
            myTempDay = itemView.findViewById(R.id.card_temp_day)
            myTime = itemView.findViewById(R.id.card_timedate)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }
    }

    fun getItem(id: Int): ForecastDay {
        return mForecastData[id]
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
}