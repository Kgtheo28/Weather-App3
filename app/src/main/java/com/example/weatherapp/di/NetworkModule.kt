package com.example.weatherapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.weatherapp.data.WeatherDao
import com.example.weatherapp.data.WeatherDatabase
import com.example.weatherapp.interfaces.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    //Room Database
    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase{
        return Room.databaseBuilder(context, WeatherDatabase::class.java, "weather_database2").build()
    }

    @Provides
    fun provideWeatherDataDao(weatherDatabase: WeatherDatabase): WeatherDao {
        return weatherDatabase.weatherDao()
    }
}