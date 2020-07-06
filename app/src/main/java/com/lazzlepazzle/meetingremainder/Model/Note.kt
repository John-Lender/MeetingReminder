package com.lazzlepazzle.meetingremainder.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class Note(val title: String,
                val date: String,
                val time: String?,
                val fullNameClient: String,
                val email: String,
                val photo: ByteArray)

data class NoteArray(val items: ArrayList<Note>)

class Result{
    @SerializedName("name_client")
    @Expose
    public val nameClient: String? = null
    @SerializedName("email")
    @Expose
    public val email: String? = null
    @SerializedName("photo")
    @Expose
    public val photo: String? = null

}

class NoteModel {
    @SerializedName("results")
    @Expose
    val results: List<Result>? = null

}
