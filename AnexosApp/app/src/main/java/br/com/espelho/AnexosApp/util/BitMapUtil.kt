package br.com.espelho.AnexosApp.util

import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class BitMapUtil {

    fun converterByteArrayParaBitMap(byteArray: ByteArray): Bitmap{
        val imageStream = ByteArrayInputStream(byteArray)
        return BitmapFactory.decodeStream(imageStream)
    }

    fun converterBitMapParaByteArray(bitmap: Bitmap): ByteArray{

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}