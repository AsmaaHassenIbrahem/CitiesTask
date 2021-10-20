package asma.cities.coordinates.api

data class ResourceState<out T>(val status: StatusApi, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): ResourceState<T> {
            return ResourceState(StatusApi.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): ResourceState<T> {
            return ResourceState(StatusApi.ERROR, data, msg)
        }

        fun <T> loading(data: T?): ResourceState<T> {
            return ResourceState(StatusApi.LOADING, data, null)
        }

        fun <T> networkError(): ResourceState<T> {
            return ResourceState(StatusApi.NETWORK_ERROR, null, null)
        }
    }

}