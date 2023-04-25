package com.cs4530.lifestyleapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import androidx.lifecycle.Observer


import java.io.IOException
import java.util.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener, ProfileEditFragment.ProfileEditDataPassingInterface {
    // UI element vars
    private var bottomNavBar: BottomNavigationView? = null
    private var bmrButton: ExtendedFloatingActionButton? = null

    // Data vars
    private var currentCity: String? = null
    private var bmrValue: String? = null
    private var currentUser: UserTable? = null

    // Location variables
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher : ActivityResultLauncher<String>
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private var gcListener : MainActivity.GCListener = GCListener()

    // Initialize the view model here
    private val mViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as MainApplication).repository)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR

        //Get UI elements
        bottomNavBar = findViewById(R.id.bottom_navigation_bar)
        bmrButton = findViewById(R.id.bmr_label)
        bottomNavBar!!.setOnItemSelectedListener(this)

        currentCity = "Salt&Lake&City"

        mViewModel.dataUser.observe(this, liveDataObserver)


        //Instantiate the fragment
        val profileEditFragment = ProfileEditFragment(mViewModel)

        //Replace the fragment container
        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fragment_placeholder, profileEditFragment, "Profile_Edit_Frag")
        fTrans.commit()


        // Permissions stuff for location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted : Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show()

                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
                    .addOnSuccessListener { location : Location? ->
                        if (location != null) {
                            setLocation(location.latitude, location.longitude)
                        }
                        else
                        {
                            Toast.makeText(this, "There was a problem finding your location", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

        if (
            ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // location permission has already been granted
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
                .addOnSuccessListener { location : Location? ->
                    if (location != null) {
                        setLocation(location.latitude, location.longitude)
                    }
                }
        }
        else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            Toast.makeText(this, "Asking permission", Toast.LENGTH_SHORT).show();
            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
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
                val profileEditFragment = ProfileEditFragment(mViewModel)

                //Replace the fragment container
                val fTrans = supportFragmentManager.beginTransaction()
                fTrans.replace(R.id.fragment_placeholder, profileEditFragment, "Profile_Edit_Frag")
                fTrans.commit()
                true
            }
            R.id.action_weather -> {
                val sanitizedLocation = currentCity!!.replace(' ', '&')
                mViewModel.setWeather("$sanitizedLocation,us")
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

    private val liveDataObserver: Observer<UserTable> =
        Observer { userData ->
            if (userData != null) {
                bmrButton!!.text = userData!!.bmr.toString()
            }
        }

    // This is saying, when passProfileData is called in the ProfileEditFragment, do this callback
    override fun passProfileData() {
        val profileDisplayFragment = ProfileDisplayFragment()

        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(
            R.id.fragment_placeholder,
            profileDisplayFragment,
            "Profile_Display_Frag"
        )
        fTrans.commit()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun setLocation(locationLat: Double, locationLon: Double) {
        val gcd = Geocoder(this, Locale.getDefault())
        try {
            gcd.getFromLocation(
                locationLat, locationLon,
                10,
                gcListener
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    inner class GCListener : Geocoder.GeocodeListener {
        override fun onGeocode(addresses: List<Address>) {
            // do something with the location
            // get a few cities, just pick the first one that has a non-null city name
            if (addresses != null) {
                for (adrs in addresses) {
                    if (adrs != null) {
                        currentCity = adrs.locality
                        if (adrs.locality != null && adrs.locality != ""
                            && adrs.countryName != null && adrs.countryName != ""
                        ) {
                            currentCity = adrs.locality
                            return
                        }
                    }
                }
            }
        }
    }

}