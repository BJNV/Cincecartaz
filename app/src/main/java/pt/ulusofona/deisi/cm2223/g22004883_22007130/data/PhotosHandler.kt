package pt.ulusofona.deisi.cm2223.g22004883_22007130.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.net.URL

object PhotosHandler {
    fun bitmapToString(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, outputStream)
        val byteArray = outputStream.toByteArray()
        return org.apache.commons.codec.binary.Base64().encode(byteArray).toString(Charsets.UTF_8)
    }


    fun stringToBitmap(encodedString: String): Bitmap {
        val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    suspend fun downloadPoster(link: String):String{
        val url = URL(link)
        val imageData = url.readBytes()
        return org.apache.commons.codec.binary.Base64().encode(imageData).toString(Charsets.UTF_8)
    }
}