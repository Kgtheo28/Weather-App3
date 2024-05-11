package com.example.weatherapp.di

import android.app.Application
import android.content.Context
import com.example.weatherapp.interfaces.ApiInterface
import com.example.weatherapp.interfaces.CapeTownInterface
import com.example.weatherapp.interfaces.PretoriaApiInterface
import com.example.weatherapp.repository.CurrentRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    const val Base_Url = "https://api.openweathermap.org/data/2.5/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Base_Url)
            .build()
    }


    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }


    @Provides
    @Singleton
    fun providePretoriaApiInterface(retrofit: Retrofit): PretoriaApiInterface {
        return retrofit.create(PretoriaApiInterface::class.java)
    }

    // Cape Town Current Weather Interface
    @Provides
    @Singleton
    fun provideCapeTownCurrentInterface(retrofit: Retrofit): CapeTownInterface {
        return retrofit.create(CapeTownInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }



}