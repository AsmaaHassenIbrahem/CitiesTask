package asma.cities.coordinates.di

import asma.cities.coordinates.feature.cities.viewModel.CitiesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module(override = true) {
    viewModel { CitiesViewModel(get()) }
}
