package asma.cities.coordinates.feature.cities.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class CitiesResponse(
    @Json(name = "status")
    var status : String?  = null,

    @Json(name = "message")
    var message : String? = null,

    @Json(name = "cities")
    val cities: List<CityItem>? = null)

data class CityItem(
    @Json(name = "name")
    val name: String? = null,

    @Json(name = "lat")
    val lat : Double? = 0.0,

    @Json(name = "lon")
    val lon : Double? = 0.0 ,

    @Json(name = "r")
    val r : Int? = 0 ,

    @Json(name = "points")
    val points: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeValue(lat)
        parcel.writeValue(lon)
        parcel.writeValue(r)
        parcel.writeString(points) }

    override fun describeContents(): Int { return 0 }

    companion object CREATOR : Parcelable.Creator<CityItem> {
        override fun createFromParcel(parcel: Parcel): CityItem {
            return CityItem(parcel) }

        override fun newArray(size: Int): Array<CityItem?> {
            return arrayOfNulls(size) }
    }
}


