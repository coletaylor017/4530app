package com.cs4530.lifestyleapp

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.lang.ClassCastException

class ProfileDisplayFragment: Fragment() {
    private var nameReceived: String? = null
    private var ageReceived : String? = null
    private var locationReceived : String? = null
    private var feetReceived: String? = null
    private var inchesReceived: String? = null
    private var weightReceived: String? = null
    private var sexReceived : String? = null
    private var activityLevelReceived : String? = null
    private var bmrReceived : String? = null
    private var bmrScore: TextView? = null
    private var mIvPic: ImageView? = null
    private var locationString: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_display, container, false)

        // Initialize the location manager. This will get an update on the users location while onCreate is running.
        val locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Initialize the location listener. This is designed to get an updated location once and stop.
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                locationString = location.latitude.toString() + ", " + location.longitude.toString()
                //This ends the requestLocationUpdates. Though initial tests didn't show much degredation in peformance. Best practice to removeUpdates though.
                locationManager.removeUpdates(this)
            }
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                // Handle status changes
                var yes : String? = null
            }
            override fun onProviderEnabled(provider: String) {
                // Handle provider enabled
                var yes : String? = null
            }
            override fun onProviderDisabled(provider: String) {
                // Handle provider disabled
                var yes : String? = null
            }
        }



        //Get the string data
        if (arguments != null) {
            nameReceived = requireArguments().getString("NAME")
            ageReceived = requireArguments().getString("AGE")
            locationReceived = requireArguments().getString("LOCATION")
            feetReceived = requireArguments().getString("HEIGHT_FEET")
            inchesReceived = requireArguments().getString("HEIGHT_INCHES")
            weightReceived = requireArguments().getString("WEIGHT")
            sexReceived = requireArguments().getString("SEX")
            activityLevelReceived = requireArguments().getString("ACTIVITY_LEVEL")
            bmrReceived = requireArguments().getString("BMR_SCORE")
        }

        // Display received data in text views
        view.findViewById<TextView>(R.id.name_value)!!.text = nameReceived!!.toString()
        view.findViewById<TextView>(R.id.age_value)!!.text = ageReceived!!.toString()
        view.findViewById<TextView>(R.id.location_value)!!.text = locationReceived!!.toString()
        view.findViewById<TextView>(R.id.height_value)!!.text = "$feetReceived\'$inchesReceived\""
        view.findViewById<TextView>(R.id.weight_value)!!.text = weightReceived!!.toString()
        view.findViewById<TextView>(R.id.sex_value)!!.text = sexReceived!!.toString()
        view.findViewById<TextView>(R.id.activity_level_value)!!.text = activityLevelReceived!!.toString()

        //Get the text view where we will display BMR score
        bmrScore = view.findViewById(R.id.bmr_score)

        if (bmrReceived == "0" || bmrReceived.isNullOrBlank()) {
            bmrScore!!.text = "-"
        } else {
            bmrScore!!.text = bmrReceived
        }

        /** The next block retrieve the photo from cache and redraw them **/
        var bits : Bitmap? = getBitmapFromCache()
        if(bits != null) {
            mIvPic = view.findViewById<View>(R.id.profile_pic) as ImageView
            mIvPic!!.setImageBitmap(bits)
        }
        else { //Default view is a person silhouette
            mIvPic = view.findViewById<View>(R.id.profile_pic) as ImageView
            mIvPic!!.setImageResource(R.drawable.baseline_person_24)
        }

        /** This block prevents hike button from being active if location is disabled. Otherwise implements **/
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(requireContext(), "No location permission. Hikes not accurate.", Toast.LENGTH_SHORT).show()
            val hikeButtonDisabled = view.findViewById<Button>(R.id.find_hike)
            hikeButtonDisabled.setOnClickListener {
                Toast.makeText(requireContext(), "Enable location permission to find hikes", Toast.LENGTH_SHORT).show()
                hikeButtonDisabled.isEnabled = false
            }
        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener) //Suppressed MissingPermission for this

            /** The next block listens for find hike button and opens the map**/
            val hikeButton = view.findViewById<Button>(R.id.find_hike)
            hikeButton.setOnClickListener {
                val searchUri = Uri.parse("geo:" + locationString + "?q=hikes near me") //Instead of getting location in search, utilized maps' automatic search from location
                //create map intent
                val mapIntent = Intent(Intent.ACTION_VIEW, searchUri)
                try{
                    startActivity(mapIntent)
                }catch(ex: ActivityNotFoundException) {
                    Toast.makeText(requireContext(), "Map Not Available", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    /**
     * This method/function is hardcoded to retrieve the profile photo from the cache.
     * This is used in [onCreate]
     */
    private fun getBitmapFromCache(): Bitmap? {
        val fileName = "photo.png"
        val file = File(requireActivity().cacheDir, fileName)

        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }

}