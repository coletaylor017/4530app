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


class MainActivity : AppCompatActivity(), View.OnClickListener {
    // Variables for UI elements
    private var mButtonCamera: Button? = null
    private var mButtonSubmit: Button? = null
    private var mIvPic: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get the buttons
        mButtonCamera = findViewById(R.id.PhotoButton)
        mButtonSubmit = findViewById(R.id.button_submit)

        //Say that this class itself contains the listener
        mButtonCamera!!.setOnClickListener(this)
        mButtonSubmit!!.setOnClickListener(this)
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
                // TODO: for now this is hardcoded to pass a height and weight value
                //Start an activity and pass the data to it.
                val messageIntent = Intent(this, ProfileDisplayActivity::class.java)
                messageIntent.putExtra("HEIGHT", "6'0\"")
                messageIntent.putExtra("WEIGHT", "100")
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

}