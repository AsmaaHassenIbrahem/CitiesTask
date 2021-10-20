package asma.cities.coordinates.api

import asma.cities.coordinates.feature.cities.model.CitiesResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun getCities(): Response<CitiesResponse>

}