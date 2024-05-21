package com.example.data.di

import com.example.data.datasource.RemoteMovieDataSourceImpl
import com.example.domain.Interface.RemoteMovieDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {
    @Binds
    abstract fun bindRemoteMovieDataSource(remoteMovieDataSourceImpl: RemoteMovieDataSourceImpl) : RemoteMovieDataSource
}