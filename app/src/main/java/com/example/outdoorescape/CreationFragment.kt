package com.example.outdoorescape

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.example.kotlindemos.PermissionUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CreationFragment : Fragment() {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var myLocation: LatLng
    private var sydneyLocation: LatLng = LatLng(-34.0, 151.0)
    private var latitude = 0.0
    private var longitude = 0.0

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
                Toast.makeText(context,
                    getString(R.string.location_is_disabled_on_your_device), Toast.LENGTH_SHORT).show()
            }
            map.addMarker(MarkerOptions().position(myLocation).title(""))
            map.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
            map.moveCamera(CameraUpdateFactory.zoomTo(15f))
            map.uiSettings.isMyLocationButtonEnabled = true
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        // Find fragment that is gonna become map, convert it to SupportMapFragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.fragmentCreationMap) as SupportMapFragment?
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
        // Restore default status bar
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
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
        fun newInstance(param1: String, param2: String) = CreationFragment()
    }
}
