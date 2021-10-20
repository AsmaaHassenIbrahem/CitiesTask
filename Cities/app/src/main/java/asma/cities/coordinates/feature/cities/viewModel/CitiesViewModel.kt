package asma.cities.coordinates.feature.cities.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import asma.cities.coordinates.R
import asma.cities.coordinates.api.MainRepository
import asma.cities.coordinates.api.ResourceState
import asma.cities.coordinates.base.BaseViewModel
import asma.cities.coordinates.feature.cities.model.CitiesResponse
import asma.cities.coordinates.utilities.Constants.EndPoint.SUCCESS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CitiesViewModel(private val mainRepository: MainRepository)  : BaseViewModel()  {


    val data = MutableLiveData<ResourceState<CitiesResponse>>()
    val dataState: LiveData<ResourceState<CitiesResponse>>
        get() = data


     fun getCities() {
        viewModelScope.launch {
            executeApiCall()
        }
    }

    private suspend fun executeApiCall() = withContext(Dispatchers.Default) {
        data.postValue(ResourceState.loading(null))
        try {
            mainRepository.getCities().let {
                if (it.isSuccessful) {
                    if (it.body()?.status == SUCCESS && it.code() == 200) {
                        data.postValue(ResourceState.success(it.body()))
                    } else {
                        data.postValue(
                            ResourceState.error(
                                it.body()?.message.toString(),
                                it.body()
                            )
                        )
                    }

                } else {
                    data.postValue(ResourceState.error(it.raw().message, null))
                }
            }
        } catch (e: Exception) {
            data.postValue(ResourceState.error(R.string.server_error.toString(), null)
            )
        }    }


    override fun handleRetry() {
        getCities()
    }
}