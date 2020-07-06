package com.lazzlepazzle.meetingremainder.Model

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

class SaveData{
    companion object{
        private val FILE_NAME: String = "note_data.json"

        fun saveNote(context: Context, note: Note): Int{
            var size = 0
            val gson: Gson = Gson()
            var noteArray: NoteArray? = openNote(context)
            if (noteArray == null){
                noteArray = NoteArray(arrayListOf(note))
                size = 1
            }else{
                noteArray.items.add(0,note)
                size = noteArray.items.size
            }
            val jsonString: String = gson.toJson(noteArray)

            var fileOutputStream: FileOutputStream? = null

            try {
                fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
                fileOutputStream.write(jsonString.toByteArray())
                return size
            }finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                    }

                }
            }
            return size
        }
        fun openNote(context: Context): NoteArray? {
            var streamReader: InputStreamReader? = null
            var fileInputStream: FileInputStream? = null
            try {
                fileInputStream = context.openFileInput(FILE_NAME)
                streamReader =  InputStreamReader(fileInputStream)
                val gson: Gson  =  Gson()
                return gson.fromJson(streamReader, NoteArray::class.java)
            } catch (ex :IOException) {
                ex.printStackTrace()
            } finally {
                if (streamReader != null) {
                    try {
                        streamReader.close()
                    } catch (e :IOException) {
                        e.printStackTrace()
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close()
                    } catch ( e :IOException) {
                        e.printStackTrace()
                    }
                }
            }
            return null
        }
    }
}