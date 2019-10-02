package br.com.espelho.AnexosApp.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import br.com.espelho.AnexosApp.Entities.AnexoEntity
import br.com.espelho.AnexosApp.constants.DataBaseConstants
import br.com.espelho.AnexosApp.enuns.TipoAnexoEnum
import java.lang.Exception

class AnexoRepository private constructor(context: Context){

    private var mTaskDataBaseHelper: TaskDataBaseHelper = TaskDataBaseHelper(context)

    companion object{
        private var INSTANCE : AnexoRepository? = null

        fun getInstance(context : Context) : AnexoRepository{
            if (INSTANCE == null){
                INSTANCE = AnexoRepository(context)
            }
            return INSTANCE as AnexoRepository
        }
    }

    fun insert(usuarioId: Int){
        try {
            val db = mTaskDataBaseHelper.writableDatabase
            var i = 0
            while (i < 3){
                when(i){
                    0 ->{
                        val insertValues = ContentValues()
                        insertValues.put(DataBaseConstants.ANEXO.COLUMNS.USER, usuarioId)
                        insertValues.put(DataBaseConstants.ANEXO.COLUMNS.TIPOANEXO, TipoAnexoEnum.CPF.name)
                        db.insert(DataBaseConstants.ANEXO.TABLE_NAME, null, insertValues)
                        i++
                    }
                    1 ->{
                        val insertValues = ContentValues()
                        insertValues.put(DataBaseConstants.ANEXO.COLUMNS.USER, usuarioId)
                        insertValues.put(DataBaseConstants.ANEXO.COLUMNS.TIPOANEXO, TipoAnexoEnum.RG.name)
                        db.insert(DataBaseConstants.ANEXO.TABLE_NAME, null, insertValues)
                        i++
                    }
                    2 ->{
                        val insertValues = ContentValues()
                        insertValues.put(DataBaseConstants.ANEXO.COLUMNS.USER, usuarioId)
                        insertValues.put(DataBaseConstants.ANEXO.COLUMNS.TIPOANEXO, TipoAnexoEnum.CONTA_DE_LUZ.name)
                        db.insert(DataBaseConstants.ANEXO.TABLE_NAME, null, insertValues)
                        i++
                    }
                }
            }
        }catch (e : Exception){
            throw e
        }
    }

    fun getList(usuarioId: Int) : MutableList<AnexoEntity> {
        val list = mutableListOf<AnexoEntity>()
        try {
            val cursor: Cursor
            val db = mTaskDataBaseHelper.readableDatabase
            val projection = arrayOf(
                DataBaseConstants.ANEXO.COLUMNS.ID,
                DataBaseConstants.ANEXO.COLUMNS.TIPOANEXO,
                DataBaseConstants.ANEXO.COLUMNS.ANEXO
            )
            val selection = "${DataBaseConstants.ANEXO.COLUMNS.USER} = ?"
            val selectionArgs = arrayOf(usuarioId.toString())
            cursor = db.query(DataBaseConstants.ANEXO.TABLE_NAME, projection, selection, selectionArgs, null, null, null)
            if (cursor.count > 0 ){
                cursor.moveToFirst()
                do {
                    val id = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.ANEXO.COLUMNS.ID))
                    val tipoAnexo = TipoAnexoEnum.valueOf(cursor.getString(cursor.getColumnIndex(DataBaseConstants.ANEXO.COLUMNS.TIPOANEXO)))
                    val anexo = cursor.getBlob(cursor.getColumnIndex(DataBaseConstants.ANEXO.COLUMNS.ANEXO))
                    list.add(AnexoEntity(id,usuarioId,tipoAnexo,anexo))
                } while (cursor.moveToNext())
            }
            cursor.close()
        }catch (e:Exception){
            return list
        }
        return list
    }

    fun get(id: Int) : AnexoEntity? {
        var anexoEntity : AnexoEntity? = null
        try {
            val cursor: Cursor
            val db = mTaskDataBaseHelper.readableDatabase
            val projection = arrayOf(
                DataBaseConstants.ANEXO.COLUMNS.USER,
                DataBaseConstants.ANEXO.COLUMNS.TIPOANEXO,
                DataBaseConstants.ANEXO.COLUMNS.ANEXO
            )
            val selection = "${DataBaseConstants.ANEXO.COLUMNS.ID} = ?"
            val selectionArgs = arrayOf(id.toString())
            cursor = db.query(DataBaseConstants.ANEXO.TABLE_NAME, projection, selection, selectionArgs, null, null, null)
            if (cursor.count > 0 ){
                cursor.moveToFirst()
                val usuarioId = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.ANEXO.COLUMNS.USER))
                val tipoAnexo = TipoAnexoEnum.valueOf(cursor.getString(cursor.getColumnIndex(DataBaseConstants.ANEXO.COLUMNS.TIPOANEXO)))
                val anexo = cursor.getBlob(cursor.getColumnIndex(DataBaseConstants.ANEXO.COLUMNS.ANEXO))
                anexoEntity = AnexoEntity(id,usuarioId,tipoAnexo,anexo)
            }
            cursor.close()
        }catch (e:Exception){
            return anexoEntity
        }
        return anexoEntity
    }

    fun update(anexoEntity: AnexoEntity){
        try {
            val db = mTaskDataBaseHelper.writableDatabase
            val updateValues = ContentValues()
            updateValues.put(DataBaseConstants.ANEXO.COLUMNS.ANEXO, anexoEntity!!.anexo)
            val selection = "${DataBaseConstants.ANEXO.COLUMNS.ID} = ?"
            val selectionArgs = arrayOf(anexoEntity.id.toString())
            db.update(DataBaseConstants.ANEXO.TABLE_NAME, updateValues, selection, selectionArgs)
        }catch (e:Exception){
            throw e
        }
    }


}