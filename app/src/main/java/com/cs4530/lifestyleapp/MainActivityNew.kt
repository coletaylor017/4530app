package com.cs4530.lifestyleapp

import android.graphics.Movie
import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import com.google.gson.GsonBuilder
import org.json.JSONException
import java.util.concurrent.Executors


class MainActivityNew : AppCompatActivity(), NavigationBarView.OnItemSelectedListener, ProfileEditFragment.ProfileEditDataPassingInterface {
    // UI element vars
    private var bottomNavBar: BottomNavigationView? = null
    private var bmrButton: ExtendedFloatingActionButton? = null

    // Data vars
    private var currentLocation: String? = null
    private var mWeatherData: WeatherData? = null
    private var temperature: Double? = null
    private var humidity: Double? = null
    private var tempHigh: Double? = null
    private var tempLow: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_new)
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR

        //Get UI elements
        bottomNavBar = findViewById(R.id.bottom_navigation_bar)
        bmrButton = findViewById(R.id.bmr_label)
        bottomNavBar!!.setOnItemSelectedListener(this)

        // TODO: this is a hard-coded value, need to get their current location
        currentLocation = "Salt&Lake&City,us"
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_profile -> {
                //Instantiate the fragment
                val profileEditFragment = ProfileEditFragment()

                //Replace the fragment container
                val fTrans = supportFragmentManager.beginTransaction()
                fTrans.replace(R.id.fragment_placeholder, profileEditFragment, "Profile_Edit_Frag")
                fTrans.commit()
                true
            }
            R.id.action_weather -> {
                val sanitizedLocation = currentLocation!!.replace(' ', '&')
                loadWeatherData(sanitizedLocation)

                true
            }
            else -> false
        }
        return true
    }

    // This is saying, when passProfileData is called in the ProfileEditFragment, do this callback
    override fun passProfileData(data: Array<String?>?) {
        if (data != null) {
            // parse the data into a bundle and then pass it to fragment
            val bundle = Bundle()
            val firstName = data[0]
            val lastName = data[1]
            val city = data[3]
            val country = data[4]
            val bmr = data[10]

            bundle.putString("NAME", "$firstName $lastName")
            bundle.putString("AGE", data[2])
            bundle.putString("LOCATION", "$city, $country")
            bundle.putString("HEIGHT_FEET", data[5])
            bundle.putString("HEIGHT_INCHES", data[6])
            bundle.putString("WEIGHT", data[7])
            bundle.putString("SEX", data[8])
            bundle.putString("ACTIVITY_LEVEL", data[9])
            bundle.putString("BMR_SCORE", bmr)

            // Set the BMR score
            bmrButton!!.text = bmr

            // Load the profile display fragment
            val profileDisplayFragment = ProfileDisplayFragment()
            profileDisplayFragment.arguments = bundle

            val fTrans = supportFragmentManager.beginTransaction()
            fTrans.replace(
                R.id.fragment_placeholder,
                profileDisplayFragment,
                "Profile_Display_Frag"
            )
            fTrans.commit()
        }
    }

    private fun loadWeatherData(location: String) {
        try {
            FetchWeatherTask().execute(location)
        } catch(e: Exception) {
            Toast.makeText(this, "Could not load weather data", Toast.LENGTH_SHORT).show()
        }
    }

    private inner class FetchWeatherTask {
        var executorService = Executors.newSingleThreadExecutor()!!
        var mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper())
        fun execute(location: String?) {
            executorService.execute {
                var jsonWeatherData: String?
                val weatherDataURL = WeatherNetworkUtils.buildURLFromString(location!!)
                try {
                    jsonWeatherData = WeatherNetworkUtils.getDataFromURL(weatherDataURL!!)
                    postToMainThread(jsonWeatherData)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        private fun postToMainThread(jsonWeatherData: String?) {
            mainThreadHandler.post {
                if (jsonWeatherData != null) {
                    try {
                        val gson = GsonBuilder()
                            .registerTypeAdapter(WeatherData::class.java, JSONWeatherUtils)
                            .create()
                        mWeatherData = gson.fromJson(jsonWeatherData, WeatherData::class.java)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (mWeatherData != null) {
                        val weatherFragment = WeatherFragment()
                        // TODO: need to format temperatures from Kelvin
//                        temperature =
//                            "" + (mWeatherData!!.temperature - 273.15).roundToInt() + " C"
                        temperature = mWeatherData!!.temperature
                        humidity = mWeatherData!!.humidity
                        tempLow = mWeatherData!!.tempLow
                        tempHigh = mWeatherData!!.tempHigh

                        val bundle = Bundle()
                        bundle.putString("TEMP", temperature.toString())
                        bundle.putString("TEMP_HIGH", tempHigh.toString())
                        bundle.putString("TEMP_LOW", tempLow.toString())
                        bundle.putString("HUMIDITY", humidity.toString())

                        weatherFragment.arguments = bundle
                        val fTrans = supportFragmentManager.beginTransaction()
                        fTrans.replace(R.id.fragment_placeholder, weatherFragment, "Weather_Frag")
                        fTrans.commit()
                    }
                }
            }
        }
    }

}