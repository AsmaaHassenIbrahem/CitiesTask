package asma.cities.coordinates.api


class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getCities() = apiHelper.getCities()

}