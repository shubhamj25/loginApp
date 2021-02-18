package com.example.loginapp.screens.home
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.loginapp.*
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
import com.google.android.gms.maps.model.PolygonOptions
import kotlinx.android.synthetic.main.activity_location.*

class LocationActivity : BaseActivity(), OnMapReadyCallback {
    companion object{
        const val UPDATE_INTERVAL = (10 * 1000).toLong()  /* 10 secs */
        const val FAST_INTERVAL: Long = 2000 /* 2 sec */
        const val EQUIVALENT_TO_A_KM=0.009009009009009 // 1km on Map
    }
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var myLocation: LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.location) as SupportMapFragment
        mapFragment.getMapAsync(this as OnMapReadyCallback)
        exit.setColorFilter(Color.parseColor(getString(R.string.black)))
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }

    private fun setListeners(){
        reCenter.setOnClickListener{
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13F))
            detailedLocation.animate().alpha(1.0f)
        }
        exit.setOnClickListener{this.finish()}
        hideDetailedLocation.setOnClickListener{
            detailedLocation.animate().alpha(0.0f)
        }
    }

    @SuppressLint("VisibleForTests")
    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap=googleMap
        mGoogleMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this))
        val location = FusedLocationProviderClient(this).lastLocation
        location.addOnCompleteListener{
                if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1)) {
                    myLocation = LatLng(it.result.latitude, it.result.longitude)
                    setListeners()
                    val geoCoder=Geocoder(this)
                    val address = geoCoder.getFromLocation(myLocation.latitude, myLocation.longitude, 1)
                    val message = address[0].featureName + '\n' + address[0].locality + " ," + address[0].countryName + " - " + address[0].postalCode
                    val locationMarker = googleMap.addMarker(
                            MarkerOptions().position(myLocation).title(getString(R.string.myLocation))
                    )
                    locationMarker.snippet = message
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13F))
                    locationMarker.showInfoWindow()
                    addSquareAround(googleMap)
                    setDescription(address[0].getAddressLine(0)+"\n"+message)
                    locationTitle.text=address[0].locality
                }
        }
    }

    private fun setDescription(address:String){
        locationFullAddress.text=address
        locationProgressBar.visibility=View.GONE
        locationFullAddress.visibility=View.VISIBLE
    }

    private fun addSquareAround(googleMap: GoogleMap){
        val arrayPoints= squareMap(myLocation)
        val polygonOptions = PolygonOptions()
        polygonOptions.addAll(arrayPoints).strokeColor(Color.parseColor(getString(R.string.purple_700)))
        .fillColor(0x551E90FF).strokeWidth(7f).zIndex(1f)
        googleMap.addPolygon(polygonOptions)
    }

    private fun startLocationUpdates() {
        // initialize location request object
        LocationRequest.create().run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = UPDATE_INTERVAL
            setFastestInterval(FAST_INTERVAL)
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
        val loc = LatLng(location.latitude, location.longitude)
        val msg = "Updated Location: " + location.latitude + " , " + location.longitude
        mGoogleMap.clear()
        mGoogleMap.addMarker(MarkerOptions().position(loc).title(msg))
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
    }

    private fun checkPermission(permission: String, requestCode: Int):Boolean{
        // Checking if permission is not granted
       return if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
           ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
           false
       } else {
            showSnackBarOnTop(locationConstraintLayout,this,R.string.permissionGranted,Color.parseColor(getString(R.string.green))).show()
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onMapReady(mGoogleMap)
                } else {
                    showDialog({ _, _ -> this.finish()},R.string.permissionDenied,R.string.permissionDeniedMessage,R.string.dialogPositive)?.show()
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}