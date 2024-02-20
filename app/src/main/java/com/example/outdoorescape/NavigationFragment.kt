package com.example.outdoorescape

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kotlindemos.PermissionUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class NavigationFragment : Fragment() {

    private val UPDATE_INTERVAL: Long = (10 * 1000).toLong()
    private val FASTEST_INTERVAL: Long = 2000
    private var latitude = 50.0
    private var longitude = 18.0
    private var mLocationRequest: LocationRequest? = null

    private var permissionDenied = false
    private lateinit var location: Location

    private lateinit var map: GoogleMap

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        map = googleMap
        // var sydney = LatLng(-34.0, 151.0)

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {

            // TODO
            // location: Location? ->
            // this.latitude = location!!.latitude
            // this.longitude = location!!.longitude
            // Toast.makeText(context, "${location.latitude} ${location.longitude}", Toast.LENGTH_SHORT).show()

                location: Location? ->
            if (location != null) {
                this.latitude = location.latitude
                this.longitude = location.longitude
                Toast.makeText(context, "${location.latitude} ${location.longitude}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Location is null", Toast.LENGTH_SHORT).show()
            }



            var sydney = LatLng(latitude, longitude)
            map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            map.moveCamera(CameraUpdateFactory.zoomTo(15f))
            enableMyLocation()

            /**
             * Straight red line between 2 locations
             */
            // val startPoint = LatLng(latitude, longitude)
            // val endPoint = LatLng(50.00, 45.00)
            // val polylineOptions = PolylineOptions()
            //     .add(startPoint, endPoint)
            //     .color(Color.RED)
            //     .width(5f)
            // googleMap.addPolyline(polylineOptions)


        }

        // Toast.makeText(context, "${location.latitude} ${location.longitude}", Toast.LENGTH_LONG).show()

        // var sydney = LatLng(latitude, 0.0)
        // map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        // map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        // enableMyLocation()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        // Find fragment that is gonna become map, convert it to SupportMapFragment
        // `getMapAsync(callback)` - download maps async and run callback - `onMapReady()`
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            location: Location? ->
            this.latitude = location!!.latitude
            this.longitude = location!!.longitude
            Toast.makeText(context, "${location.latitude} ${location.longitude}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        // startLocationUpdates()
    }

    /*
    // protected fun startLocationUpdates() {
    //     // initialize location request object
    //     mLocationRequest = LocationRequest.create()
    //     mLocationRequest!!.run {
    //         setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    //         setInterval(UPDATE_INTERVAL)
    //         setFastestInterval(FASTEST_INTERVAL)
    //     }
    //
    //     // initialize location setting request builder object
    //     val builder = LocationSettingsRequest.Builder()
    //     builder.addLocationRequest(mLocationRequest!!)
    //     val locationSettingsRequest = builder.build()
    //
    //     // initialize location service object
    //     val settingsClient = LocationServices.getSettingsClient(this)
    //     settingsClient!!.checkLocationSettings(locationSettingsRequest)
    //
    //     // call register location listener
    //     // registerLocationListner()
    // }

    // private fun registerLocationListner() {
    //     // initialize location callback object
    //     val locationCallback = object : LocationCallback() {
    //         override fun onLocationResult(locationResult: LocationResult?) {
    //             onLocationChanged(locationResult!!.getLastLocation())
    //         }
    //     }
    //     // 4. add permission if android version is greater then 23
    //     if(Build.VERSION.SDK_INT >= 23 && checkPermission()) {
    //         LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper())
    //     }
    // }
    //
    // //
    // private fun onLocationChanged(location: Location) {
    //     // create message for toast with updated latitude and longitudefa
    //     var msg = "Updated Location: " + location.latitude  + " , " +location.longitude
    //
    //     // show toast message with updated location
    //     //Toast.makeText(this,msg, Toast.LENGTH_LONG).show()
    //     val location = LatLng(location.latitude, location.longitude)
    //     mGoogleMap!!.clear()
    //     mGoogleMap!!.addMarker(MarkerOptions().position(location).title("Current Location"))
    //     mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    // }
    //
    // private fun checkPermission() : Boolean {
    //     if (ContextCompat.checkSelfPermission(context!! , android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
    //         return true;
    //     } else {
    //         requestPermissions()
    //         return false
    //     }
    // }
    //
    // private fun requestPermissions() {
    //     requestPermissions(arrayOf("Manifest.permission.ACCESS_FINE_LOCATION"),1)
    // }
    //
    // override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    //     super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    //     if(requestCode == 1) {
    //         if (permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION ) {
    //             registerLocationListner()
    //         }
    //     }
    // }
     */

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    /*
    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {

        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mGoogleMap.isMyLocationEnabled = true
            return
        }

        // 2. If if a permission rationale dialog should be shown
        // if (ActivityCompat.shouldShowRequestPermissionRationale(
        //         requireActivity(),
        //         Manifest.permission.ACCESS_FINE_LOCATION
        //     ) || ActivityCompat.shouldShowRequestPermissionRationale(
        //         requireActivity(),
        //         Manifest.permission.ACCESS_COARSE_LOCATION
        //     )
        // ) {
        //     PermissionUtils.RationaleDialog.newInstance(
        //         LOCATION_PERMISSION_REQUEST_CODE, true
        //     ).show(supportFragmentManager, "dialog")
        //     return
        // }

        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }
     */

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {

        // [START maps_check_location_permission]
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            return
        }

        // 2. If if a permission rationale dialog should be shown
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            PermissionUtils.RationaleDialog.newInstance(
                LOCATION_PERMISSION_REQUEST_CODE, true
            ).show(childFragmentManager, "dialog")
            return
        }

        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
        // [END maps_check_location_permission]
    }



    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}