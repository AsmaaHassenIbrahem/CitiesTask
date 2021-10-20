package asma.cities.coordinates.api

import asma.cities.coordinates.feature.cities.model.CitiesResponse
import retrofit2.Response

class ApiHelperImpl(private val endPoint: EndPoint) : ApiHelper {

    override suspend fun getCities(): Response<CitiesResponse> = endPoint.getCities()


}