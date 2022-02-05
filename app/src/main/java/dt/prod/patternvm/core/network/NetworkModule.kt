package dt.prod.patternvm.core.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dt.prod.patternvm.createProblem.network.CreateEventApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideCreateEventApi(): CreateEventApi {
        return NetworkHolder.getCreateEventApi()
    }
}