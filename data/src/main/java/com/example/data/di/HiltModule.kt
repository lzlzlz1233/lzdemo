package com.example.data.di
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.network.GetMovieApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_preferences")

val API_KEY = "d0bfa2d663af7a94e515085e33ab9615"

@Module
@InstallIn(SingletonComponent::class)
class HiltModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor {chain ->
            val url = chain.request().url().newBuilder().addQueryParameter("api_key",API_KEY).build()
            val request = chain.request().newBuilder().url(url).build()
            chain.proceed(request)
        }
        .readTimeout(15, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .build()

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .build()

    @Provides
    fun providesRemoteMovieDataSource(retrofit: Retrofit) : GetMovieApiService = retrofit.create(GetMovieApiService :: class.java)


//    @Provides
//    fun provideLocalInteractionDataSourceImpl(@ApplicationContext context: Context) =
//        PersistenceDataSourceImpl(context.dataStore)

//    @Provides
//    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
//        Room.databaseBuilder(
//            context,
//            AppDatabase::class.java, "my-database"
//        ).build()

//    @Provides
//    fun provideUserDao(appDatabase: AppDatabase): EmployeeDao = appDatabase.employeeDao()

    companion object {
        private const val BASE_URL = "https://s3.amazonaws.com/sq-mobile-interview/"
    }

}