package com.cs4530.lifestyleapp

import android.content.ActivityNotFoundException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.provider.MediaStore
import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts


class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    // Variables for UI elements
    private var mButtonCamera: Button? = null
    private var mButtonSubmit: Button? = null
    private var mIvPic: ImageView? = null

    private var countryOptions : Array<String> = arrayOf("United States", "Canada", "Ethiopia")
    private var heightFeetOptions : Array<Int> = arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
    private var heightInchesOptions : Array<Int> = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
    private var sexOptions : Array<String> = arrayOf("Prefer not to say", "Female", "Male")
    private var activityLevelOptions : Array<String> = arrayOf("Sedentary", "Lightly active", "Moderately active", "Active", "Very active")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get the buttons
        mButtonCamera = findViewById(R.id.PhotoButton)
        mButtonSubmit = findViewById(R.id.button_submit)

        //Say that this class itself contains the listener
        mButtonCamera!!.setOnClickListener(this)
        mButtonSubmit!!.setOnClickListener(this)

        setSpinnerDataString(R.id.countryInput, countryOptions)
        setSpinnerDataInt(R.id.heightFeetInput, heightFeetOptions)
        setSpinnerDataInt(R.id.heightInchesInput, heightInchesOptions)
        setSpinnerDataString(R.id.sexInput, sexOptions)
        setSpinnerDataString(R.id.activityLevelInput, activityLevelOptions)

    }

    private fun setSpinnerDataString(spinnerId: Int, spinnerOptions: Array<String>) {
        val targetSpinner = findViewById<Spinner>(spinnerId)
        targetSpinner.onItemSelectedListener = this // onItemSelectedListener tells you which item was clicked
        val targetSpinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            spinnerOptions)
        targetSpinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        targetSpinner.adapter = targetSpinnerAdapter
    }


    private fun setSpinnerDataInt(spinnerId: Int, spinnerOptions: Array<Int>) {
        val targetSpinner = findViewById<Spinner>(spinnerId)
        targetSpinner.onItemSelectedListener = this // onItemSelectedListener tells you which item was clicked
        val targetSpinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            spinnerOptions)
        targetSpinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        targetSpinner.adapter = targetSpinnerAdapter
    }

    override fun onClick(view: View) {
        when (view.id) { //Added ? due to warning message. Consider better checks.
            R.id.PhotoButton -> {
                //The button press should open a camera
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    cameraActivity.launch(cameraIntent)
                }catch(ex:ActivityNotFoundException){
                    //Do error handling here
                }
            }
            R.id.button_submit -> {
                val firstNameTextEdit : EditText? = findViewById(R.id.firstNameInput)
                val firstNameValue : String = firstNameTextEdit!!.text.toString()
                val lastNameTextEdit : EditText? = findViewById(R.id.lastNameInput)
                val lastNameValue : String = lastNameTextEdit!!.text.toString()

                val ageTextEdit : EditText? = findViewById(R.id.ageInput)
                val ageValue = Integer.parseInt(ageTextEdit!!.text.toString())

                val cityTextEdit : EditText? = findViewById(R.id.cityInput)
                val cityValue : String = cityTextEdit!!.text.toString()
                val countrySpinner : Spinner? = findViewById(R.id.countryInput)
                val countryValue : String = countrySpinner!!.selectedItem.toString()

                val heightFeetSpinner : Spinner? = findViewById(R.id.heightFeetInput)
                val heightFeetValue : Int = Integer.parseInt(heightFeetSpinner!!.selectedItem.toString())
                val heightInchesSpinner : Spinner? = findViewById(R.id.heightFeetInput)
                val heightInchesValue : Int = Integer.parseInt(heightInchesSpinner!!.selectedItem.toString())

                val weightTextEdit : EditText? = findViewById(R.id.weightInput)
                val weightValue = Integer.parseInt(weightTextEdit!!.text.toString())

                val sexSpinner : Spinner? = findViewById(R.id.sexInput)
                val sexValue : String = sexSpinner!!.selectedItem.toString()

                val activityLevelSpinner : Spinner? = findViewById(R.id.activityLevelInput)
                val activityLevelValue : String = activityLevelSpinner!!.selectedItem.toString()

                //Start an activity and pass the data to it.
                val messageIntent = Intent(this, ProfileDisplayActivity::class.java)
                messageIntent.putExtra("NAME", "$firstNameValue $lastNameValue")
                messageIntent.putExtra("AGE", ageValue)
                messageIntent.putExtra("LOCATION", "$cityValue, $countryValue")
                messageIntent.putExtra("HEIGHT", heightFeetValue * 12 + heightInchesValue)
                messageIntent.putExtra("WEIGHT", weightValue)
                messageIntent.putExtra("SEX", sexValue)
                messageIntent.putExtra("ACTIVITY_LEVEL", activityLevelValue)
                this.startActivity(messageIntent)
            }
        }
    }

    private val cameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if(result.resultCode == RESULT_OK) {
            mIvPic = findViewById<View>(R.id.iv_pic) as ImageView
            //val extras = result.data!!.extras
            //val thumbnailImage = extras!!["data"] as Bitmap?

            if (Build.VERSION.SDK_INT >= 33) {
                val thumbnailImage = result.data!!.getParcelableExtra("data", Bitmap::class.java)
                mIvPic!!.setImageBitmap(thumbnailImage)
            }
            else {
                val thumbnailImage = result.data!!.getParcelableExtra<Bitmap>("data")
                mIvPic!!.setImageBitmap(thumbnailImage)
            }
        }
    }

    // Note: Cosmo Kramer is 6'3" and born in 1949

    override fun onItemSelected(parent: AdapterView<*>?,
                                view: View, position: Int,
                                id: Long) {
            // access selected country using countries[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

}