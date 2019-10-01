package br.grupointegrado.tads.finallistgoalsapp.Utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import org.json.JSONArray
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class FileUtils {

    companion object {
        val FILE_NAME = "listgoals"
    }

    fun loadFile(context:Context):JSONArray?{
        try{
            val fileInput : FileInputStream = context.openFileInput(FILE_NAME)
            var strBuilder = StringBuilder()
            strBuilder.setLength(0)

            var line = fileInput.bufferedReader().readLine()
            while (line!=null){
                strBuilder.append(line).append("\n")
                line = fileInput.bufferedReader().readLine()
            }
            return if (strBuilder.length>0)JSONArray(strBuilder.toString()) else null
        }catch (ex:IOException){
            ex.printStackTrace()
            Toast.makeText(context, "O arquivo não existe",Toast.LENGTH_LONG).show()
            return null
        }
    }

    fun saveFile(context: Context, jsonArray: JSONArray?){
        try{
            val fileOutput : FileOutputStream = context.openFileOutput(FILE_NAME,Context.MODE_PRIVATE)
            if (jsonArray == null){
                fileOutput.write("".toByteArray())
                fileOutput.close()
                return
            }
            val strJsonArray = jsonArray.toString()
            fileOutput.write(strJsonArray.toByteArray())
            fileOutput.close()
        }catch (ex:IOException){
            ex.printStackTrace()
            Toast.makeText(context, "Não Foi possível criar o arquivo",Toast.LENGTH_LONG).show()
        }
    }

}