package com.example.loginapp.screens.home
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.loginapp.BaseActivity
import com.example.loginapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_location.*
class LocationActivity : BaseActivity(), OnMapReadyCallback {
    private val updateInterval = (10 * 1000).toLong()  /* 10 secs */
    private val fastInterval: Long = 2000 /* 2 sec */
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var myLocation: LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.location) as SupportMapFragment
        mapFragment.getMapAsync(this as OnMapReadyCallback)
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap=googleMap
        mGoogleMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this))
        val location = FusedLocationProviderClient(this).lastLocation
        location.addOnCompleteListener{
                if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1,mGoogleMap)) {
                    myLocation = LatLng(it.result.latitude, it.result.longitude)
                    val geoCoder = Geocoder(this)
                    val address = geoCoder.getFromLocation(myLocation.latitude, myLocation.longitude, 1)
                    val message = address[0].featureName + '\n' + address[0].locality + " ," + address[0].countryName + " - " + address[0].postalCode
                    val locationMarker = googleMap.addMarker(
                            MarkerOptions().position(myLocation).title(getString(R.string.myLocation))
                    )
                    locationMarker.snippet = message
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17F)).also {
                        locationMarker.showInfoWindow()
                    }
                }
        }
    }
    private fun startLocationUpdates() {
        // initialize location request object
        LocationRequest.create().run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = updateInterval
            setFastestInterval(fastInterval)
        }
        registerLocationListener()
    }

    private fun registerLocationListener() {
        // initialize location callback object
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                onLocationChanged(locationResult!!.lastLocation)
            }
        }
    }

    private fun onLocationChanged(location: Location) {
        // create message for toast with updated latitude and longitude
        val location = LatLng(location.latitude, location.longitude)
        val msg = "Updated Location: " + location.latitude + " , " + location.longitude
        mGoogleMap.clear()
        mGoogleMap.addMarker(MarkerOptions().position(location).title(msg))
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    private fun checkPermission(permission: String, requestCode: Int,mGoogleMap: GoogleMap):Boolean{
        // Checking if permission is not granted
       return if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
           ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
           false
       } else {
            Snackbar.make(locationConstraintLayout, getString(R.string.permissionGranted), Snackbar.LENGTH_LONG).setBackgroundTint(Color.parseColor(getString(R.string.green))).setTextColor(Color.WHITE).show()
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onMapReady(mGoogleMap)
                } else {
                    Snackbar.make(locationConstraintLayout, getString(R.string.permissionDenied), Snackbar.LENGTH_LONG).setBackgroundTint(Color.parseColor(getString(R.string.deepRed))).setTextColor(Color.WHITE).show()
                    this.finish()
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}