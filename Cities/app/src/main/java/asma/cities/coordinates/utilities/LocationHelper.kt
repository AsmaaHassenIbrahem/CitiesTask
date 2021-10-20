package asma.cities.coordinates.utilities

import com.google.android.gms.maps.model.LatLng

object LocationHelper {

    fun getLatLngList(points: String): ArrayList<LatLng> {
        var latLngArrayListAfterSplit: ArrayList<LatLng> = ArrayList()

        val latLongArrayList = points.split(",").toTypedArray()
        latLongArrayList?.let {
            for (item in it) {

                var latLng = item.split(" ").toTypedArray()
                val latitude = latLng[0].toDouble()
                val longitude = latLng[1].toDouble()
                latLngArrayListAfterSplit.add(LatLng(latitude, longitude))

            }
        }
        return latLngArrayListAfterSplit
    }
}