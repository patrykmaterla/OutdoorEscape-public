package com.example.outdoorescape

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.kotlindemos.PermissionUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

/**
 * Tracking Fragment, track user location and register it as path.
 */
class RegistrationFragment : Fragment() {

    private var isTracking: Boolean = false
    private var latitude = 0.0
    private var longitude = 0.0
    private var mLocation = LatLng(latitude, longitude)
    private var pathPoints = mutableListOf<Polyline>()
    private val userPath = mutableListOf<LatLng>()
    private lateinit var locationManager: LocationManager
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

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            location: Location? ->
            if (location != null) {
                this.latitude = location.latitude
                this.longitude = location.longitude
                Toast.makeText(
                    context,
                    "${location.latitude} ${location.longitude}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(context, "Location is null", Toast.LENGTH_SHORT).show()
            }

            mLocation = LatLng(latitude, longitude)
            map.addMarker(MarkerOptions().position(mLocation).title("My Marker"))
            map.moveCamera(CameraUpdateFactory.newLatLng(mLocation))
            map.moveCamera(CameraUpdateFactory.zoomTo(15f))
            enableMyLocation()

            drawPolyline(listOf(LatLng(30.00, 20.00), LatLng(35.00, 20.00)))

        }
    }

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
                RegistrationFragment.LOCATION_PERMISSION_REQUEST_CODE, true
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
            RegistrationFragment.LOCATION_PERMISSION_REQUEST_CODE
        )
        // [END maps_check_location_permission]
    }

    private fun toggleRun() {
        if (isTracking) {

        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
    }

    private fun moveCameraToUser() {
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            /**
             * Animate camera movement on the map.
             */
            map.animateCamera(
                /**
                 * Move camera to given lat, lon and zoom
                 */
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    20f
                )
            )
        }
    }

    private fun addAllPolylines() {
        for(polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(Color.RED)
                .width(8f)
                .addAll(polyline)
            map.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(Color.RED)
                .width(8f)
                .add(preLastLatLng)
                .add(lastLatLng)
            map.addPolyline(polylineOptions)
        }
    }

    fun drawPolyline(polylinePoints: List<LatLng>) {
        val polylineOptions = PolylineOptions()
            .width(8f) // Customize width
            .color(Color.BLUE) // Customize color
            .addAll(polylinePoints)
        map.addPolyline(polylineOptions)
    }

    private fun trackAndDrawRoute(location: Location) {
        val newLatLng = LatLng(location.latitude, location.longitude)
        userPath.add(newLatLng)

        // Draw the updated route on the map
        // ...

        drawPolyline(userPath)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationListener = object : android.location.LocationListener {
            override fun onLocationChanged(location: Location) {
                trackAndDrawRoute(location)
                moveCameraToUser()
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5L, 5f, locationListener)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        // Find fragment that is gonna become map, convert it to SupportMapFragment
        // `getMapAsync(callback)` - download maps async and run callback - `onMapReady()`
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        fun newInstance() = RegistrationFragment()
    }

}