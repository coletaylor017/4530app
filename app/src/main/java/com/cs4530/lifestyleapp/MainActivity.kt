package com.cs4530.lifestyleapp

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.*
import android.location.Geocoder.GeocodeListener
import android.os.Build
import android.view.MenuItem
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.navigation.NavigationBarView
import java.io.File
import java.io.FileOutputStream
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener, NavigationBarView.OnItemSelectedListener {
    // Variables to hold values of UI elements
    private var firstNameValue: String? = null
    private var lastNameValue: String? = null
    private var ageValue: Int? = null
    private var cityValue: String? = null
    private var countryValue: String? = null
    private var heightFeetValue: Int? = null
    private var heightInchesValue: Int? = null
    private var weightValue: Int? = null
    private var sexValue: String? = null
    private var activityLevelValue: String? = null

    // Variables for UI elements
    private var firstNameTextEdit: EditText? = null
    private var lastNameTextEdit: EditText? = null
    private var ageTextEdit: EditText? = null
    private var cityTextEdit: EditText? = null
    private var countrySpinner: Spinner? = null
    private var heightFeetSpinner: Spinner? = null
    private var heightInchesSpinner: Spinner? = null
    private var weightTextEdit: EditText? = null
    private var sexSpinner: Spinner? = null
    private var activityLevelSpinner: Spinner? = null
    private var mButtonCamera: Button? = null
    private var mButtonSubmit: Button? = null
    private var mButtonLocation: Button? = null
    private var mIvPic: ImageView? = null

    private var countryAdapter: ArrayAdapter<String?>? = null
    private var feetAdapter: ArrayAdapter<String?>? = null
    private var inchesAdapter: ArrayAdapter<String?>? = null
    private var sexAdapter: ArrayAdapter<String?>? = null
    private var activityAdapter: ArrayAdapter<String?>? = null
    private var countryOptions : Array<String> = arrayOf("United States", "Canada", "Ethiopia", "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda", "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "The Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo, Republic of the", "Congo, Democratic Republic of the", "Costa Rica", "Cote d'Ivoire", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor (Timor-Leste)", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Fiji", "Finland", "France", "Gabon", "The Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, North", "Korea, South", "Kosovo", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mexico", "Micronesia, Federated States of", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar (Burma)", "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania", "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City (Holy See)", "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe")
    private var heightFeetOptions : Array<String> = arrayOf("1", "2", "3", "4", "5", "6", "7", "8")
    private var heightInchesOptions : Array<String> = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11")
    private var sexOptions : Array<String> = arrayOf("Prefer not to say", "Female", "Male")
    private var activityLevelOptions : Array<String> = arrayOf("Sedentary", "Lightly active", "Moderately active", "Active", "Very active")

    private var cityInput : EditText? = null

    private var latitude: Double? = null
    private var longitude: Double? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var requestPermissionLauncher : ActivityResultLauncher<String>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private var gcListener : GCListener = GCListener()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR

        // Get the form fields
        firstNameTextEdit = findViewById(R.id.firstNameInput)
        lastNameTextEdit = findViewById(R.id.lastNameInput)
        ageTextEdit = findViewById(R.id.ageInput)
        cityTextEdit = findViewById(R.id.cityInput)
        countrySpinner = findViewById(R.id.countryInput)
        heightFeetSpinner = findViewById(R.id.heightFeetInput)
        heightInchesSpinner = findViewById(R.id.heightFeetInput)
        weightTextEdit = findViewById(R.id.weightInput)
        sexSpinner = findViewById(R.id.sexInput)
        activityLevelSpinner = findViewById(R.id.activityLevelInput)

        //Get the buttons
        mButtonCamera = findViewById(R.id.PhotoButton)
        mButtonSubmit = findViewById(R.id.button_submit)
        mButtonLocation = findViewById(R.id.LocationButton)

        //Say that this class itself contains the listener
        mButtonCamera!!.setOnClickListener(this)
        mButtonSubmit!!.setOnClickListener(this)
        mButtonLocation!!.setOnClickListener(this)

        cityInput = findViewById(R.id.cityInput)

        // Define remaining values
        countryAdapter = setSpinnerData(R.id.countryInput, countryOptions)
        feetAdapter = setSpinnerData(R.id.heightFeetInput, heightFeetOptions)
        inchesAdapter = setSpinnerData(R.id.heightInchesInput, heightInchesOptions)
        sexAdapter = setSpinnerData(R.id.sexInput, sexOptions)
        activityAdapter = setSpinnerData(R.id.activityLevelInput, activityLevelOptions)

        /** The next few lines retrieve the photo from cache and redraw them **/
        var bits: Bitmap? = getBitmapFromCache()
        if (bits != null) {
            mIvPic = findViewById<View>(R.id.iv_pic) as ImageView
            mIvPic!!.setImageBitmap(bits)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted : Boolean ->
            if (isGranted) {
                Toast.makeText(this@MainActivity, "Location Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this@MainActivity, "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "No location permission. Hikes not accurate.", Toast.LENGTH_SHORT).show()
        }

        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                latitude = location.latitude
                longitude = location.longitude
                val latLongText = findViewById<TextView>(R.id.latLong)
                latLongText.text = "Latitude: ${location?.latitude} Longitude: ${location?.longitude}"
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



    }


//    override fun onRequestPermissionsResult(requestCode: Int,
//                                            permissions: Array<String>,
//                                            grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 1) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this@MainActivity, "Location Permission Granted", Toast.LENGTH_SHORT)
//                    .show()
//
//
//            } else {
//                Toast.makeText(this@MainActivity, "Location Permission Denied", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getLocationName(latitude: Double, longitude: Double): String? {
        var cityName = "Not Found"
        val gcd = Geocoder(this, Locale.getDefault())
        try {
            gcd.getFromLocation(
                latitude, longitude,
                10,
                gcListener
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return cityName
    }

    override fun onSaveInstanceState(outState: Bundle) {

        super.onSaveInstanceState(outState)

        firstNameValue = firstNameTextEdit!!.text.toString()
        lastNameValue = lastNameTextEdit!!.text.toString()
        val age = ageTextEdit!!.text.toString()
        cityValue = cityTextEdit!!.text.toString()
        countryValue = countrySpinner!!.selectedItem.toString()
        val heightFeet = heightFeetSpinner!!.selectedItem.toString()
        val heightInches = heightInchesSpinner!!.selectedItem.toString()
        val weight = weightTextEdit!!.text.toString()
        sexValue = sexSpinner!!.selectedItem.toString()
        activityLevelValue = activityLevelSpinner!!.selectedItem.toString()

        outState.putString("FIRST_NAME", firstNameValue)
        outState.putString("LAST_NAME", lastNameValue)
        outState.putString("AGE", age)
        outState.putString("COUNTRY", countryValue)
        outState.putString("CITY", cityValue)
        outState.putString("HEIGHT_FEET", heightFeet)
        outState.putString("HEIGHT_INCHES", heightInches)
        outState.putString("WEIGHT", weight)
        outState.putString("SEX", sexValue)
        outState.putString("ACTIVITY_LEVEL", activityLevelValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        super.onRestoreInstanceState(savedInstanceState)

        firstNameTextEdit!!.setText(savedInstanceState.getString("FIRST_NAME"))
        lastNameTextEdit!!.setText(savedInstanceState.getString("LAST_NAME"))
        ageTextEdit!!.setText(savedInstanceState.getString("AGE"))
        cityTextEdit!!.setText(savedInstanceState.getString("CITY"))
        val countryPosition = countryAdapter!!.getPosition(savedInstanceState.getString("COUNTRY"))
        countrySpinner!!.setSelection(countryPosition)
        val feetPosition = feetAdapter!!.getPosition(savedInstanceState.getString("HEIGHT_FEET"))
        heightFeetSpinner!!.setSelection(feetPosition)
        val inchesPosition = inchesAdapter!!.getPosition(savedInstanceState.getString("HEIGHT_INCHES"))
        heightInchesSpinner!!.setSelection(inchesPosition)
        weightTextEdit!!.setText(savedInstanceState.getString("WEIGHT"))
        val sexPosition = sexAdapter!!.getPosition(savedInstanceState.getString("SEX"))
        sexSpinner!!.setSelection(sexPosition)
        val activityPosition = activityAdapter!!.getPosition(savedInstanceState.getString("ACTIVITY_LEVEL"))
        activityLevelSpinner!!.setSelection(activityPosition)
    }

    private fun setSpinnerData(
        spinnerId: Int,
        spinnerOptions: Array<String>
    ): ArrayAdapter<String?> {
        val targetSpinner = findViewById<Spinner>(spinnerId)
        targetSpinner.onItemSelectedListener =
            this // onItemSelectedListener tells you which item was clicked
        val targetSpinnerAdapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this,
            android.R.layout.simple_spinner_item,
            spinnerOptions
        )
        targetSpinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        targetSpinner.adapter = targetSpinnerAdapter

        return targetSpinnerAdapter
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onClick(view: View) {
        when (view.id) { //Added ? due to warning message. Consider better checks.
            R.id.LocationButton -> {



                if (
                    ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // You can use the API that requires the permission.
                    Toast.makeText(this@MainActivity, "Location permission already granted", Toast.LENGTH_SHORT).show();


                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            // Got last known location. In some rare situations this can be null.
                            val latLongText = findViewById<TextView>(R.id.latLong)
                            latLongText.text = "Latitude: ${location?.latitude} Longitude: ${location?.longitude}"
                            if (location != null) {
                                getLocationName(location.latitude, location.longitude)
                            }
                        }
                }
                else {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    Toast.makeText(this@MainActivity, "Asking permission", Toast.LENGTH_SHORT).show();
                    requestPermissionLauncher.launch(
                        Manifest.permission.ACCESS_COARSE_LOCATION

                    )
                }
            }

            R.id.PhotoButton -> {
                //The button press should open a camera
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    cameraActivity.launch(cameraIntent)
                } catch (ex: ActivityNotFoundException) {
                    //Do error handling here
                }
            }
            R.id.button_submit -> {
                /** Next three IF statements prevent 'submit' button crash due to not completing age, height, weight fields
                 * If sliders implemented these can probably be removed. Imported text.utils to allow TextUtils.isEmpty check **/

                if(TextUtils.isEmpty(ageTextEdit!!.text)) {
                    Toast.makeText(this@MainActivity, "Age cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(heightFeetSpinner!!.selectedItem.toString()) ||
                    TextUtils.isEmpty(heightInchesSpinner!!.selectedItem.toString())) {
                    Toast.makeText(this@MainActivity, "Height cannot be empty", Toast.LENGTH_SHORT).show();
                    return
                }
                if(TextUtils.isEmpty(weightTextEdit!!.text)) {
                    Toast.makeText(this@MainActivity, "Weight cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                firstNameValue = firstNameTextEdit!!.text.toString()
                lastNameValue = lastNameTextEdit!!.text.toString()
                ageValue = Integer.parseInt(ageTextEdit!!.text.toString())
                cityValue = cityTextEdit!!.text.toString()
                countryValue = countrySpinner!!.selectedItem.toString()
                heightFeetValue = Integer.parseInt(heightFeetSpinner!!.selectedItem.toString())
                heightInchesValue = Integer.parseInt(heightInchesSpinner!!.selectedItem.toString())
                weightValue = Integer.parseInt(weightTextEdit!!.text.toString())
                sexValue = sexSpinner!!.selectedItem.toString()
                activityLevelValue = activityLevelSpinner!!.selectedItem.toString()

                //Start an activity and pass the data to it.
                val messageIntent = Intent(this, ProfileDisplayActivity::class.java)
                messageIntent.putExtra("NAME", "$firstNameValue $lastNameValue")
                messageIntent.putExtra("AGE", ageValue)
                messageIntent.putExtra("LOCATION", "$cityValue, $countryValue")
                messageIntent.putExtra("HEIGHT", heightFeetValue!! * 12 + heightInchesValue!!)
                messageIntent.putExtra("WEIGHT", weightValue)
                messageIntent.putExtra("SEX", sexValue)
                messageIntent.putExtra("ACTIVITY_LEVEL", activityLevelValue)
                this.startActivity(messageIntent)
            }
            //this doesn't do anything, not sure how to make it go to the weather page from the button
            //on the navbar...
            /*R.id.action_weather -> {
                cityValue = cityTextEdit!!.text.toString()
                countryValue = countrySpinner!!.selectedItem.toString()

                //Start an activity and pass the data to it.
                val messageIntent = Intent(this, WeatherDisplayActivity::class.java)
                messageIntent.putExtra("LOCATION", "$cityValue, $countryValue")
                this.startActivity(messageIntent)
            }*/
        }
    }

    /**
     * This function/method is near identical to the class example.
     * This enables the intent for the camera and image retrieval.
     */
    private val cameraActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                mIvPic = findViewById<View>(R.id.iv_pic) as ImageView
                //val extras = result.data!!.extras
                //val thumbnailImage = extras!!["data"] as Bitmap?

                if (Build.VERSION.SDK_INT >= 33) {
                    val thumbnailImage =
                        result.data!!.getParcelableExtra("data", Bitmap::class.java)
                    mIvPic!!.setImageBitmap(thumbnailImage)
                    if (thumbnailImage != null) {
                        /** This saves the thumbnail to cache **/
                        saveBitmapToCache(thumbnailImage)
                    }
                } else {
                    val thumbnailImage = result.data!!.getParcelableExtra<Bitmap>("data")
                    mIvPic!!.setImageBitmap(thumbnailImage)

                    if (thumbnailImage != null) {
                        /** This saves the thumbnail to cache **/
                        saveBitmapToCache(thumbnailImage)
                    }
                }
            }
        }

    /**
     * This method/function is hardcoded to save the photo to the cache.
     * This is used in [cameraActivity]
     */
    private fun saveBitmapToCache(bitmap: Bitmap) {
        val fileName = "photo.png"
        val file = File(cacheDir, fileName)
        val outputStream = FileOutputStream(file)

        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            outputStream.close()
        }
    }

    /**
     * This method/function is hardcoded to retrieve the profile photo from the cache.
     * This is used in [onCreate]
     */
    private fun getBitmapFromCache(): Bitmap? {
        val fileName = "photo.png"
        val file = File(cacheDir, fileName)

        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }

    // Note: Cosmo Kramer is 6'3" and born in 1949

    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?, position: Int,
        id: Long
    ) {
        // access selected country using countries[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
    // access selected country using countries[position]

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    inner class GCListener : GeocodeListener {

        override fun onGeocode(addresses: List<Address>) {
            // do something with the location
            var cityName: String = "Not found"
            if (addresses != null) {
                for (adrs in addresses) {
                    if (adrs != null) {
                        val city: String = adrs.locality
                        if (city != null && city != "") {
                            cityName = city
                            println("city ::  $cityName")
                        } else {
                        }
                        // you should also try with addresses.get(0).toSring();
                    }
                }
            }
            cityInput?.setText(cityName)
        }

        override fun onError(errorMessage: String?) {
            super.onError(errorMessage)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_profile -> {
                // Respond to navigation item 1 click
                true
            }
            R.id.action_weather -> {
                // Respond to navigation item 2 click
                true
            }
            else -> false
        }
        return true
    }

    //override fun NavigationItemSelectedListener()
}


