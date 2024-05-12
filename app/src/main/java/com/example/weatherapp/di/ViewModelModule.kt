package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.repository.CurrentRepositoryImpl
import com.example.weatherapp.viewModel.CurrentViewModel
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
        repository: CurrentRepositoryImpl,
        @ApplicationContext context: Context
    ): CurrentViewModel {
        return CurrentViewModel( repository, context)
    }
}