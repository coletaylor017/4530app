package com.cs4530.lifestyleapp

import android.content.ActivityNotFoundException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.provider.MediaStore
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.navigation.NavigationBarView
import java.io.File
import java.io.FileOutputStream

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
    private var mIvPic: ImageView? = null

    private var countryOptions: Array<String> = arrayOf("United States", "Canada", "Ethiopia")
    private var heightFeetOptions: Array<String> = arrayOf("1", "2", "3", "4", "5", "6", "7", "8")
    private var heightInchesOptions: Array<String> = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11")
    private var sexOptions: Array<String> = arrayOf("Prefer not to say", "Female", "Male")
    private var activityLevelOptions: Array<String> =
        arrayOf("Sedentary", "Lightly active", "Moderately active", "Active", "Very active")

    private var countryAdapter: ArrayAdapter<String?>? = null
    private var feetAdapter: ArrayAdapter<String?>? = null
    private var inchesAdapter: ArrayAdapter<String?>? = null
    private var sexAdapter: ArrayAdapter<String?>? = null
    private var activityAdapter: ArrayAdapter<String?>? = null

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

        //Say that this class itself contains the listener
        mButtonCamera!!.setOnClickListener(this)
        mButtonSubmit!!.setOnClickListener(this)

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

    override fun onClick(view: View) {
        when (view.id) { //Added ? due to warning message. Consider better checks.
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
            R.id.action_weather -> {
                cityValue = cityTextEdit!!.text.toString()
                countryValue = countrySpinner!!.selectedItem.toString()

                //Start an activity and pass the data to it.
                val messageIntent = Intent(this, WeatherDisplayActivity::class.java)
                messageIntent.putExtra("LOCATION", "$cityValue, $countryValue")
                this.startActivity(messageIntent)
            }
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

    override fun onNothingSelected(parent: AdapterView<*>?) {}

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


