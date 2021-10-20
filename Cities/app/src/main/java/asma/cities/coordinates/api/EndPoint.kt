package asma.cities.coordinates.api

import asma.cities.coordinates.feature.cities.model.CitiesResponse
import asma.cities.coordinates.utilities.Constants
import retrofit2.Response
import retrofit2.http.*

interface EndPoint {

    @GET(Constants.EndPoint.CITIES)
    suspend fun getCities(): Response<CitiesResponse>

}