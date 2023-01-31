package com.cs4530.lifestyleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(), View.OnClickListener {
    //Create variables for the UI elements that we need to control
    private var mButtonSubmit: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get the button
        mButtonSubmit = findViewById(R.id.button_submit)

        //Say that this class itself contains the listener.
        mButtonSubmit!!.setOnClickListener(this)
    }

    //Handle clicks for ALL buttons here
    override fun onClick(view: View) {
        when (view.id) {
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
}