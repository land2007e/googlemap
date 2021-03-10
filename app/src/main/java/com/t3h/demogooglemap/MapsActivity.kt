package com.t3h.demogooglemap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class MapsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        supportFragmentManager.beginTransaction()
            .add(R.id.content, MyMapFragment())
            .commit()
    }

}