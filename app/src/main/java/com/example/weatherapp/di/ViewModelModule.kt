package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.interfaces.ApiInterface
import com.example.weatherapp.interfaces.CapeTownInterface
import com.example.weatherapp.interfaces.PretoriaApiInterface
import com.example.weatherapp.repository.CurrentRepositoryImpl
import com.example.weatherapp.repository.CurrentViewModel
import com.example.weatherapp.repository.MyDataViewModel
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
        pretoriaApiInterface: PretoriaApiInterface,
        @ApplicationContext context: Context
    ): MyDataViewModel {
        return MyDataViewModel(apiInterface, pretoriaApiInterface, context)
    }

    @Provides
    fun provideCurrentViewModel (
        repository: CurrentRepositoryImpl,
        @ApplicationContext context: Context
    ): CurrentViewModel {
        return CurrentViewModel( repository, context)
    }
}