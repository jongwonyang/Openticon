package io.ssafy.openticon.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ssafy.openticon.data.repository.EmoticonPackRepositoryImpl
import io.ssafy.openticon.data.repository.PointsRepositoryImpl
import io.ssafy.openticon.data.repository.PurchaseRepositoryImpl
import io.ssafy.openticon.domain.repository.EmoticonPackRepository
import io.ssafy.openticon.domain.repository.PointsRepository
import io.ssafy.openticon.domain.repository.PurchaseRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPurchaseRepository(impl: PurchaseRepositoryImpl): PurchaseRepository

    @Binds
    @Singleton
    abstract fun bindEmoticonPackRepository(impl: EmoticonPackRepositoryImpl): EmoticonPackRepository

    @Binds
    @Singleton
    abstract fun bindPointsRepository(impl: PointsRepositoryImpl): PointsRepository
}