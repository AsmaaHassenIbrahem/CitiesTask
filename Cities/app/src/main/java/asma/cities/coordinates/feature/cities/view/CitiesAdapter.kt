package asma.cities.coordinates.feature.cities.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import asma.cities.coordinates.R
import asma.cities.coordinates.databinding.ItemCityBinding
import asma.cities.coordinates.feature.cities.model.CityItem
import asma.cities.coordinates.utilities.toLocation
import com.google.android.gms.maps.model.LatLng

class CitiesAdapter(
    private val ciyList: List<CityItem>,
    private val userLatLng : LatLng?,
    private val cityClickListener: CityClickListener
) : RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemCityBinding>(
            LayoutInflater.from(parent.context), R.layout.item_city, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = ciyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city: CityItem = ciyList[position]
        holder.bind(city)
    }

    inner class ViewHolder(private val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CityItem) {
            with(binding) {
                cityItem = item
                nameView.text = item.name
                root.setOnClickListener { cityClickListener.onCityClick(root,item) }

                val distance = userLatLng?.toLocation()?.distanceTo(item.lat?.let { item.lon?.let { it1 ->
                    LatLng(it, it1) .toLocation() } })
                distanceView.text = distance.toString()
            }
        }
    }

}

 interface CityClickListener {
    fun onCityClick(view: View ,cityItem: CityItem)
}