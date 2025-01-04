package com.example.managementapp.presentation.di

import android.content.Context
import androidx.room.Room
import com.example.managementapp.data.local.ProjectDatabase
import com.example.managementapp.data.remote.GithubApi
import com.example.managementapp.data.repository.GithubRepositoryImpl
import com.example.managementapp.domain.repository.GithubRepository
import com.example.managementapp.data.util.NetworkManager
import com.example.managementapp.data.util.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGithubApi(): GithubApi {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Accept", "application/vnd.github.v3+json")
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProjectDatabase(@ApplicationContext context: Context): ProjectDatabase {
        return Room.databaseBuilder(
            context,
            ProjectDatabase::class.java,
            "project_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGithubRepository(
        api: GithubApi,
        db: ProjectDatabase,
        networkManager: NetworkManager,
        resourceProvider: ResourceProvider
    ): GithubRepository {
        return GithubRepositoryImpl(api, db, networkManager,resourceProvider)
    }
}