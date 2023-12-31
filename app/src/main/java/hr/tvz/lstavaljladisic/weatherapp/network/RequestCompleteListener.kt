package hr.tvz.lstavaljladisic.weatherapp.network

interface RequestCompleteListener<T> {
    fun onRequestSuccess(data: T)
    fun onRequestFailed(errorMessage: String)
}