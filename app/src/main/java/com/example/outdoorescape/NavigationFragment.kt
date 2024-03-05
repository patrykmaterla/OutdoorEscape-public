package com.example.outdoorescape

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.updateLayoutParams
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
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text

class NavigationFragment : Fragment() {

    // private val UPDATE_INTERVAL: Long = (10 * 1000).toLong()
    // private val FASTEST_INTERVAL: Long = 2000
    // private lateinit var location: Location
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    // Shared preference is used to show or don't show tip in snackbar.
    private lateinit var sharedPreferences: SharedPreferences
    // private var mLocationRequest: LocationRequest? = null
    private lateinit var myLocation: LatLng
    private var latitude = 0.0
    private var longitude = 0.0
    private var sydneyLocation: LatLng = LatLng(-34.0, 151.0)
    // private var permissionDenied = false
    private var navigationTip: Boolean? = null

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
                Toast.makeText(context, R.string.location_is_disabled_on_your_device, Toast.LENGTH_SHORT).show()
            }
            // map.addMarker(MarkerOptions().position(myLocation).title("Marker in Sydney"))
            map.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
            map.moveCamera(CameraUpdateFactory.zoomTo(15f))
            map.uiSettings.isMyLocationButtonEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = activity!!.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_navigation, container, false)

        // Show tip in snackbar. If user already seen the message (navigationTip == true) dont show.
        navigationTip = sharedPreferences.getBoolean("navigation_tip", false)
        if (navigationTip == false) {
            var snackbar: Snackbar = Snackbar.make(view.findViewById(R.id.myCoordinatorLayout),
                getString(
                    R.string.navigation_tip
                ), Snackbar.LENGTH_INDEFINITE)
            var snackbarView = snackbar.view
            var snackTextView: TextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            snackTextView.maxLines = 4
            snackbar
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                .setAction("OK") {
                    sharedPreferences.edit().apply() {
                        putBoolean("navigation_tip", true)
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
        val mapFragment = childFragmentManager.findFragmentById(R.id.navigationMap) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        // Set location button with bigger top margin
        val locationButton = (mapFragment?.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(Integer.parseInt("2"))
        val rlp =  locationButton.getLayoutParams() as RelativeLayout.LayoutParams
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
        rlp.setMargins(0, 128, 0, 0)
    }

    // @SuppressLint("MissingPermission")
    // private fun getLastLocation() {
    //     fusedLocationProviderClient.lastLocation.addOnSuccessListener {
    //         location: Location? ->
    //         this.latitude = location!!.latitude
    //         this.longitude = location!!.longitude
    //         Toast.makeText(context, "${location.latitude} ${location.longitude}", Toast.LENGTH_SHORT).show()
    //     }
    // }

    override fun onStart() {
        super.onStart()
        // startLocationUpdates()
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

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
