package com.cs4530.lifestyleapp

import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener, ProfileEditFragment.ProfileEditDataPassingInterface {
    // UI element vars
    private var bottomNavBar: BottomNavigationView? = null
    private var bmrButton: ExtendedFloatingActionButton? = null

    // Data vars
    private var currentLocation: String? = null
    private var bmrValue: String? = null

    // Initialize the view model here. One per activity.
    // While initializing, we'll also inject the repository.
    // However, standard view model constructor only takes a context to
    // the activity. We'll need to define our own constructor, but this
    // requires writing our own view model factory.
    private val mViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as MainApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR

        //Get UI elements
        bottomNavBar = findViewById(R.id.bottom_navigation_bar)
        //Consider removing this to update BMR from profile button
        bmrButton = findViewById(R.id.bmr_label)
        bottomNavBar!!.setOnItemSelectedListener(this)

        // TODO: this is a hard-coded value, need to get their current location
        currentLocation = "Salt&Lake&City,us"

        //Instantiate the fragment
        val profileEditFragment = ProfileEditFragment()

        //Replace the fragment container
        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fragment_placeholder, profileEditFragment, "Profile_Edit_Frag")
        fTrans.commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("BMR_SCORE", bmrValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        bmrButton!!.text = bmrValue
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
                mViewModel.setWeather(sanitizedLocation)
                val weatherFragment = WeatherFragment()
                val fTrans = supportFragmentManager.beginTransaction()
                fTrans.replace(R.id.fragment_placeholder, weatherFragment, "Weather_Frag")
                fTrans.commit()
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
            bmrValue = data[10]

            bundle.putString("NAME", "$firstName $lastName")
            bundle.putString("AGE", data[2])
            bundle.putString("LOCATION", "$city, $country")
            bundle.putString("HEIGHT_FEET", data[5])
            bundle.putString("HEIGHT_INCHES", data[6])
            bundle.putString("WEIGHT", data[7])
            bundle.putString("SEX", data[8])
            bundle.putString("ACTIVITY_LEVEL", data[9])
            bundle.putString("BMR_SCORE", bmrValue)

            // Set the BMR score
            bmrButton!!.text = bmrValue

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

}