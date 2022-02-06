package dt.prod.patternvm.core.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dt.prod.patternvm.createProblem.domain.CreateEventApi
import dt.prod.patternvm.listProblem.domain.EventApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideCreateEventApi(): CreateEventApi {
        return NetworkHolder.getCreateEventApi()
    }

    @Singleton
    @Provides
    fun provideEventApi(): EventApi {
        return NetworkHolder.getEventApi()
    }
}