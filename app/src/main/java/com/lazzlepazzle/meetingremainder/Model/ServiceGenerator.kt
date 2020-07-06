package com.lazzlepazzle.meetingremainder.Model

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceGenerator(){
    companion object{
        val BASE_URL: String = "https://randomapi.com/api/"
        private val  httpClient: OkHttpClient.Builder =  OkHttpClient.Builder()
        private val  sbuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            fun <S> createService(serviceClass: Class<S>): S{
                val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                httpClient.addInterceptor(logging)
                val retrofit: Retrofit = sbuilder
                    .client(httpClient.build())
                    .build()
                return retrofit.create(serviceClass)
            }
    }
}