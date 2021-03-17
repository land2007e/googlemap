package com.t3h.demogooglemap

import android.Manifest
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.lang.Exception


class MyMapFragment : SupportMapFragment(), OnMapReadyCallback,
    GoogleMap.OnMyLocationChangeListener {
    private lateinit var map: GoogleMap
    private var mMarker: Marker? = null
    private var mPolyline: Polyline? = null
    private var lastAddress = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        inits()
    }

    private fun buildLocationRequest(): LocationRequest {
        return LocationRequest.create()
            .setNumUpdates(200000)
            .setExpirationDuration(60000)
            .setInterval(100)
            .setPriority(LocationRequest.PRIORITY_LOW_POWER)
    }


    private fun inits() {
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isScrollGesturesEnabled = true

        map.uiSettings.setAllGesturesEnabled(true)

        //check permission
        val permissions = mutableListOf<String>()
        permissions.add(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if ( PermissionUtils.checkPermission(context!!, permissions)){
            applyChangeLocation()
        }else {
            PermissionUtils.showDialogPermission(activity!!, permissions, 100)
        }
    }

    fun applyChangeLocation(){
        map.isMyLocationEnabled = true
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                Log.d("MyMapFragment", "onLocationResult result............")
                updateLocation(result.lastLocation)
            }

            override fun onLocationAvailability(result: LocationAvailability) {
                Log.d("MyMapFragment", "onLocationAvailability............")
            }
        }
//        LocationServices.getFusedLocationProviderClient(context)
//            .requestLocationUpdates(
//                buildLocationRequest(),
//                callback,
//                Looper.getMainLooper()
//            )
        map.setOnMyLocationChangeListener(this)
    }

    private fun updateLocation(location: Location) {
        Log.d(
            "MyMapFragment", "updateLocation address: " +
                    getLocationString(location.latitude, location.longitude)
        )
        createOrUpdateMarker(location)

    }

    private fun createOrUpdateMarker(location: Location) {
        if (mMarker == null) {
            val option = MarkerOptions()
                .icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_BLUE
                    )
                )
                .position(
                    LatLng(location.latitude, location.longitude)
                )
                .title("My location")
                .snippet(
                    getLocationString(location.latitude, location.longitude)
                )
            mMarker = map.addMarker(option)

            lastAddress = getLocationString(location.latitude, location.longitude)
            createCameraZoom(mMarker!!)

            val lineOp = PolylineOptions()
                .color(Color.GREEN)
                .width(10f)
                .add(
                    LatLng(location.latitude, location.longitude)
                )
            mPolyline = map.addPolyline(lineOp)
        } else {
            val currentAddress = getLocationString(location.latitude, location.longitude)
            if (currentAddress.equals(lastAddress)) {
                return
            }
            mMarker!!.position =
                LatLng(location.latitude, location.longitude)
            mMarker!!.snippet =
                getLocationString(location.latitude, location.longitude)

            lastAddress = currentAddress
            createCameraZoom(mMarker!!)

            //ve line
            val los = mPolyline!!.points
            los.add(
                LatLng(location.latitude, location.longitude)
            )
            mPolyline!!.points = los
        }
    }

    private fun createCameraZoom(marker: Marker) {
        map.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition(
                    marker.position,
                    13f, 30f, 0f
                )
            )
        )
    }

    private fun getLocationString(lat: Double, log: Double): String {
        val geo = Geocoder(context)
        try {
            val addresss = geo.getFromLocation(lat, log, 1)
            if (addresss == null || addresss.size == 0) {
                return "Unknow"
            }
            val add = addresss.get(0)
            var addString = add.getAddressLine(0)
            for (i in 1..add.maxAddressLineIndex - 1) {
                addString += (", " + add.getAddressLine(i))
            }
            addString += ", " + add.countryName
            return addString
        }catch (e :Exception){
            return "Unknow"
        }

    }

    override fun onMyLocationChange(p0: Location) {
        Log.d("MyMapFragment", "onMyLocationChange result............")
        updateLocation(p0)
    }
}