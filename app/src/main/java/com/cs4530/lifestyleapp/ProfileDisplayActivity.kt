package com.cs4530.lifestyleapp

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle

class ProfileDisplayActivity : AppCompatActivity() {
    private var heightReceived: String? = null
    private var weightReceived: String? = null
    private var bmrScore: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_display)

        //Get the intent that created this activity.
        val receivedIntent = intent

        //Get the string data
        heightReceived = receivedIntent.getStringExtra("HEIGHT")
        weightReceived = receivedIntent.getStringExtra("WEIGHT")

        //Get the text view where we will display BMR score
        bmrScore = findViewById(R.id.bmr_score)

        if (heightReceived.isNullOrBlank() || weightReceived.isNullOrBlank()) {
            bmrScore!!.text = "-"
        } else {
            val bmr = calculateBMR(heightReceived!!, weightReceived!!.toInt(), "Female", 10 )
            bmrScore!!.text = bmr.toString()
        }
    }

    /* BMR calculation:
    * For men: 66.47 + (6.24 × weight in pounds) + (12.7 × height in inches) − (6.75 × age in years).
    * For women: BMR = 65.51 + (4.35 × weight in pounds) + (4.7 × height in inches) - (4.7 × age in years) */
    private fun calculateBMR(height: String, weight: Int, sex:String, age:Int): Double {
        val heightNoQuotes = height.filter { it.isDigit() }
        val feet = heightNoQuotes.first().code
        val inches = heightNoQuotes.substring(1).toInt()
        val heightInInches = (feet * 12) + inches

        if (sex == "Female") {
            return 65.51 + (4.35 * weight) + (4.7 * heightInInches) - (4.7 * age)
        }
        if (sex == "Male") {
            return 66.47 + (6.24 * weight) + (12.7 * heightInInches) - (6.75 * age)
        }
        return 0.0
    }
}