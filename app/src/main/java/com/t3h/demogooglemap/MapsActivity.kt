package com.t3h.demogooglemap

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class MapsActivity : AppCompatActivity() {

    private lateinit var fr :MyMapFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        fr = MyMapFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.content, fr)
            .commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ( requestCode == 100){
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED){
                    return
                }
            }
            fr.applyChangeLocation()
        }
    }

}