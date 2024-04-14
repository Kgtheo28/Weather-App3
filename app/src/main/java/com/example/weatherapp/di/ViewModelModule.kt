package com.example.weatherapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideMyDataViewModel(
        apiInterface: ApiInterface,
        @ApplicationContext context: Context
    ): MyDataViewModel {
        return MyDataViewModel(apiInterface, context)
    }
}