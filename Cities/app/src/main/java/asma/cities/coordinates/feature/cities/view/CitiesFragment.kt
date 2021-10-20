package asma.cities.coordinates.feature.cities.view

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import asma.cities.coordinates.R
import asma.cities.coordinates.api.StatusApi
import asma.cities.coordinates.base.BaseFragment
import asma.cities.coordinates.databinding.FragmentCitiesBinding
import asma.cities.coordinates.feature.Polygon.BoundariesFragment.Companion.CITY_ITEM_KEY
import asma.cities.coordinates.feature.cities.model.CityItem
import asma.cities.coordinates.feature.cities.viewModel.CitiesViewModel
import com.google.android.gms.maps.model.LatLng
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class CitiesFragment : BaseFragment<FragmentCitiesBinding, CitiesViewModel>(), CityClickListener {

    override val viewModel: CitiesViewModel by viewModel()

    private var mLat = 0.0
    private var mLong = 0.0
    private var latLong: LatLng? = null

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

    override fun layout(): Int = R.layout.fragment_cities

    override fun init() {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        binding.refreshLayout.setOnRefreshListener{ viewModel.getCities() }
        setupObserver()
    }


    private fun setupObserver() {

        viewModel.dataState.observe(this, {
            when (it.status) {
                StatusApi.SUCCESS -> {
                    binding.refreshLayout.isRefreshing = false
                    binding.citiesView.apply {
                        layoutManager = LinearLayoutManager(activity)
                        adapter = it.data?.cities?.let { cities -> CitiesAdapter(cities, latLong,this@CitiesFragment) }
                    }
                    binding.loadingView.isVisible = false
                    binding.citiesView.isVisible = true
                }
                StatusApi.LOADING -> {
                    binding.refreshLayout.isRefreshing = false
                    binding.loadingView.isVisible = true
                    binding.citiesView.isVisible = false
                }
                StatusApi.ERROR -> {
                    binding.refreshLayout.isRefreshing = false
                    binding.loadingView.isVisible = false
                    it.message?.let { msg -> showError(activity, msg) }
                }
                StatusApi.NETWORK_ERROR -> {
                    binding.refreshLayout.isRefreshing = false
                    binding.loadingView.isVisible = false
                    showNetworkError(activity)

                }
            }
        })
    }

    override fun onCityClick(view: View, cityItem: CityItem) {
        val bundle = Bundle()
        bundle.putParcelable(CITY_ITEM_KEY, cityItem)
        Navigation.findNavController(view).navigate(R.id.action_citiesFragment_to_boundariesFragment,bundle)
    }

    private fun permission(){
        if (activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_ACCESS_FINE_LOCATION) }
            return
        }else{
            getLocation()
        }
    }

    private fun getLocation() {

        Toast.makeText(activity, "finding your location!", Toast.LENGTH_SHORT).show()

        var locationManager = activity?.getSystemService(LOCATION_SERVICE)as LocationManager?

        var locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                location.let { loc ->
                    latLong = loc?.latitude?.let { lat ->
                        mLat = lat
                        loc?.longitude?.let { long ->
                            mLong = long
                            LatLng(
                                lat,
                                long
                            )
                        }
                    }
                    viewModel.getCities()

                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

        }

        try {
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch (ex: SecurityException) {

        }
        if (activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
            != PackageManager.PERMISSION_GRANTED) {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_ACCESS_FINE_LOCATION) }
            return
        }
        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> getLocation()
                PackageManager.PERMISSION_DENIED -> permission()
            }
        }
    }
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            getLocation()
            // Do if the permission is granted
        }
        else {
            // Do otherwise
        }
    }

}
