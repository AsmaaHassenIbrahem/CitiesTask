package asma.cities.coordinates.feature.Polygon

import android.os.Build
import androidx.annotation.RequiresApi
import asma.cities.coordinates.R
import asma.cities.coordinates.base.BaseMapFragment
import asma.cities.coordinates.databinding.FragmentBoundariesBinding
import asma.cities.coordinates.feature.cities.model.CityItem
import asma.cities.coordinates.utilities.LocationHelper
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*


class BoundariesFragment : BaseMapFragment<FragmentBoundariesBinding>(), OnMapReadyCallback {
   var mapFragment: SupportMapFragment? = null

    private var cityItem : CityItem? = null
    companion object {
        const val CITY_ITEM_KEY = "cityItemKey"
    }

    override fun layout(): Int = R.layout.fragment_boundaries

    override fun init() {
        cityItem = arguments?.getParcelable(CITY_ITEM_KEY)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment

        mapFragment?.getMapAsync(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMapReady(googleMap: GoogleMap) {
        cityItem?.let {
            val position = it.lat?.let { it1 -> it.lon?.let { it2 -> LatLng(it1, it2) } }

            val markerOptions = MarkerOptions()
                .position(position)
                .title(it.name)
            googleMap.addMarker(markerOptions)

            var circleOptions = CircleOptions()
                .center(position).radius(50000.0)

            googleMap.addCircle(circleOptions)

            it.points?.let {
                val points = LocationHelper.getLatLngList(it)
                var polygonOptions = PolygonOptions()
                    .strokeColor(resources.getColor(R.color.teal_700,null)).fillColor(resources.getColor(R.color.pink, null))
                    .addAll(points)

                googleMap.addPolygon(polygonOptions)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(points[5],14f))

            }
        }
    }
}