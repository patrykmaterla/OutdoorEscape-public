package com.example.outdoorescape

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.WindowCompat
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
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

/**
 * Tracking Fragment, track user location and register it as path.
 */
class RegistrationFragment : Fragment() {

    private var isTracking: Boolean = false
    private lateinit var myLocation: LatLng
    private var sydneyLocation: LatLng = LatLng(-34.0, 151.0)
    private var latitude = 0.0
    private var longitude = 0.0
    private var mLocation = LatLng(latitude, longitude)
    private var pathPoints = mutableListOf<Polyline>()
    private val userPath = mutableListOf<LatLng>()
    private var registrationTip: Boolean? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var locationManager: LocationManager
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        enableMyLocation()

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                this.latitude = location.latitude
                this.longitude = location.longitude
                myLocation = LatLng(latitude, longitude)
                // Toast.makeText(context, "${location.latitude} ${location.longitude}", Toast.LENGTH_SHORT).show()
            } else {
                myLocation = sydneyLocation
                Toast.makeText(context, "Location is null", Toast.LENGTH_SHORT).show()
            }
            map.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
            map.moveCamera(CameraUpdateFactory.zoomTo(15f))

            drawPolyline(listOf(LatLng(30.00, 20.00), LatLng(35.00, 20.00)))
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
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
        // 2. If a permission rationale dialog should be shown
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            PermissionUtils.RationaleDialog.newInstance(
                CreationFragment.LOCATION_PERMISSION_REQUEST_CODE, true
            ).show(childFragmentManager, "dialog")
            return
        }
        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), CreationFragment.LOCATION_PERMISSION_REQUEST_CODE
        )
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
        sharedPreferences = activity!!.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationListener = object : android.location.LocationListener {
            override fun onLocationChanged(location: Location) {
                trackAndDrawRoute(location)
                moveCameraToUser()
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5L, 5f, locationListener)

        // Show tip in snackbar. If user already seen the message (navigationTip == true) dont show.
        registrationTip = sharedPreferences.getBoolean("registration_tip", false)
        if (registrationTip == false) {
            var snackbar: Snackbar = Snackbar.make(view.findViewById(R.id.myCoordinatorLayout),
                getString(
                    R.string.registration_tip
                ), Snackbar.LENGTH_INDEFINITE)
            var snackbarView = snackbar.view
            var snackTextView: TextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            snackTextView.maxLines = 4
            snackbar
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                .setAction("OK") {
                    sharedPreferences.edit().apply() {
                        putBoolean("registration_tip", true)
                        apply()
                    }
                }.show()
        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        // Find fragment that is gonna become map, convert it to SupportMapFragment
        // `getMapAsync(callback)` - download maps async and run callback - `onMapReady()`
        val mapFragment = childFragmentManager.findFragmentById(R.id.registrationMap) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        // Set location button with bigger top margin
        val locationButton = (mapFragment?.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(Integer.parseInt("2"))
        val rlp =  locationButton.getLayoutParams() as RelativeLayout.LayoutParams
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
        rlp.setMargins(0, 128, 0, 0)
    }

    override fun onResume() {
        super.onResume()

        // Changing status bar color to transparent
        WindowCompat.setDecorFitsSystemWindows(activity!!.window, false)
        activity?.window?.apply {
            // Set the status bar color to transparent
            statusBarColor = Color.TRANSPARENT

            // Add flags to the window to enable drawing behind the status bar
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            decorView.systemUiVisibility = View.STATUS_BAR_HIDDEN;
        }
    }

    override fun onPause() {
        super.onPause()

        activity?.window?.apply {
            // Restore the default status bar behavior
            WindowCompat.setDecorFitsSystemWindows(this, true)
            // Remove the flags that enabled drawing behind the status bar
            decorView.systemUiVisibility = 0
            // Set the status bar color back to the default theme color
            statusBarColor = resources.getColor(R.color.primary)
            val darkModeFlag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK // Retrieve the Mode of the App.
            val isDarkModeOn = darkModeFlag == Configuration.UI_MODE_NIGHT_YES // Check if the Dark Mode is On
            if (isDarkModeOn) {
                decorView.systemUiVisibility = View.STATUS_BAR_VISIBLE;
            }
            else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        fun newInstance() = RegistrationFragment()
    }
}