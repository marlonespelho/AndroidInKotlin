package br.com.espelho.AnexosApp.util

import android.content.Context
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result

class BarCodeSharedPreferences {
    companion object {

        private val SP_NAME = "SP"
        val KEY_NAME = "text"
        val KEY_BARCODE_NAME = "barcode_name"

        fun saveResult(context: Context, result: Result? = null) {
            val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            val contents = result?.text
            val barcodeName = result?.barcodeFormat?.name

            sp.edit()
                .putString(KEY_NAME, contents)
                .putString(KEY_BARCODE_NAME, barcodeName)
                .apply()
        }

        fun getSavedResult(context: Context): Result? {
            val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            val text = sp.getString(KEY_NAME, null)

            if (text == null) {
                return null
            }

            val barcodeFormat = BarcodeFormat
                .valueOf(sp.getString(KEY_BARCODE_NAME, null)!!)
            val result = Result(
                text,
                text.toByteArray(),
                arrayOf(),
                barcodeFormat)

            return result
        }

        fun clearSavedResult(){

        }
    }
}