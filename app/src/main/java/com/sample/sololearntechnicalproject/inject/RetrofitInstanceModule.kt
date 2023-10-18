package com.sample.sololearntechnicalproject.inject

import android.content.Context
import com.sample.sololearntechnicalproject.data.api.GitHubApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


private const val BASE_URL_GITHUB_API = "https://api.github.com/"

@Module
@InstallIn(SingletonComponent::class)
class RetrofitInstanceModule {


    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context, onlineInterceptor: Interceptor): OkHttpClient{
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val cache = Cache(context.cacheDir, cacheSize)

        return OkHttpClient.Builder()
            .addNetworkInterceptor(onlineInterceptor)
            .cache(cache)
            .build()
    }

    @Provides
    @Singleton
    @Named("Retrofit")
    fun theRetrofitInstance(okHttpClient: OkHttpClient): GitHubApiService {
        val api: GitHubApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL_GITHUB_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(GitHubApiService::class.java)
        }
        return api
    }

    @Provides
    @Singleton
    fun getInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val maxAge = 60 // read from cache for 60 seconds even if there is internet connection
            response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Pragma")
                .build()
        }
    }

}