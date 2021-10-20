package asma.cities.coordinates.di

import asma.cities.coordinates.api.MainRepository
import org.koin.dsl.module

val repoModule = module {
    single { MainRepository(get()) }
}