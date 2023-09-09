import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hr.tvz.lstavaljladisic.weatherapp.R
import hr.tvz.lstavaljladisic.weatherapp.model.forecast.ForecastHour

class ForecastHourAdapter internal constructor(context: Context?, weatherData: List<ForecastHour>) : RecyclerView.Adapter<ForecastHourAdapter.ViewHolder>() {
    private val mForecastData: List<ForecastHour>
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
        holder.myTemp.text = forecast.temperature
        holder.myTime.text = forecast.dateTime
    }

    override fun getItemCount(): Int {
        return mForecastData.size
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var myImage: ImageView
        var myTemp: TextView
        var myTime: TextView

        init {
            myImage = itemView.findViewById(R.id.card_image)
            myTemp = itemView.findViewById(R.id.card_temp_day)
            myTime = itemView.findViewById(R.id.card_timedate)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }
    }

    fun getItem(id: Int): ForecastHour {
        return mForecastData[id]
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
}