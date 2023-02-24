package com.cs4530.lifestyleapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import java.io.File

class ProfileDisplayActivity : AppCompatActivity() {

    private var nameReceived: String? = null
    private var ageReceived : Int? = null
    private var locationReceived : String? = null
    private var heightReceived: Int? = null // height in inches
    private var weightReceived: Int? = null
    private var sexReceived : String? = null
    private var activityLevelReceived : String? = null
    private var bmrScore: TextView? = null
    private var mIvPic: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)

        //Get the intent that created this activity.
        val receivedIntent = intent

        //Get the string data
        nameReceived = receivedIntent.getStringExtra("NAME")
        ageReceived = receivedIntent.getIntExtra("AGE", 0)
        locationReceived = receivedIntent.getStringExtra("LOCATION")
        heightReceived = receivedIntent.getIntExtra("HEIGHT", 0)
        weightReceived = receivedIntent.getIntExtra("WEIGHT", 0)
        sexReceived = receivedIntent.getStringExtra("SEX")
        activityLevelReceived = receivedIntent.getStringExtra("ACTIVITY_LEVEL")

        // Display received data in text views
        findViewById<TextView>(R.id.name_value)!!.text = nameReceived!!.toString()
        findViewById<TextView>(R.id.age_value)!!.text = ageReceived!!.toString()
        findViewById<TextView>(R.id.location_value)!!.text = locationReceived!!.toString()
        findViewById<TextView>(R.id.height_value)!!.text =
            (heightReceived!! / 12).toInt().toString() + "\' " + (heightReceived!! % 12) + "\""
        findViewById<TextView>(R.id.weight_value)!!.text = weightReceived!!.toString()
        findViewById<TextView>(R.id.sex_value)!!.text = sexReceived!!.toString()
        findViewById<TextView>(R.id.activity_level_value)!!.text = activityLevelReceived!!.toString()

        //Get the text view where we will display BMR score
        bmrScore = findViewById(R.id.bmr_score)

        if (heightReceived!! == 0 || weightReceived!! == 0 || ageReceived!! == 0 || sexReceived!!.isNullOrBlank()) {
            bmrScore!!.text = "-"
        } else {
            val bmr = calculateBMR(heightReceived!!, weightReceived!!, sexReceived!!, ageReceived!!)
            bmrScore!!.text = bmr.toString()
        }

        /** The next few lines retrieve the photo from cache and redraw them **/
        var bits : Bitmap? = getBitmapFromCache()
        if(bits != null) {
            mIvPic = findViewById<View>(R.id.profile_pic) as ImageView
            mIvPic!!.setImageBitmap(bits)
        }

        /** The next lines create listener for the Find Hike button **/
        val hikeButton = findViewById<Button>(R.id.find_hike)
        hikeButton.setOnClickListener {
            //TODO: Update based on user location
            val locationOfUser: String = "40.000, -111.000" //Temporary
            val searchUri = Uri.parse("geo:"+locationOfUser+"?q=hikes near me")
            //create map intent
            val mapIntent = Intent(Intent.ACTION_VIEW, searchUri)

            try{
                startActivity(mapIntent)
            }catch(ex: ActivityNotFoundException) {
                Toast.makeText(this, "Map Not Available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /* BMR calculation:
    * For men: 66.47 + (6.24 × weight in pounds) + (12.7 × height in inches) − (6.75 × age in years).
    * For women: BMR = 65.51 + (4.35 × weight in pounds) + (4.7 × height in inches) - (4.7 × age in years) */
    private fun calculateBMR(height: Int, weight: Int, sex:String, age:Int): Int {

        if (sex == "Female") {
            return (65.51 + (4.35 * weight) + (4.7 * height) - (4.7 * age)).toInt()
        }
        if (sex == "Male") {
            return (66.47 + (6.24 * weight) + (12.7 * height) - (6.75 * age)).toInt()
        }
        return 0
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
}