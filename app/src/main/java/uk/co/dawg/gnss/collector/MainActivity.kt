package uk.co.dawg.gnss.collector

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import dagger.android.ContributesAndroidInjector
import timber.log.Timber
import uk.co.dawg.gnss.collector.databinding.ActivityMainBinding
import uk.co.dawg.gnss.collector.extensions.hasPermission
import uk.co.dawg.gnss.collector.location.service.GnssCollectorService
import java.util.*

class MainActivity : AppCompatActivity(), PermissionsListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
    private lateinit var mapboxStyle: Style
    private lateinit var locationComponent: LocationComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This needs to run before the setContentView otherwise it crashes
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            if (isGranted) {
                startGnssService()
            } else {
                Timber.i("Denied")
            }
        }

        initializeMap(savedInstanceState)


        binding.btnShareLocation.setOnClickListener {
            checkForFineAccessLocationPermissions {
                startGnssService()
                initializeMap(savedInstanceState)
            }
        }

        binding.btnFocus.setOnClickListener {
            if(this@MainActivity::locationComponent.isInitialized)
                focusOnUserLocation()
            else {
                initializeMap(savedInstanceState)
            }
        }
    }

    private fun focusOnUserLocation() {
        mapboxMap.locationComponent.lastKnownLocation?.let {
            val position = CameraPosition.Builder()
                .target(LatLng(it.latitude, it.longitude)) // Sets the new camera position
                .zoom(17.0) // Sets the zoom
                .bearing(it.bearing.toDouble()) // Rotate the camera
                .tilt(30.0) // Set the camera tilt
                .build() // Creates a CameraPosition from the builder


            mapboxMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(position),
                7000
            )

            val addresses = Geocoder(this@MainActivity, Locale.getDefault())
                .getFromLocation(it.latitude, it.longitude, 1)

            addresses.firstOrNull()?.getAddressLine(0)?.let { addr ->
                binding.txtLocation.text = "Im in '$addr'"

                if (binding.txtLocation.visibility == View.GONE) {
                    binding.txtLocation.visibility = View.VISIBLE
                }
            }

        }
    }

    private fun initializeMap(savedInstanceState: Bundle?) {
        mapView = binding.mapView
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { mapboxMap ->
            this@MainActivity.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.DARK) {
                this@MainActivity.mapboxStyle = it
                enableLocationComponent(it, mapboxMap)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style, mapboxMap: MapboxMap) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Create and customize the LocationComponent's options
            val customLocationComponentOptions = LocationComponentOptions.builder(this)
                .elevation(5f)
                .accuracyAlpha(.6f)
                .accuracyColor(Color.BLUE)
                .foregroundDrawable(R.drawable.ic_me)
                .build()

            // Get an instance of the component
            locationComponent = mapboxMap.locationComponent
            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(this, loadedMapStyle)
                    .locationComponentOptions(customLocationComponentOptions)
                    .useDefaultLocationEngine(true)
                    .build()

            // Activate with options
            locationComponent.activateLocationComponent(locationComponentActivationOptions)
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING

            locationComponent.renderMode = RenderMode.COMPASS
        } else {
            PermissionsManager(this).requestLocationPermissions(this)
        }
    }

    private fun checkForFineAccessLocationPermissions(onLocationAccepted: ()->Unit) {
        when {
            hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                onLocationAccepted()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                binding.showErrorSnackbar("We need your location to track GNSS data")
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    fun startGnssService() {
        binding.root.showInfoSnackbar("Started tracking your GNSS. Turn off from the notification")
        startService(Intent(this, GnssCollectorService::class.java))
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        binding.root.showErrorSnackbar("You need location permissions to use this application")
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(mapboxStyle, mapboxMap)
        }
    }

    @dagger.Module
    abstract class Module {

        @ContributesAndroidInjector
        abstract fun contributeActivity(): MainActivity
    }
}