package uk.co.dawg.gnss.collector.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
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
import uk.co.dawg.gnss.collector.R
import uk.co.dawg.gnss.collector.databinding.FragmentMapBinding
import uk.co.dawg.gnss.collector.extensions.hasPermission
import uk.co.dawg.gnss.collector.location.service.GnssCollectorService
import uk.co.dawg.gnss.collector.showErrorSnackbar
import uk.co.dawg.gnss.collector.showInfoSnackbar
import java.util.*

class MapFragment : Fragment(), PermissionsListener {

    private lateinit var binding: FragmentMapBinding

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
    private lateinit var mapboxStyle: Style
    private lateinit var locationComponent: LocationComponent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // This needs to run before the setContentView otherwise it crashes
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

        // Starts GNSS service and init map button
        binding.btnShareLocation.setOnClickListener {
            checkForFineAccessLocationPermissions {
                startGnssService()
                initializeMap(savedInstanceState)
            }
        }
        // Zoom to user location on 1st init
        binding.btnFocus.setOnClickListener {

            if (this@MapFragment::locationComponent.isInitialized)
                focusOnUserLocation()
            else {
                initializeMap(savedInstanceState)
            }
        }
    }

    private fun checkForFineAccessLocationPermissions(onLocationAccepted: () -> Unit) {
        when {
            requireContext().hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) -> {
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

            val addresses = Geocoder(requireContext(), Locale.getDefault())
                .getFromLocation(it.latitude, it.longitude, 1)

            addresses.firstOrNull()?.getAddressLine(0)?.let { addr ->
                binding.txtLocation.text = "Current location: '$addr'"
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
            this@MapFragment.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.TRAFFIC_NIGHT) {
                this@MapFragment.mapboxStyle = it
                enableLocationComponent(it, mapboxMap)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style, mapboxMap: MapboxMap) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {

            // Create and customize the LocationComponent's options
            val customLocationComponentOptions = LocationComponentOptions.builder(requireContext())
                .elevation(5f)
                .accuracyAlpha(.6f)
                .accuracyColor(Color.BLUE)
                .foregroundDrawable(R.drawable.ic_me)
                .build()

            // Get an instance of the component
            locationComponent = mapboxMap.locationComponent
            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(requireContext(), loadedMapStyle)
                    .locationComponentOptions(customLocationComponentOptions)
                    .useDefaultLocationEngine(true)
                    .build()

            // Activate with options
            locationComponent.activateLocationComponent(locationComponentActivationOptions)
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING

            locationComponent.renderMode = RenderMode.COMPASS
        } else {
            PermissionsManager(this).requestLocationPermissions(requireActivity())
        }
    }

    fun startGnssService() {
        binding.root.showInfoSnackbar("Started tracking your GNSS. Turn off from the notification")
        requireActivity().startService(Intent(requireContext(), GnssCollectorService::class.java))
    }

    //region Lifecycle
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
    //endregion Lifecycle

    //region PermissionListener
    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        binding.root.showErrorSnackbar("You need location permissions to use this application")
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(mapboxStyle, mapboxMap)
        }
    }
    //endregion

    @dagger.Module
    abstract class Module {
        @ContributesAndroidInjector
        abstract fun bind(): MapFragment
    }
}