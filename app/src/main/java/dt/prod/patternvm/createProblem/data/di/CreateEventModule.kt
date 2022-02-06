package dt.prod.patternvm.createProblem.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dt.prod.patternvm.createProblem.data.CreateProblemRepositoryImpl
import dt.prod.patternvm.createProblem.domain.CreateProblemRepository
import dt.prod.patternvm.createProblem.domain.CreateEventApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CreateProblemModule {

    @Singleton
    @Provides
    fun provideCreateProblemRepository(
        createEventApi: CreateEventApi
    ): CreateProblemRepository {
        return CreateProblemRepositoryImpl(createEventApi)
    }
}