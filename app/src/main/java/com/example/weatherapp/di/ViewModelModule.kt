package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.repository.RepositoryImpl
import com.example.weatherapp.viewModel.CurrentWeatherViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun provideCurrentViewModel (
        repository: RepositoryImpl,
        @ApplicationContext context: Context
    ): CurrentWeatherViewModel {
        return CurrentWeatherViewModel(repository, context)
    }
}