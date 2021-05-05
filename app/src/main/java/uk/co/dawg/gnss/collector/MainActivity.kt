package uk.co.dawg.gnss.collector

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import dagger.android.ContributesAndroidInjector
import timber.log.Timber
import uk.co.dawg.gnss.collector.databinding.ActivityMainBinding
import uk.co.dawg.gnss.collector.extensions.hasPermission
import uk.co.dawg.gnss.collector.location.service.GnssCollectorService


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnShareLocation.setOnClickListener { checkForFineAccessLocationPermissions() }

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            if (isGranted) {
                startGnssService()
            } else {
                Timber.i("Denied")
            }
        }
    }

    private fun checkForFineAccessLocationPermissions() {
        when {
            hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                startGnssService()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                binding.showErrorSnackbar("We need your location to track GNSS data")
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun startGnssService() {
        startService(Intent(this, GnssCollectorService::class.java))
    }

    companion object {
        private const val RC_LOCATION = 123

        private val LOCATION_PERMS: Array<String>
            get() {
                return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                } else {
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                }
            }
    }

    @dagger.Module
    abstract class Module {

        @ContributesAndroidInjector
        abstract fun contributeActivity(): MainActivity
    }
}