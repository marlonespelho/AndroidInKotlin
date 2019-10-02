package br.com.espelho.AnexosApp.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.IOException
import java.lang.Exception

class FileUtil {

    fun getTempFile(context: Context, url: String): File? =
        Uri.parse(url)?.lastPathSegment?.let { filename ->
            File.createTempFile(filename, null, context.cacheDir)
        }
    fun getCacheFile(dir: String, fileName: String): File?{
        try {
            return File(dir, fileName)
        }catch (e:IOException){
            throw e
            return null
        }
    }

    fun deleteTempFile(file: File){
        file.delete()
    }
}