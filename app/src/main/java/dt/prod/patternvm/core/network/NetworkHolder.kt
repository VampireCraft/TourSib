package dt.prod.patternvm.core.network

import dt.prod.patternvm.createProblem.network.CreateEventApi
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit

object NetworkHolder {
    const val baseurl = "https://dt-box.tk/"

     //   const val baseurl = "https://comewithme.online/"
    var retrofitClient: Retrofit
    //var authenticatorRetrofitClient: Retrofit
    var httpClient: OkHttpClient

    init {
//        authenticatorRetrofitClient = Retrofit.Builder()
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(baseurl)
//            .build()
        val interceptor = HttpLoggingInterceptor()
//            if (BuildConfig.DEBUG)
        interceptor.level = HttpLoggingInterceptor.Level.BODY
//            else
//        interceptor.level = HttpLoggingInterceptor.Level.NONE
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

//    fun getLogInApi(): LogInApi {
//        return retrofitClient.create(LogInApi::class.java)
//    }
//
//    fun getSignUpApi(): SignUpApi {
//        return retrofitClient.create(SignUpApi::class.java)
//    }

    fun getCreateEventApi(): CreateEventApi {
        return retrofitClient.create(CreateEventApi::class.java)
    }

//    fun getEventApi(): EventApi {
//        return retrofitClient.create(EventApi::class.java)
//    }
//
//    fun getTagsApi(): TagsApi {
//        return retrofitClient.create(TagsApi::class.java)
//    }
//
//    fun getFeedbackApi(): FeedbackApi {
//        return retrofitClient.create(FeedbackApi::class.java)
//    }
//
//    fun getUserApi(): UserApi {
//        return retrofitClient.create(UserApi::class.java)
//    }
}