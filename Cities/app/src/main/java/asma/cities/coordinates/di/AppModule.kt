package asma.cities.coordinates.di

import android.content.Context
import asma.cities.coordinates.utilities.Constants
import asma.cities.coordinates.api.ApiHelper
import asma.cities.coordinates.api.ApiHelperImpl
import asma.cities.coordinates.api.EndPoint
import asma.cities.coordinates.utilities.NetworkHelper
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val appModule = module {
    single { provideOkHttpClient(androidContext()) }
    single { provideRetrofit(get(), Constants.EndPoint.BASE_URL) }
    single { provideApiService(get()) }
    single { provideNetworkHelper() }

    single<ApiHelper> {
        return@single ApiHelperImpl(get())
    }
}

private fun provideNetworkHelper() = NetworkHelper

private fun provideOkHttpClient(context: Context): OkHttpClient {
    val cacheSize = (5 * 1024 * 1024).toLong()
    val myCache = Cache(context.cacheDir, cacheSize)
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

    return OkHttpClient.Builder()
        .cache(myCache)
        .addInterceptor { chain ->
            var request = chain.request()
            request = if (NetworkHelper.isNetworkConnected())
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5)
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .build()
            else
                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .build()
            chain.proceed(request)
        }.addInterceptor(loggingInterceptor)
        .build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(okHttpClient)
        .build()

private fun provideApiService(retrofit: Retrofit): EndPoint =
    retrofit.create(EndPoint::class.java)
