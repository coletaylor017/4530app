package com.cs4530.lifestyleapp

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import kotlin.math.roundToInt

class MainRepository private constructor(weatherDao: WeatherDao) {
    // Weather fields
    val weatherData = MutableLiveData<WeatherTable>()
    private var mWeatherDao: WeatherDao = weatherDao
    private var mJsonWeatherData: String? = null
    private var temperature: Int? = null
    private var humidity: Double? = null
    private var tempHigh: Int? = null
    private var tempLow: Int? = null


    private var mLocation: String? = null

    fun setWeather(location: String) {
        // First cache the location
        mLocation = location

        // Everything within the scope happens logically sequentially
        mScope.launch(Dispatchers.IO){
            //fetch data on a worker thread
            fetchWeatherData(location)

            // After the suspend function returns, Update the View THEN insert into db
            if(mJsonWeatherData!=null) {
                try {
                    val gson = GsonBuilder()
                        .registerTypeAdapter(WeatherTable::class.java, JSONWeatherUtils)
                        .create()

                    val data = gson.fromJson(mJsonWeatherData, WeatherTable::class.java)
                    temperature =
                        (((data!!.temperature?.minus(273.15))?.
                        times(9)?.
                        div(5))?.
                        plus(32))?.
                        roundToInt()
                    humidity = data!!.humidity
                    tempLow = data!!.tempLow
                    tempHigh = data!!.tempHigh

                    weatherData.postValue(data)

                    insert()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

    @WorkerThread
    suspend fun insert() {
        if (temperature != null && tempHigh !=null && tempLow !=null && humidity !=null) {
            mWeatherDao.insert(WeatherTable(temperature = temperature!!, tempHigh = tempHigh!!, tempLow = tempLow!!, humidity = humidity!!))
        }
    }

    @WorkerThread
    suspend fun fetchWeatherData(location: String) {
        val weatherDataURL = WeatherNetworkUtils.buildURLFromString(location)
        if(weatherDataURL!=null) {
            // This is actually a blocking call unless you're using an
            // asynchronous IO library (which we're not). However, it is a blocking
            // call on a background thread, not on the UI thread
            val jsonWeatherData = WeatherNetworkUtils.getDataFromURL(weatherDataURL)
            if (jsonWeatherData != null) {
                mJsonWeatherData = jsonWeatherData
            }
        }
    }

    // Make the repository singleton.
    companion object {
        private var mInstance: MainRepository? = null
        private lateinit var mScope: CoroutineScope
        @Synchronized
        fun getInstance(weatherDao: WeatherDao,
                        scope: CoroutineScope
        ): MainRepository {
            mScope = scope
            return mInstance?: synchronized(this){
                val instance = MainRepository(weatherDao)
                mInstance = instance
                instance
            }
        }
    }
}