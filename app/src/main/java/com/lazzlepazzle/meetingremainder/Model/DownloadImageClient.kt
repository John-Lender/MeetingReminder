package com.lazzlepazzle.meetingremainder.Model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import java.io.InputStream

class DownloadImageClient: AsyncTask<String, Void, Bitmap> {
    lateinit var bwImage: ImageView
    var bitmapData: Bitmap? = null
    constructor(bwImage: ImageView){
        this.bwImage = bwImage
    }

    override fun doInBackground(vararg params: String?): Bitmap? {
        val urlDisplay = params[0]
        var mIcon: Bitmap? = null
        try {
            val inputSteam: InputStream = java.net.URL(urlDisplay).openStream()
            mIcon = BitmapFactory.decodeStream(inputSteam)
        }catch (e: Exception){
            e.printStackTrace()
            Log.e("DOWNLOAD_IMAGE_CLIENT",e.toString())
        }

        return mIcon
    }

    override fun onPostExecute(result: Bitmap?) {
        bitmapData = result!!
        bwImage.setImageBitmap(result)
    }

}