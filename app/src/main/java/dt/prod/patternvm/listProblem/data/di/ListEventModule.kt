package dt.prod.patternvm.listProblem.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dt.prod.patternvm.listProblem.data.PlansRepositoryImpl
import dt.prod.patternvm.listProblem.domain.EventApi
import dt.prod.patternvm.listProblem.domain.PlansRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ListProblemModule {

    @Singleton
    @Provides
    fun providePlansRepository(
        createEventApi: EventApi
    ): PlansRepository {
        return PlansRepositoryImpl(createEventApi)
    }
}