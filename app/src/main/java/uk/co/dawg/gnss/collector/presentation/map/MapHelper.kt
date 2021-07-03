package uk.co.dawg.gnss.collector.presentation.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import uk.co.dawg.gnss.collector.R
import javax.inject.Inject

class MapHelper @Inject constructor(
    private val context: Context
) {

    fun initializeHoveringMarker(mapView: MapView): ImageView {
        return ImageView(context).also {
            it.setImageResource(R.drawable.ic_red_marker)
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
            it.layoutParams = params
            it.visibility = View.INVISIBLE

            mapView.addView(it);
        }
    }

    fun initDroppedMarker(loadedMapStyle: Style) {
        (AppCompatResources.getDrawable(
            context,
            R.drawable.ic_blue_marker
        ) as? VectorDrawable)?.let {

            val bitmap = Bitmap.createBitmap(
                it.intrinsicWidth,
                it.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            it.setBounds(0, 0, canvas.width, canvas.height)
            it.draw(canvas)

            bitmap

        }?.let { bitmap ->
            loadedMapStyle.addImage("dropped-icon-image", bitmap)
            loadedMapStyle.addSource(GeoJsonSource("dropped-marker-source-id"))
            loadedMapStyle.addLayer(
                SymbolLayer(
                    DROPPED_MARKER_LAYER_ID,
                    "dropped-marker-source-id"
                ).withProperties(
                    PropertyFactory.iconImage("dropped-icon-image"),
                    PropertyFactory.visibility(Property.NONE),
                    PropertyFactory.iconAllowOverlap(true),
                    PropertyFactory.iconIgnorePlacement(true)
                )
            )
        }
    }


    fun showBlueMarkerOnCameraCenter(map: MapboxMap, style: Style) {
        val mapTargetLatLng = map.getCameraLatLng()

        style.getLayer(DROPPED_MARKER_LAYER_ID)?.let { layer ->

            // Here we are updating the marker's location
            style.getSourceAs<GeoJsonSource>(DROPPED_MARKER_SOURCE_ID)?.let { source ->
                source.setGeoJson(
                    Point.fromLngLat(
                        mapTargetLatLng.longitude,
                        mapTargetLatLng.latitude
                    )
                )

                layer.setProperties(PropertyFactory.visibility(Property.VISIBLE))
            }
        }
    }

    fun hideBlueMarker(style: Style) {
        style.getLayer(DROPPED_MARKER_LAYER_ID)?.let { layer ->
            layer.setProperties(PropertyFactory.visibility(Property.NONE))
        }
    }

    companion object {
        const val DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID"
        const val DROPPED_MARKER_SOURCE_ID = "dropped-marker-source-id"


        fun MapboxMap.getCameraLatLng(): LatLng {
            return cameraPosition.target
        }
    }

}