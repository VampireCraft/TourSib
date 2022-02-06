package dt.prod.patternvm.core.network

import dt.prod.patternvm.createProblem.domain.CreateEventApi
import dt.prod.patternvm.listProblem.domain.EventApi
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit

object NetworkHolder {
    const val baseurl = "https://dt-box.tk/"

    var retrofitClient: Retrofit
    var httpClient: OkHttpClient

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 3

        httpClient = OkHttpClient().newBuilder()
            .followRedirects(true)
            .followSslRedirects(false)
            .addInterceptor(interceptor)
            .dispatcher(dispatcher)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        retrofitClient = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseurl)
            .client(httpClient)
            .build()
    }

    fun getCreateEventApi(): CreateEventApi {
        return retrofitClient.create(CreateEventApi::class.java)
    }

    fun getEventApi(): EventApi {
        return retrofitClient.create(EventApi::class.java)
    }
}