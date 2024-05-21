package com.example.domain.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.domain.Interface.RemoteMovieDataSource
import com.example.domain.di.IoDispatcher
import com.example.domain.entities.ErrorType
import com.example.domain.entities.Result
import com.example.domain.entities.Window
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject


/**
 * Entry point for managing network data and business logic for get Movies Use Case
 *
 * @param remoteDataSource - The network data source
 * @param dispatcher - The dispatcher to be used for long running or complex operations, such as ID
 * generation or mapping many models.
 */
class RemoteMovieRepository @Inject constructor(
    private val remoteDataSource: RemoteMovieDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun getMovies(window : Window, language: String, offset: Int) : Result<*> = withContext(dispatcher){
        try {
            return@withContext Result.Success(remoteDataSource.getTrendingMovies(window.name, language, offset))
        } catch (exception: Throwable) {
            when(exception){
                is HttpException ->  return@withContext Result.Error(ErrorType.HTTP.name)
                is IOException -> return@withContext  Result.Error(ErrorType.IO.name)
                is JsonDataException -> return@withContext Result.Error(ErrorType.MALFORMED.name)
                else -> {return@withContext Result.Error(ErrorType.OTHER.name)}
            }
        }
    }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun searchMovies(query :String, offset: Int) : Result<*> = withContext(dispatcher){
        try {
            return@withContext Result.Success(remoteDataSource.searchMovies(query, offset))
        } catch (exception: Throwable) {
            when(exception){
                is HttpException ->  return@withContext Result.Error(ErrorType.HTTP.name)
                is IOException -> return@withContext  Result.Error(ErrorType.IO.name)
                is JsonDataException -> return@withContext Result.Error(ErrorType.MALFORMED.name)
                else -> {return@withContext Result.Error(ErrorType.OTHER.name)}
            }
        }
    }
}







