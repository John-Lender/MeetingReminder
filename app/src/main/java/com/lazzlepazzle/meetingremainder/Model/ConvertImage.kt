package com.lazzlepazzle.meetingremainder.Model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class ConvertImage {
    companion object{
        val COMPRESSION_QUALITY: Int = 100
        fun decodeImageToByteArray(image: Bitmap):ByteArray{
            val byteArrayBitmapStream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY, byteArrayBitmapStream)
            return byteArrayBitmapStream.toByteArray()
        }
        fun ByteArrayToBitmap( byteArray:ByteArray):Bitmap{
            return  BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }
}