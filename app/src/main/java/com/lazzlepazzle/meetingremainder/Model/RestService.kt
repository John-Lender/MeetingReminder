package com.lazzlepazzle.meetingremainder.Model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RestService {
    @GET(" ")
    fun getNotes(@Query("key") key: String,
                 @Query("ref") ref: String,
                 @Query("results") results: Int): Call<NoteModel>
}