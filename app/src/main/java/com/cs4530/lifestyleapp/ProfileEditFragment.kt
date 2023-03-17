package com.cs4530.lifestyleapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import java.io.File
import java.io.FileOutputStream
import java.lang.ClassCastException

class ProfileEditFragment: Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    // Variables to hold values of UI elements
    private var firstNameValue: String? = null
    private var lastNameValue: String? = null
    private var ageValue: String? = null
    private var cityValue: String? = null
    private var countryValue: String? = null
    private var heightFeetValue: String? = null
    private var heightInchesValue: String? = null
    private var weightValue: String? = null
    private var sexValue: String? = null
    private var activityLevelValue: String? = null
    private var bmrValue: String? = null

    // Variables for UI elements
    private var firstNameTextEdit: EditText? = null
    private var lastNameTextEdit: EditText? = null
    private var ageSlider: Slider? = null
    private var cityTextEdit: EditText? = null
    private var countrySpinner: Spinner? = null
    private var heightFeetSpinner: Spinner? = null
    private var heightInchesSpinner: Spinner? = null
    private var weightSlider: Slider? = null
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

    var dataPasser: ProfileEditDataPassingInterface? = null

    interface ProfileEditDataPassingInterface {
        fun passProfileData(data: Array<String?>?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = try {
            context as ProfileEditDataPassingInterface
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ProfileDisplayFragment.DataPassingInterface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)
        // Get the form fields
        firstNameTextEdit = view.findViewById(R.id.firstNameInput)
        lastNameTextEdit = view.findViewById(R.id.lastNameInput)
        ageSlider = view.findViewById(R.id.ageInput)
        cityTextEdit = view.findViewById(R.id.cityInput)
        countrySpinner = view.findViewById(R.id.countryInput)
        heightFeetSpinner = view.findViewById(R.id.heightFeetInput)
        heightInchesSpinner = view.findViewById(R.id.heightFeetInput)
        weightSlider = view.findViewById(R.id.weightInput)
        sexSpinner = view.findViewById(R.id.sexInput)
        activityLevelSpinner = view.findViewById(R.id.activityLevelInput)

        //Get the buttons
        mButtonCamera = view.findViewById(R.id.button_upload_photo)
        mButtonSubmit = view.findViewById(R.id.button_submit)

        //Say that this class itself contains the listener
        mButtonCamera!!.setOnClickListener(this)
        mButtonSubmit!!.setOnClickListener(this)

        // Define remaining values
        countryAdapter = setSpinnerData(R.id.countryInput, countryOptions, view)
        feetAdapter = setSpinnerData(R.id.heightFeetInput, heightFeetOptions, view)
        inchesAdapter = setSpinnerData(R.id.heightInchesInput, heightInchesOptions, view)
        sexAdapter = setSpinnerData(R.id.sexInput, sexOptions, view)
        activityAdapter = setSpinnerData(R.id.activityLevelInput, activityLevelOptions, view)

        mIvPic = view.findViewById(R.id.iv_pic)

        /** The next few lines retrieve the photo from cache and redraw them **/
        var bits: Bitmap? = getBitmapFromCache()
        if (bits != null) {
            mIvPic = view.findViewById<View>(R.id.iv_pic) as ImageView
            mIvPic!!.setImageBitmap(bits)
        }

        return view
    }

    private fun setSpinnerData(
        spinnerId: Int,
        spinnerOptions: Array<String>,
        view: View
    ): ArrayAdapter<String?> {
        val targetSpinner = view.findViewById<Spinner>(spinnerId)
        targetSpinner.onItemSelectedListener =
            this // onItemSelectedListener tells you which item was clicked
        // TODO: need to do something with activity because it's in a frag here
        val targetSpinnerAdapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerOptions
        )
        targetSpinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        targetSpinner.adapter = targetSpinnerAdapter

        return targetSpinnerAdapter
    }


    /**
     * This function/method is near identical to the class example.
     * This enables the intent for the camera and image retrieval.
     */
    private val cameraActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
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
        val file = File(requireActivity().cacheDir, fileName)
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
        val file = File(requireActivity().cacheDir, fileName)

        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }

    /* BMR calculation:
    * For men: 66.47 + (6.24 × weight in pounds) + (12.7 × height in inches) − (6.75 × age in years).
    * For women: BMR = 65.51 + (4.35 × weight in pounds) + (4.7 × height in inches) - (4.7 × age in years) */
    private fun calculateBMR(feet: String?, inches:String?, weight: String?, sex:String?, age:String?, activity:String?): Int {
        if (feet.isNullOrBlank() || inches.isNullOrBlank() || weight.isNullOrBlank() || sex.isNullOrBlank()
            || age.isNullOrBlank() || activity.isNullOrBlank()) {
            return 0

        }
        // TODO: have to factor in activity level
        val weightInt = weight.toInt()
        val ageInt = age.toInt()
        val heightInInches = feet.toInt() * 12 + inches.toInt()

        if (sex == "Female") {
            return (65.51 + (4.35 * weightInt) + (4.7 * heightInInches) - (4.7 * ageInt)).toInt()
        }
        if (sex == "Male") {
            return (66.47 + (6.24 * weightInt) + (12.7 * heightInInches) - (6.75 * ageInt)).toInt()
        }
        return 0
    }

    override fun onClick(view: View) {
        when (view?.id) { //Added ? due to warning message. Consider better checks.
            R.id.button_upload_photo -> {
                //The button press should open a camera
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    cameraActivity.launch(cameraIntent)
                } catch (ex: ActivityNotFoundException) {
                    val text = "Error: $ex"
                    val duration = Toast.LENGTH_SHORT

                    val toast = Toast.makeText(requireContext(), text, duration)
                    toast.show()
                }
            }
            R.id.button_submit -> {
                firstNameValue = firstNameTextEdit!!.text.toString()
                lastNameValue = lastNameTextEdit!!.text.toString()
                ageValue = ageSlider!!.value.toInt().toString()
                cityValue = cityTextEdit!!.text.toString()
                countryValue = countrySpinner!!.selectedItem.toString()
                heightFeetValue = heightFeetSpinner!!.selectedItem.toString()
                heightInchesValue = heightInchesSpinner!!.selectedItem.toString()
                weightValue = weightSlider!!.value.toInt().toString()
                sexValue = sexSpinner!!.selectedItem.toString()
                activityLevelValue = activityLevelSpinner!!.selectedItem.toString()

                bmrValue = calculateBMR(heightFeetValue, heightInchesValue, weightValue, sexValue, ageValue, activityLevelValue).toString()

                val formValuesArray : Array<String?> = arrayOf(firstNameValue, lastNameValue, ageValue, cityValue,
                    countryValue, heightFeetValue, heightInchesValue, weightValue, sexValue, activityLevelValue, bmrValue)
                // Pass data back up to main activity
                dataPasser!!.passProfileData(formValuesArray)
                // Load the display fragment


                //Start an activity and pass the data to it.
//                val messageIntent = Intent(this, ProfileDisplayActivity::class.java)
//                messageIntent.putExtra("NAME", "$firstNameValue $lastNameValue")
//                messageIntent.putExtra("AGE", ageValue)
//                messageIntent.putExtra("LOCATION", "$cityValue, $countryValue")
//                messageIntent.putExtra("HEIGHT", heightFeetValue!! * 12 + heightInchesValue!!)
//                messageIntent.putExtra("WEIGHT", weightValue)
//                messageIntent.putExtra("SEX", sexValue)
//                messageIntent.putExtra("ACTIVITY_LEVEL", activityLevelValue)
//                this.startActivity(messageIntent)
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

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
// Finish this
      }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}